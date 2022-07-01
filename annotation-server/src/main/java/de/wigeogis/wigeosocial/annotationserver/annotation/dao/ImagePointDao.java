package de.wigeogis.wigeosocial.annotationserver.annotation.dao;



import de.wigeogis.wigeosocial.annotationserver.annotation.model.ImagePoint;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Path;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;


@Transactional
public interface ImagePointDao extends CrudRepository<ImagePoint, Long> {

    public ImagePoint findImagePointByImageId(String imageId);
}
