package com.a2mee.services.impl.access;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.access.PermissionDao;
import com.a2mee.model.access.Permission;
import com.a2mee.services.access.PermissionService;


@Service
public class PermissionServiceImpl implements PermissionService

{
	@Autowired
	PermissionDao permissiondaoDao;
	@Override
	public void addPermission(Permission userPermisson) {
		permissiondaoDao.addPermission(userPermisson);
	}

	

	@Override
	public List<Permission> getAllPermsisons() {
		return permissiondaoDao.getAllPermisions();
	}



	@Override
	public Permission getPermission(int id) {
		return permissiondaoDao.getPermission(id);
	}



	@Override
	public void deletePermissionById(int id) {
		 permissiondaoDao.deletePermissionById(id);
		
	}

}
