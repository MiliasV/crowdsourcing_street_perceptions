package de.wigeogis.wigeosocial.annotationserver.annotation.dao;

import de.wigeogis.wigeosocial.annotationserver.annotation.model.Session;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;


@Transactional
public interface SessionDao extends CrudRepository<Session, Long> {

    public Session findSessionBySessionId(String sessionId);

}
