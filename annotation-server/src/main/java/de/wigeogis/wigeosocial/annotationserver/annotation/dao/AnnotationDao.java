package de.wigeogis.wigeosocial.annotationserver.annotation.dao;

import de.wigeogis.wigeosocial.annotationserver.annotation.model.Annotation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;


@Transactional
public interface AnnotationDao extends CrudRepository<Annotation, Integer> {

    Long countAnnotationsByIdIsNotNull();
}
