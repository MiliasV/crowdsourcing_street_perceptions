package de.wigeogis.wigeosocial.annotationserver.annotation.service;


import de.wigeogis.wigeosocial.annotationserver.annotation.model.Session;

public interface SessionService {

    public Session findRowBySessionId(String sessionId);
    public Session insert(Session session);

}
