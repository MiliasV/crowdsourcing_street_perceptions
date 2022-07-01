package de.wigeogis.wigeosocial.annotationserver.annotation.dao;

import de.wigeogis.wigeosocial.annotationserver.annotation.model.ImageRecordings;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;


@Transactional
public interface ImageRecordingDao extends CrudRepository<ImageRecordings, Long> {

}
