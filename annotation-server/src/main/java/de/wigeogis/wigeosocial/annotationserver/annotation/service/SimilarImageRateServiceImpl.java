package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.dao.ImageRateDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.dao.SimilarImageRateDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.ImageRate;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.SimilarImageRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("similarImageRateManager")
public class SimilarImageRateServiceImpl implements SimilarImageRateService{

    @Autowired
    private SimilarImageRateDao similarImageRateDao;

    @Override
    public SimilarImageRate insert(SimilarImageRate imageRate) {
        return similarImageRateDao.save(imageRate);
    }
}
