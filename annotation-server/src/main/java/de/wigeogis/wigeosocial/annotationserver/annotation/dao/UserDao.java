package de.wigeogis.wigeosocial.annotationserver.annotation.dao;

import javax.transaction.Transactional;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


@Transactional
public interface UserDao extends CrudRepository<User, Long> {

    @Query(value = "SELECT *  FROM users WHERE username = :#{#username}", nativeQuery = true)
    public List<User> findByUsername(@Param("username") String username);

}


