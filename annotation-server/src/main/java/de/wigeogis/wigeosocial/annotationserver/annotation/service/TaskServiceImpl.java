package de.wigeogis.wigeosocial.annotationserver.annotation.service;


import de.wigeogis.wigeosocial.annotationserver.annotation.dao.TaskDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("taskManager")
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Override
    public void save(Image image) {
        taskDao.save(image);
    }

    @Override
    public void update(Image image) {
        taskDao.save(image);
    }


    @Override
    public List<Image> getAllByCountry(String country) {
        return taskDao.findAllByCountry(country);
    }

    @Override
    public List<Image> getAllByCountryAndDirection(String country, Double min, Double max) {
        return taskDao.findAllByCountryAndDirectionBetween(country, min, max);
    }

    @Override
    public List<Image> getAllImages() {
        List<Image> labelList = StreamSupport
                .stream(taskDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return labelList;
    }

    @Override
    public Image getImageById(Integer id) {
        return taskDao.findById(id).get();
    }

    @Override
    public Image getImageByName(String name) {
        List<Image> imgList = taskDao.findAllByName(name);
        return imgList.size() > 0 ? imgList.get(0) : null;
    }

    @Override
    public List<Image> getAllImagesByAnnotationCount(Integer count) {
        return taskDao.findAllByAnnotationsLessThan(count);
    }
}
