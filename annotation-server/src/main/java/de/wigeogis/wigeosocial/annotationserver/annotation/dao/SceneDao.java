package de.wigeogis.wigeosocial.annotationserver.annotation.dao;

import de.wigeogis.wigeosocial.annotationserver.annotation.model.Scene;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;


@Transactional
public interface SceneDao extends CrudRepository<Scene, Integer> {

    @Query(value = "SELECT *  FROM scene WHERE active is true ORDER BY random() LIMIT 1", nativeQuery = true)
    List<Scene> findSingleSceneRandomly();

    Long countScenesByIdIsNotNull();

    Long countScenesByActiveFalse();
}
