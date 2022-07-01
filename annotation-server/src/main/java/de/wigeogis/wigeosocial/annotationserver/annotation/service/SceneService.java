package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.model.Scene;

import java.util.List;

public interface SceneService {

    public boolean insert(Scene scene);

    public Scene getOneRandomly();

    public Long countCheckedScenes();

    public Long countTotalScenes();

}
