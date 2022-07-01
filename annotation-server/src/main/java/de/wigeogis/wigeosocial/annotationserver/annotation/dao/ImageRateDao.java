package de.wigeogis.wigeosocial.annotationserver.annotation.dao;

import de.wigeogis.wigeosocial.annotationserver.annotation.model.ImagePoint;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.ImageRate;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface ImageRateDao extends CrudRepository<ImageRate, Long> {
    
}
