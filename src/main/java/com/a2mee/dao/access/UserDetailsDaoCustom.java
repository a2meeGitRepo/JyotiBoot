package com.a2mee.dao.access;

import java.util.List;

import com.a2mee.model.access.UserDetails;

public interface UserDetailsDaoCustom {
	UserDetails addUser(UserDetails user);

	List<UserDetails> getUsers();

	UserDetails getUserById(String id);

	void deleteUser(String id);

	UserDetails addRoles(UserDetails rolelist);

	UserDetails getUserDetailById(String id);

	UserDetails checkUser(String id);

	void updateUser(UserDetails existinguser);

	UserDetails deleteRoles(UserDetails userDetail);

}
