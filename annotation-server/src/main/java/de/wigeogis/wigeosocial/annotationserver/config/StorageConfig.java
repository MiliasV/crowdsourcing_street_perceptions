package de.wigeogis.wigeosocial.annotationserver.config;

import de.wigeogis.wigeosocial.annotationserver.storage.FileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {


    @Value("${server.files.storage}")
    private String rootFolderPath;

    @Value("${server.images.path}")
    private String imageFolderPath;

    @Value("${server.temp.path}")
    private String imageTempPath;

    @Value("${server.download.path}")
    private String imageDownloadPath;


    public String getRootFolderPath() {
        return rootFolderPath;
    }

    public void setRootFolderPath(String rootFolderPath) {
        this.rootFolderPath = rootFolderPath;
    }

    public String getImageFolderPath() {
        return imageFolderPath;
    }

    public void setImageFolderPath(String imageFolderPath) {
        this.imageFolderPath = imageFolderPath;
    }

    public String getImageTempPath() {
        return imageTempPath;
    }

    public void setImageTempPath(String imageTempPath) {
        this.imageTempPath = imageTempPath;
    }

    public String getImageDownloadPath() {
        return imageDownloadPath;
    }

    public void setImageDownloadPath(String imageDownloadPath) {
        this.imageDownloadPath = imageDownloadPath;
    }
}
