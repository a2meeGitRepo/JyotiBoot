package com.a2mee.services.access;

import com.a2mee.model.access.UserDetails;

public interface LoginService {


	UserDetails getUser(String id, String password);

	

}
