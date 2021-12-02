package com.a2mee.dao.access;



import java.util.List;

import com.a2mee.model.access.Permission;



public interface PermissionDao {
	
	void addPermission(Permission userPermisson);

	List<Permission> getAllPermisions();

	Permission getPermission(int id);

	void deletePermissionById(int id);

}
