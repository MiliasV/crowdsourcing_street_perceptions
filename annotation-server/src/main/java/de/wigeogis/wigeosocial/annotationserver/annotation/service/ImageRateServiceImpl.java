package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.dao.AnnotationDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.dao.ImageRateDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.ImageRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("imageRateManager")
public class ImageRateServiceImpl implements ImageRateService{

    @Autowired
    private ImageRateDao imageRateDao;

    @Override
    public ImageRate insert(ImageRate imageRate) {
        return imageRateDao.save(imageRate);
    }
}
