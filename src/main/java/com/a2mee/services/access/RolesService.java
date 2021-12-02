package com.a2mee.services.access;

import java.util.List;

import com.a2mee.model.dto.access.RoleDetailDto;
import com.a2mee.model.dto.access.RoleDto;
import com.a2mee.model.dto.access.UsersWithRolesDto;
import com.a2mee.model.access.Role;

public interface RolesService {
	

	void addRole(Role roles);

	List<RoleDto> getAllRoles();
	
	List<RoleDetailDto> getAllRoleDetails();

	RoleDto getRolesById(int id);

	void deleteRoleById(int id);

	RoleDetailDto assignPermissions(RoleDetailDto rolesDetail);

	Role assignUser(Role rolesDetail);

	UsersWithRolesDto getUsersforRole(int id);

	Role deleteUser(Role rolesDetail);

	Role setStatus(Role role); 
	
}
