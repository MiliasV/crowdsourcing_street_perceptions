package de.wigeogis.wigeosocial.annotationserver.annotation.dao;



import de.wigeogis.wigeosocial.annotationserver.annotation.model.Label;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;



@Transactional
public interface LabelDao extends CrudRepository<Label, Integer> {


}
