package de.wigeogis.wigeosocial.annotationserver.annotation.service;


import de.wigeogis.wigeosocial.annotationserver.annotation.model.Annotation;

import java.util.List;

public interface AnnotationService {
    public List<Annotation> getAll();
    public Annotation findRow(Integer id);
    public Annotation insert(Annotation annotation);
    public Long countAnnotations();

}
