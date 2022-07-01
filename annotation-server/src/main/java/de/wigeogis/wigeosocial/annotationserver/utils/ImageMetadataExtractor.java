package de.wigeogis.wigeosocial.annotationserver.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.jpeg.JpegDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ImageMetadataExtractor {
    public static class ImageInformation {
        private final String name;
        private final int width;
        private final int height;
        private final Date date;
        private final double orientation;
        private final double latitude;
        private final double longitude;

        public ImageInformation(String name, int width, int height, Date date, double orientation, double latitude, double longitude) {
            this.name = name;
            this.width = width;
            this.height = height;
            this.date = date;
            this.orientation = orientation;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public Date getDate() {
            return date;
        }

        public double getOrientation() {
            return orientation;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        @Override
        public String toString() {
            return "ImageInformation{" +
                    "name='" + name + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    ", date=" + date +
                    ", orientation=" + orientation +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }


    public static ImageInformation readImageInformation(File imageFile) throws IOException, MetadataException, ImageProcessingException {
        ImageInformation imageInformation = null;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
            Directory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
            FileSystemDirectory fileDirectory = metadata.getFirstDirectoryOfType(FileSystemDirectory.class);

            double orientation = 0.0;
            double latitude = 0.0;
            double longitude = 0.0;
            int width = 0;
            int height = 0;
            if (gpsDirectory != null) {
                orientation = gpsDirectory.getDouble(GpsDirectory.TAG_IMG_DIRECTION);
                GeoLocation geoLocation = ((GpsDirectory) gpsDirectory).getGeoLocation();
                if (geoLocation != null) {
                    latitude = geoLocation.getLatitude();
                    longitude = geoLocation.getLongitude();
                }
            }
            String name = fileDirectory.getString(FileSystemDirectory.TAG_FILE_NAME);
            if (jpegDirectory != null) {
                width = jpegDirectory.getImageWidth();
                height = jpegDirectory.getImageHeight();
            }
            Date date = exifDirectory.getDate(ExifIFD0Directory.TAG_DATETIME);
            imageInformation = new ImageInformation(name, width, height, date, orientation, latitude, longitude);
        }catch (Exception ex){
            System.out.println("Error happened!");
        }
        return imageInformation;
    }
}
