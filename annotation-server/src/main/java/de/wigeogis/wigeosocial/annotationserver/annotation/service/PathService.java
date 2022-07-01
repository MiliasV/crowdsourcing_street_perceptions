package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.model.Path;

import java.util.List;

public interface PathService {
    public List<Path> getAll();
    public Path findRow(Integer id);
    public Path insert(Path path);
    public void updateCounter(Integer id);
    public Integer getAvailablePath(List<Integer> excluded);

}
