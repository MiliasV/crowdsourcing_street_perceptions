package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.dao.ImageRecordingDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.ImageRecordings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("imageRecordingManager")
public class ImageRecordingServiceImpl implements ImageRecordingService {

    @Autowired
    private ImageRecordingDao imageRecordingDao;

    @Override
    public List<ImageRecordings> getAll() {
        List<ImageRecordings> imagePointList = StreamSupport
                .stream(imageRecordingDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return imagePointList;
    }


    @Override
    public ImageRecordings findRowByImageId(String imageId) {
        return null;
    }

    @Override
    public ImageRecordings insert(ImageRecordings imagePoint) {
        return imageRecordingDao.save(imagePoint);
    }

    @Override
    public void delete(ImageRecordings imagePoint) {

    }
}
