package de.wigeogis.wigeosocial.annotationserver.annotation.dao;

import de.wigeogis.wigeosocial.annotationserver.annotation.model.Log;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Path;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.Scene;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;


@Transactional
public interface LogDao extends CrudRepository<Log, Long> {

    @Query(value = "SELECT DISTINCT path_id FROM log" +
            " WHERE session_id IN ( SELECT session_id FROM \"session\"" +
            " WHERE pid = :#{#username} AND complete IS TRUE);", nativeQuery = true)
    List<Integer> getPathCountFromLogs(@Param("username") String username);


    @Query(value = "SELECT i.image_id AS image_id, l.yaw AS direction, l.pitch AS pitch, l.pov AS pov" +
            " FROM log AS l JOIN image_point i ON l.image_id = i.\"id\"" +
            " WHERE l.pitch IS NOT NULL AND l.pov IS NOT NULL" +
            " ORDER BY l.session_id, l.recorded_at;", nativeQuery = true)
    List<Map<String, Object>> getLogsInfo();

    public Log findLogBySessionId(String sessionId);
}
