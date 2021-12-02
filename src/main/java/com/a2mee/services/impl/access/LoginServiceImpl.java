package com.a2mee.services.impl.access;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.access.UserDetailsDao;
import com.a2mee.model.access.UserDetails;
import com.a2mee.services.access.LoginService;
@Service
public class LoginServiceImpl implements LoginService{

	
	@Autowired
	UserDetailsDao loginDao;
	


	@Override
	public UserDetails getUser(String id, String password) {
		Optional<UserDetails> user = loginDao.findById(id);
		System.out.println("BY ID IS Pewsent   "+user.isPresent());
		return user.isPresent()? (user.get().getPassword().equals(password)? user.get() : null) : null;	
	}
}
