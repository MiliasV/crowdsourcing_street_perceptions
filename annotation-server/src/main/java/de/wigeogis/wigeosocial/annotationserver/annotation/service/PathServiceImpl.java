package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.dao.AnnotationDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.dao.LabelDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.dao.PathDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Annotation;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Label;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("pathManager")
public class PathServiceImpl implements PathService {

    @Autowired
    private PathDao pathDao;

    @Override
    public List<Path> getAll() {
        List<Path> pathList = StreamSupport
                .stream(pathDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return pathList;
    }

    @Override
    public Path findRow(Integer id) {
        return pathDao.findPathById(id);
    }

    @Override
    public Path insert(Path path) {
        return null;
    }


    @Override
    public void updateCounter(Integer id) {
        Path path = pathDao.findPathById(id);
        path.setCounter(path.getCounter() + 1);
        pathDao.save(path);
    }

    @Override
    public Integer getAvailablePath(List<Integer> excluded) {
        List<Integer> pathList = StreamSupport
                .stream(pathDao.findAll().spliterator(), false)
                .filter(path -> !excluded.contains(path.getId()))
                .sorted(Comparator.comparingInt(Path::getCounter))
                .map(path -> path.getId())
                .collect(Collectors.toList());
        return pathList.get(0);
    }
}
