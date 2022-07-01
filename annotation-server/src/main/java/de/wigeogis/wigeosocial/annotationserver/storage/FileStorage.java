package de.wigeogis.wigeosocial.annotationserver.storage;

import de.wigeogis.wigeosocial.annotationserver.config.StorageConfig;
import de.wigeogis.wigeosocial.annotationserver.exception.FileNotFoundException;
import de.wigeogis.wigeosocial.annotationserver.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorage {

    private final Path imageStorageLocation;
    private final Path tempStorageLocation;
    private final Path downloadStorageLocation;

    @Autowired
    public FileStorage(StorageConfig fileStorageConfig) {

        this.imageStorageLocation = Paths.get(
                fileStorageConfig.getRootFolderPath() + fileStorageConfig.getImageFolderPath()
        ).toAbsolutePath().normalize();

        this.tempStorageLocation = Paths.get(
                fileStorageConfig.getRootFolderPath() + fileStorageConfig.getImageTempPath()
        ).toAbsolutePath().normalize();

        this.downloadStorageLocation = Paths.get(
                fileStorageConfig.getRootFolderPath() + fileStorageConfig.getImageDownloadPath()
        ).toAbsolutePath().normalize();

        /*try {
            Files.createDirectories(this.imageStorageLocation);
            Files.createDirectories(this.tempStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }*/
    }

    public String storeFile(MultipartFile file, boolean isTemp) {
        // Normalize file name
        String fileId = UUID.randomUUID().toString();
        String fileName = fileId + ".jpg";

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = (isTemp == true)
                    ? this.tempStorageLocation.resolve(fileName)
                    : this.imageStorageLocation.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public String storeDownloadFile(MultipartFile file, boolean isDownload) {
        // Normalize file name

        String fileName = file.getOriginalFilename() + ".png";

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = (isDownload == true)
                    ? this.downloadStorageLocation.resolve(fileName)
                    : this.imageStorageLocation.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadSimilarImageAsResource(String fileName) {
        try {
            Path filePath = this.imageStorageLocation.resolve(imageStorageLocation + "/" + fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }

    public Path getImageStorageLocation() {
        return imageStorageLocation;
    }

    public Path getTempStorageLocation() {
        return tempStorageLocation;
    }
}
