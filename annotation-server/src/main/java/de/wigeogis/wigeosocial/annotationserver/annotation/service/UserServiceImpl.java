package de.wigeogis.wigeosocial.annotationserver.annotation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.wigeogis.wigeosocial.annotationserver.annotation.dao.UserDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.sql.Timestamp;
import java.util.*;


@Service("userManager")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public User createNewUser(User user){
        User newUser = userDao.save(user);
        return newUser;
    }


    @Override
    public User getUserByUsername(String username) {
        List<User> user = userDao.findByUsername(username);
        return (user.size() > 0) ? user.get(0) : null;
    }

    @Override
    public User getUserByUserID(long userID) {
        Optional<User> user = userDao.findById(userID);
        return user.isPresent()?user.get():null;
    }


    @Override
    public boolean validatePassword(User user, String password) throws Exception {
        return user.getPassword().equals(password);
    }


    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }


    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
