package de.wigeogis.wigeosocial.annotationserver.security.services;


import de.wigeogis.wigeosocial.annotationserver.annotation.dao.UserDao;
import de.wigeogis.wigeosocial.annotationserver.annotation.model.User;
import de.wigeogis.wigeosocial.annotationserver.annotation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserService userService;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUserByUsername(username);
		if(user == null)
				throw new UsernameNotFoundException("User Not Found with username: " + username);

		return UserDetailsImpl.build(user);
	}

}
