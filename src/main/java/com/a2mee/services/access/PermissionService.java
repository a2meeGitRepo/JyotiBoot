package com.a2mee.services.access;


import java.util.List;

import com.a2mee.model.access.Permission;

public interface PermissionService {
	public void addPermission(Permission userPermisson);

	public List<Permission> getAllPermsisons();

	public Permission getPermission(int id);

	public void deletePermissionById(int id);	
}
