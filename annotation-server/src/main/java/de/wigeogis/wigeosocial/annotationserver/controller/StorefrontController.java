package de.wigeogis.wigeosocial.annotationserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.GsonBuilder;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Annotation;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Image;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Label;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Scene;
import de.wigeogis.wigeosocial.annotationserver.annotation.service.AnnotationService;
import de.wigeogis.wigeosocial.annotationserver.annotation.service.LabelService;
import de.wigeogis.wigeosocial.annotationserver.annotation.service.SceneService;
import de.wigeogis.wigeosocial.annotationserver.annotation.service.TaskService;
import de.wigeogis.wigeosocial.annotationserver.storage.FileStorage;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api")
public class StorefrontController {

    @Autowired
    public SceneService sceneService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private AnnotationService annotationService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FileStorage fileStorage;


    @Value("${ai.similarity.api.url}")
    private String storeDetectionAPI;


    @GetMapping(value = "scene/get")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String getScene() throws Exception {
        Scene scene = sceneService.getOneRandomly();
        scene.setActive(false);
        scene.setUpdateAt(new Timestamp(new Date().getTime()));
        sceneService.insert(scene);
        String json = new Gson().toJson(scene);
        return json;
    }



    @GetMapping(value = {"labels/all","labels/all/{language}"})
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String getAllLabels(@PathVariable(value = "language",required = false) String language) throws Exception {
        List<Label> labels = labelService.getAll();
        List<Map<String, Object>> result = new ArrayList<>();
        if (language != null && language.equals("de")) {
            Collections.sort(labels, Comparator.comparing(Label::getName_de));
            labels.forEach(label -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", label.getId());
                item.put("name", label.getName_de());
                result.add(item);
            });
        } else {
            Collections.sort(labels, Comparator.comparing(Label::getName_en));
            labels.forEach(label -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", label.getId());
                item.put("name", label.getName_en());
                result.add(item);
            });
        }

        String json = new Gson().toJson(result);
        return json;
    }


    @PostMapping("task/save")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String saveTask(@RequestBody Annotation[] annotations) throws Exception {
        JsonObject jsonObject = new JsonObject();
        try {
            Timestamp creationDate = new Timestamp(new Date().getTime());
            for (Annotation annotation : annotations) {
                annotation.setCreationDate(creationDate);
                this.annotationService.insert(annotation);
            }

            jsonObject.addProperty("status", "ok");

        } catch (Exception ex) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("error", ex.getMessage());
        }
        return jsonObject.toString();
    }


    @GetMapping(value = "task/get/{uid}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String getTask(@PathVariable("uid") String userId) throws Exception {
        Map<String, Object> task = new HashMap<>();
        try {
            List<Image> images = taskService.getAllImagesByAnnotationCount(1);
            if (images.size() > 0) {
                Collections.shuffle(images, new Random());
                for (Image image : images) {
                    task.put("status", "ok");
                    task.put("id", image.getId());
                    task.put("image_name", image.getName());
                    task.put("prediction", 1);
                    break;
                }
            } else {
                task.put("status", "error");
                task.put("error", "there is no task available");
            }
        } catch (Exception ex) {
            task.put("status", "error");
            task.put("error", ex.getMessage());
        }
        String json = new Gson().toJson(task);
        return json;
    }


    @GetMapping("annotation/counter")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String getAnnotationCounter() throws Exception {
        Map<String, Object> result = new HashMap<>();
        Long total = sceneService.countTotalScenes();
        Long checked = sceneService.countCheckedScenes();
        Long annotations = annotationService.countAnnotations();
        result.put("total", total);
        result.put("checked", checked);
        result.put("annotations", annotations);
        String json = new Gson().toJson(result);
        return json;
    }


    @PostMapping(value = "store/upload", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<?> uploadStoreImage(@RequestPart("input") String input, @RequestPart("file") MultipartFile file) throws Exception {

        System.out.println("jsonData: " + input);
        final String fileName = fileStorage.storeFile(file, true);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> params = mapper.readValue(input, new TypeReference<Map<String, Object>>() {
                });

                final Double currentLat = Double.parseDouble(params.get("y").toString());
                final Double currentLng = Double.parseDouble(params.get("x").toString());
                List<Map<String, Object>> tmp = detectStores(fileName);

                List<Map<String, Object>> response = IntStream.range(0, tmp.size())
                        .mapToObj(i -> {
                            if (tmp.get(i).get("id").equals("all")) {
                                tmp.get(i).put("y", currentLat);
                                tmp.get(i).put("x", currentLng);
                                tmp.get(i).put("imagewidth", params.get("imagewidth"));
                            }else {
                                tmp.get(i).put("imagewidth", "100");
                                tmp.get(i).put("y", currentLat + 0.000195);
                                tmp.get(i).put("x", currentLng + (0.000139 * (i - 2)));
                            }
                            return tmp.get(i);
                        })
                        .collect(Collectors.toList());

                RestTemplate restTemplate = new RestTemplate();
                String targetURL = "https://social-data.wigeogis.com/socialdataws/services/wg_extract_poidata_from_image?" +
                        "method=runJob&" +
                        "arg50=--context_param lang=de&" +
                        "arg99=--context_param editkey=" + params.get("editkey").toString() + "&" +
                        "arg1=--context_param parentguid=" + params.get("parentguid").toString() + "&" +
                        "arg2=--context_param username=" + params.get("username").toString() + "&" +
                        "arg3=--context_param operation=updatedb&" +
                        "arg4=--context_param jsondata=" + encode(newGson().toJson(response));

                System.out.println(targetURL);

                ResponseEntity<String> result = restTemplate.getForEntity(targetURL, String.class);
                System.out.println(result.getBody());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return ResponseEntity.ok().body("Image is saved and currently being processed!");
    }


    @PostMapping("store/street/upload")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String uploadStreetImage(@RequestPart("file") MultipartFile file) throws Exception {
        String fileName = fileStorage.storeFile(file, true);
        List<Map<String, Object>> response = detectStores(fileName);
       // Map<String, Object> response = new HashMap<>();
       // response.put("name", fileName);
        return newGson().toJson(response);
    }


    public List<Map<String, Object>> detectStores(String imageName) throws Exception{

        RestTemplate restTemplate = new RestTemplate();
        String targetURL = this.storeDetectionAPI + "store";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        String filePath = fileStorage.getTempStorageLocation().toAbsolutePath().toString();
        filePath = filePath + "/" + imageName;
        FileInputStream inputStream = new FileInputStream(filePath);
        MultipartFile multipartFile = new MockMultipartFile(filePath, filePath,
                URLConnection.getFileNameMap().getContentTypeFor(filePath), inputStream);
        body.add("file", multipartFile.getResource());
        body.add("query", imageName);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(targetURL, requestEntity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> similarImages = mapper.readValue(response.getBody(), new TypeReference<List<Map<String, Object>>>(){});
        return similarImages;
    }


    public List<Map<String, Object>> detectStores2(String imageName) throws Exception{

        RestTemplate restTemplate = new RestTemplate();
        String targetURL = this.storeDetectionAPI + "store";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        String filePath = fileStorage.getTempStorageLocation().toAbsolutePath().toString();
        filePath = filePath + "/" + imageName;
        FileInputStream inputStream = new FileInputStream(filePath);
        MultipartFile multipartFile = new MockMultipartFile(filePath, filePath,
                URLConnection.getFileNameMap().getContentTypeFor(filePath), inputStream);
        body.add("file", multipartFile.getResource());
        body.add("query", imageName);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(targetURL, requestEntity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> similarImages = mapper.readValue(response.getBody(), new TypeReference<List<Map<String, Object>>>(){});
        return similarImages;
    }


    private static Gson newGson() {
        return new GsonBuilder().create();
    }

    private static String encode(String json) {
        byte[] encodedBytes = Base64.getEncoder().encode(json.getBytes());
        String b64encodedjsonData = "b64::".concat((new String(encodedBytes)).replace("+", "~"));
        System.out.println("jsonData: " + json);
        System.out.println("b64encodedstring: " + b64encodedjsonData);
        return b64encodedjsonData;
    }


    public static void main(String[] args) {
        String jsonData = encode("");
        byte[] encodedBytes = Base64.getDecoder().decode(jsonData);
        System.out.println("jsonData: " + new String(encodedBytes));
    }

}
