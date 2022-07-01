package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.model.Log;

import java.util.List;
import java.util.Map;


public interface LogService {

    public List<Integer> findPathByUserId(String username);
    public List<Map<String,Object>> findLogsInfo();
    public Log findRowBySessionId(String sessionId);
    public Log insert(Log log);

}
