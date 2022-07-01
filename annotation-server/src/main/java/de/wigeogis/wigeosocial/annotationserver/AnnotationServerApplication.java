package de.wigeogis.wigeosocial.annotationserver;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Scene;
import de.wigeogis.wigeosocial.annotationserver.annotation.service.SceneService;
import de.wigeogis.wigeosocial.annotationserver.utils.ImageMetadataExtractor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SpringBootApplication
public class AnnotationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnnotationServerApplication.class, args);
	}

}
