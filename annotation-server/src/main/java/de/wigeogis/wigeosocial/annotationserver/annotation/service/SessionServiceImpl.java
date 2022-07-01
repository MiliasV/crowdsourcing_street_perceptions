package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import de.wigeogis.wigeosocial.annotationserver.annotation.dao.SessionDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("sessionManager")
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionDao sessionDao;

    @Override
    public Session findRowBySessionId(String sessionId) {
        return sessionDao.findSessionBySessionId(sessionId);
    }

    @Override
    public Session insert(Session session) {
        return sessionDao.save(session);
    }
}
