package de.wigeogis.wigeosocial.annotationserver.annotation.service;


import de.wigeogis.wigeosocial.annotationserver.annotation.model.Annotation;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.ImagePoint;

import java.util.List;

public interface ImagePointService {
    public List<ImagePoint> getAll();
    public ImagePoint findRowByImageId(String imageId);
    public ImagePoint insert(ImagePoint imagePoint);
    public void delete(ImagePoint imagePoint);

}
