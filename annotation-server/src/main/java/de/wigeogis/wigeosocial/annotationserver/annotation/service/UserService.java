package de.wigeogis.wigeosocial.annotationserver.annotation.service;


import de.wigeogis.wigeosocial.annotationserver.annotation.model.User;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

/**
 * Created by shahin on 20.11.16.
 */
public interface UserService {

    public User createNewUser(User user);

    public User getUserByUserID(long userID);

    public User getUserByUsername(String username);

    public boolean validatePassword(User user, String password) throws Exception;


}
