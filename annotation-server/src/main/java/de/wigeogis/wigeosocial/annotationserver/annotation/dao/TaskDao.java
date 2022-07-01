package de.wigeogis.wigeosocial.annotationserver.annotation.dao;



import de.wigeogis.wigeosocial.annotationserver.annotation.model.Image;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;


@Transactional
public interface TaskDao extends CrudRepository<Image, Integer> {

    public List<Image> findAllByName(String name);
    public List<Image> findAllByCountry(String country);
    public List<Image> findAllByCountryAndDirectionBetween(String country, Double min, Double max);
    public List<Image> findAllByAnnotationsLessThan(Integer count);
}
