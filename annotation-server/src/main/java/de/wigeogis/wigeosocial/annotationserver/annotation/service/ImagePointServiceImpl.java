package de.wigeogis.wigeosocial.annotationserver.annotation.service;


import de.wigeogis.wigeosocial.annotationserver.annotation.dao.ImagePointDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.ImagePoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("imagePointManager")
public class ImagePointServiceImpl implements ImagePointService {

    @Autowired
    private ImagePointDao imagePointDao;

    @Override
    public List<ImagePoint> getAll() {
        List<ImagePoint> imagePointList = StreamSupport
                .stream(imagePointDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return imagePointList;
    }


    @Override
    public ImagePoint findRowByImageId(String imageId) {
        return imagePointDao.findImagePointByImageId(imageId);
    }


    @Override
    public ImagePoint insert(ImagePoint imagePoint) {
        return imagePointDao.save(imagePoint);
    }


    @Override
    public void delete(ImagePoint imagePoint) {
        imagePointDao.delete(imagePoint);
    }
}
