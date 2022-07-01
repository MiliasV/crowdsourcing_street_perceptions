package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.dao.AnnotationDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Annotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("annotationManager")
public class AnnotationServiceImpl implements AnnotationService {

    @Autowired
    private AnnotationDao annotationDao;


    @Override
    public Annotation insert(Annotation annotation) {
        Annotation row = annotationDao.save(annotation);
        return row;
    }


    @Override
    public List<Annotation> getAll() {
        List<Annotation> annotations = StreamSupport
                .stream(annotationDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return annotations;
    }


    @Override
    public Annotation findRow(Integer id) {
        return this.annotationDao.findById(id).get();
    }


    @Override
    public Long countAnnotations() {
        return annotationDao.countAnnotationsByIdIsNotNull();
    }
}
