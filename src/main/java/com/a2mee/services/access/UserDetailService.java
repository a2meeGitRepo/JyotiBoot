package com.a2mee.services.access;



import java.util.List;

import com.a2mee.model.dto.access.UserDetailsDto;
import com.a2mee.model.dto.access.UserPermissionDto;
import com.a2mee.model.dto.access.UserRoleDto;
import com.a2mee.model.access.UserDetails;

public interface UserDetailService {

	UserDetails addUser(UserDetails user);

	List<UserRoleDto> getUsers();

	UserDetailsDto getUserById(String id);

	void deleteUserById(String id);

	UserDetails assignRoles(UserDetails userDetail);

	UserPermissionDto getUserDetail(String id);

	UserRoleDto getUserDetailById(String id);

	UserDetails checkUser(String id);

	void updateUser(UserDetails user);

	UserDetails deleteRoles(UserDetails userDetail);

//	List<PermissionsOfUserDto> getUserDetailMobile(String id);
	
}
