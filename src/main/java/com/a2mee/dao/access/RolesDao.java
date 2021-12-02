package com.a2mee.dao.access;

import java.util.List;

import com.a2mee.model.access.Role;

public interface RolesDao {

	void save(Role roles);

	List<Role> getAllRoles();

	Role getRolesById(int id);

	void deleteRoleById(int id);

	Role assignPermissions(Role rolesDetail);

	Role assignUser(Role rolesDetail);

	Role getUsersforRole(int id);

	Role deleteUser(Role rolesDetail);
}
