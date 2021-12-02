package com.a2mee.controller.access;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a2mee.model.access.Permission;
import com.a2mee.services.access.PermissionService;
import com.a2mee.util.API;

@RestController
@RequestMapping(API.permission)
@CrossOrigin
public class PermissionController {

	
	@Autowired
	PermissionService permissionService;
	
	@PostMapping(API.addPermission)
	public void addPermission(@RequestBody Permission userPermisson) {
		permissionService.addPermission(userPermisson);

	}
	@GetMapping(API.getAllPermissions)
	public ResponseEntity<List<Permission>> getPermissions() {
		List<Permission> allpermissions =	permissionService.getAllPermsisons();
		return new ResponseEntity<List<Permission>>(allpermissions, HttpStatus.OK);
	}
	@GetMapping(API.getOnePermission)
	public ResponseEntity<Permission> getPermissionsById(@PathVariable int id){
		Permission permission = permissionService.getPermission(id);
		return new ResponseEntity< Permission>(permission,HttpStatus.OK);
		
	}
	
	@DeleteMapping(API.deletePermission)
	public void deletePermissionById(@PathVariable int id){
		permissionService.deletePermissionById(id);
		
	}
	

	}
	
	

