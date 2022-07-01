package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.dao.SceneDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sceneManager")
public class SceneServiceImpl implements SceneService {

    @Autowired
    private SceneDao sceneDao;


    @Override
    public boolean insert(Scene scene) {
        sceneDao.save(scene);
        return true;
    }

    @Override
    public Scene getOneRandomly() {
        List<Scene> scenes = sceneDao.findSingleSceneRandomly();
        return (scenes != null && scenes.size() > 0) ? scenes.get(0) : null;
    }


    @Override
    public Long countCheckedScenes() {
        return sceneDao.countScenesByActiveFalse();
    }

    @Override
    public Long countTotalScenes() {
        return sceneDao.countScenesByIdIsNotNull();
    }
}
