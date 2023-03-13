package de.wigeogis.wigeosocial.annotationserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.wigeogis.wigeosocial.annotationserver.annotation.dao.ImageRecordingDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.*;
import de.wigeogis.wigeosocial.annotationserver.annotation.service.*;
import de.wigeogis.wigeosocial.annotationserver.security.services.UserDetailsImpl;
import de.wigeogis.wigeosocial.annotationserver.storage.FileStorage;
import de.wigeogis.wigeosocial.annotationserver.utils.IPDetector;
import de.wigeogis.wigeosocial.annotationserver.utils.RandomGenerator;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SubjectivityController {

    @Autowired
    private UserService userManagerService;


    @Autowired
    public PathService pathService;

    @Autowired
    public ImagePointService imagePointService;

    @Autowired
    public ImageRecordingService imageRecordingService;

    @Autowired
    public SessionService sessionService;

    @Autowired
    public LogService logService;

    @Autowired
    public ImageRateService imageRateService;

    @Autowired
    public SimilarImageRateService similarImageRateService;

    @Autowired
    private FileStorage fileStorage;

    @Value("${ai.similarity.api.url}")
    private String similarityAPI;


    @PostMapping(value = "session/init")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String saveSession(@RequestBody Session session, HttpServletRequest request) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userid = ((UserDetailsImpl) principal).getId();
        User user = userManagerService.getUserByUserID(userid);

        JsonObject jsonObject = new JsonObject();

        if (user != null && user.getProlific() != null && user.getProlific() == true && user.getTasks() >= 3) {
            jsonObject.addProperty("invalid", true);
        } else {
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            session.setBrowserName(userAgent.getBrowser().getName());
            session.setBrowserVersion(userAgent.getBrowserVersion().toString());
            session.setOperatingSystem(userAgent.getOperatingSystem().getName());
            session.setCreationDate(new Timestamp(new Date().getTime()));
            session.setIpAddress(IPDetector.getClientIp(request));

            if (session.getSessionId() != null)
                session.setSessionId(session.getSessionId());
            else {
                String sessionId = UUID.randomUUID().toString();
                session.setSessionId(sessionId);
            }

            if (session.getWidth() != null)
                session.setWidth(session.getWidth());
            if (session.getHeight() != null)
                session.setHeight(session.getHeight());
            if (session.getPid() != null)
                session.setPid(session.getPid());

            if (session.getStudyId() != null)
                session.setStudyId(session.getStudyId());
            if (session.getTimeElapsed() != null)
                session.setTimeElapsed(session.getTimeElapsed());
            if (session.getInstructionTimeEffort() != null)
                session.setInstructionTimeEffort(session.getInstructionTimeEffort());

            Session newSession = sessionService.insert(session);

            jsonObject.addProperty("sessionId", newSession.getSessionId());
            jsonObject.addProperty("instruction", user.getShowInstruction());

            if (user.getShowInstruction() != null && user.getShowInstruction() == true) {
                user.setShowInstruction(false);
                this.userManagerService.createNewUser(user);
            }
        }
        return jsonObject.toString();
    }


    @PostMapping(value = "session/complete")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String completeTask(@RequestBody Session session) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userid = ((UserDetailsImpl) principal).getId();
        User user = userManagerService.getUserByUserID(userid);
        user.setTasks(user.getTasks() + 1);
        this.userManagerService.createNewUser(user);

        JsonObject jsonObject = new JsonObject();
        Session mySession = sessionService.findRowBySessionId(session.getSessionId());
        mySession.setComplete(true);
        mySession.setFinishDate(new Timestamp(new Date().getTime()));
        sessionService.insert(mySession);

        jsonObject.addProperty("sessionId", mySession.getSessionId());
        jsonObject.addProperty("counter", user.getTasks());
        if(user.getProlific() != null && user.getProlific() == true && user.getTasks() >= 3)
            jsonObject.addProperty("redirect", "https://app.prolific.co/submissions/complete?cc=8E13512C");
        return jsonObject.toString();
    }


    @PostMapping(value = "log/save")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String saveLog(@RequestBody Log log) throws Exception {
        log.setRecordedAt(new Timestamp(new Date().getTime()));
        Log newLog = logService.insert(log);
        final Long logId = newLog.getId();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("logId", logId);
        return jsonObject.toString();
    }


    @GetMapping(value = {"path/get","path/get/{pathId}"})
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String getPath(@PathVariable(value = "pathId",required = false) Integer pathId) throws Exception {
        if (pathId == null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            long userid = ((UserDetailsImpl) principal).getId();
            User user = userManagerService.getUserByUserID(userid);
            if (user.getProlific() != null && user.getProlific() == true) {
                List<Integer> userPaths = logService.findPathByUserId(user.getUsername());
                pathId = pathService.getAvailablePath(userPaths);
                pathService.updateCounter(pathId);
            } else
                pathId = RandomGenerator.getRandomNumberInRange(1, 27);
        }

        final Integer id = pathId;
        List<ImagePoint> recordings = imagePointService.getAll().stream().filter(
                imagePoint -> imagePoint.getPathId() == id
        ).collect(Collectors.toList());
        String json = newGson().toJson(recordings);
        return json;
    }


    @PostMapping(value = "path/update")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String saveImagePoint(@RequestBody ImagePoint imagePoint) throws Exception {
        /*ImagePoint image = imagePointService.findRowByImageId(imagePoint.getImageId());
        if (imagePoint.getOrderId() == -1)
            imagePointService.delete(image);
        else {
            image.setOrderId(imagePoint.getOrderId());
            image.setStart(imagePoint.getStart());
            image.setEnd(imagePoint.getEnd());
            imagePointService.insert(image);
        }*/
        return "true";
    }


    @PostMapping("image/upload")
    //@PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String uploadFile( @RequestPart("file") MultipartFile file) throws Exception {
        String fileName = fileStorage.storeDownloadFile(file, true);
        Map<String, Object> response = new HashMap<>();
        response.put("name", fileName);
        return newGson().toJson(response);
    }


    @PostMapping(value = "image/score")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String saveScore(@RequestParam("rate") String rateData, @RequestParam("file") MultipartFile file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ImageRate rate = mapper.readValue(rateData, ImageRate.class);
        List<Map<String, Object>> similarImages = null;
        try {
            String fileId = fileStorage.storeFile(file, true);
            similarImages = getSimilarImages(fileId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        rate.setCreationDate(new Timestamp(new Date().getTime()));
        ImageRate imageRate = imageRateService.insert(rate);
        Map<String, Object> response = new HashMap<>();
        response.put("rateId", imageRate.getId());
        response.put("similars", similarImages);
        return newGson().toJson(response);
    }


    @PostMapping(value = "similar/score")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String saveSimilarScore(@RequestBody SimilarImageRate rate) throws Exception {
        rate.setCreationDate(new Timestamp(new Date().getTime()));
        SimilarImageRate imageRate = similarImageRateService.insert(rate);

        Map<String, Object> response = new HashMap<String, Object>() {{
            put("rateId", imageRate.getId());
        }};
        return newGson().toJson(response);
    }


    @GetMapping("similars/{image:.+}")
    public ResponseEntity<Resource> getSimilarImage(@PathVariable("image") String image, HttpServletRequest request) throws IOException {

        Resource resource = fileStorage.loadSimilarImageAsResource(image);
        BufferedImage originalImage = ImageIO.read(resource.getURL());

        Dimension scaledDim = getScaledDimension(originalImage,new Dimension(1000,1000));
        BufferedImage resizedImage = new BufferedImage(scaledDim.width, scaledDim.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, scaledDim.width, scaledDim.height, null);
        graphics2D.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        byte[] media = baos.toByteArray();

        Resource result = new ByteArrayResource(media);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            //logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(result);
    }


    public static Dimension getScaledDimension(BufferedImage imgSize, Dimension boundary) {
        int original_width = imgSize.getWidth();
        int original_height = imgSize.getHeight();
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;
        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }
        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }
        return new Dimension(new_width, new_height);
    }


    public List<Map<String, Object>> getSimilarImages(String imageName) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        String targetURL = this.similarityAPI + "search";
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

}
