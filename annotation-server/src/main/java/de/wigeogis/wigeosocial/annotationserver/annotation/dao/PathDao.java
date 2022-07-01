package de.wigeogis.wigeosocial.annotationserver.annotation.dao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Path;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;

@Transactional
public interface PathDao extends CrudRepository<Path, Integer> {

    public Path findPathById(Integer id);

}
