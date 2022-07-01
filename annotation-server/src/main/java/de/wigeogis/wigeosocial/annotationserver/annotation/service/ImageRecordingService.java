package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.model.ImageRecordings;
import java.util.List;

public interface ImageRecordingService {
    public List<ImageRecordings> getAll();
    public ImageRecordings findRowByImageId(String imageId);
    public ImageRecordings insert(ImageRecordings imagePoint);
    public void delete(ImageRecordings imagePoint);

}
