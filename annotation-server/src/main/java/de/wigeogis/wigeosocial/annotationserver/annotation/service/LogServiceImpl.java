package de.wigeogis.wigeosocial.annotationserver.annotation.service;


import de.wigeogis.wigeosocial.annotationserver.annotation.dao.LogDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("logManager")
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logService;

    @Override
    public List<Integer> findPathByUserId(String username) {
        return logService.getPathCountFromLogs(username);
    }

    @Override
    public Log findRowBySessionId(String sessionId) {
        return logService.findLogBySessionId(sessionId);
    }

    @Override
    public Log insert(Log log) {
        return logService.save(log);
    }

    @Override
    public List<Map<String,Object>> findLogsInfo(){
        return logService.getLogsInfo();
    }
}
