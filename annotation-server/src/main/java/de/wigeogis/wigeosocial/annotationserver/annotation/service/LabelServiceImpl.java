package de.wigeogis.wigeosocial.annotationserver.annotation.service;


import de.wigeogis.wigeosocial.annotationserver.annotation.dao.LabelDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("labelManager")
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelDao labelDao;

    @Override
    public List<Label> getAll() {
        List<Label> labelList = StreamSupport
                .stream(labelDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return labelList;
    }
}
