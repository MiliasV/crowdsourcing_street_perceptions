package de.wigeogis.wigeosocial.annotationserver.annotation.service;




import de.wigeogis.wigeosocial.annotationserver.annotation.model.Image;

import java.util.List;

public interface TaskService {

    public void save(Image image);
    public void update(Image image);
    public List<Image> getAllImages();
    public List<Image> getAllByCountryAndDirection(String country, Double min, Double max);
    public List<Image> getAllByCountry(String country);
    public Image getImageById(Integer id);
    public Image getImageByName(String name);
    public List<Image> getAllImagesByAnnotationCount(Integer count);

}
