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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a2mee.model.access.Role;
import com.a2mee.model.dto.access.RoleDetailDto;
import com.a2mee.model.dto.access.RoleDto;
import com.a2mee.model.dto.access.UsersWithRolesDto;
import com.a2mee.services.access.RolesService;
import com.a2mee.util.API;

@RestController
@RequestMapping(API.roles)
@CrossOrigin

public class RolesController {

	@Autowired
	RolesService rolesService;

//	@PostMapping(API.addRole)
//	public void userRollsAdd(@RequestBody Role roles) {
//		rolesService.addRole(roles);
//
//	}

	@GetMapping(API.getAllRole)
	public ResponseEntity<List<RoleDto>> getAllRoles() {

		List<RoleDto> allroles = rolesService.getAllRoles();
		return new ResponseEntity<List<RoleDto>>(allroles, HttpStatus.OK);

	}
	
	@GetMapping(API.getAllRoleDetails)
	public ResponseEntity<List<RoleDetailDto>> getAllRoleDetails() {

		List<RoleDetailDto> allroles = rolesService.getAllRoleDetails();
		return new ResponseEntity<List<RoleDetailDto>>(allroles, HttpStatus.OK);

	}

	@GetMapping(API.getOneRole)
	public ResponseEntity<RoleDto> getRolesById(@PathVariable int id) {
		RoleDto singlerole = rolesService.getRolesById(id);
		return new ResponseEntity<RoleDto>(singlerole, HttpStatus.OK);

	}

	@DeleteMapping(API.deleteRole)
	public void deleteRoleById(@PathVariable int id) {
		rolesService.deleteRoleById(id);

	}

	@PutMapping(API.assignUser)
	public ResponseEntity<Role> assignUser( @RequestBody Role rolesDetail) {
		Role rolesassigned = rolesService.assignUser(rolesDetail);
		return new ResponseEntity<Role>(rolesassigned, HttpStatus.OK);
	}
	@PutMapping(API.removeUser)
	public ResponseEntity<Role> deleteUser( @RequestBody Role rolesDetail) {
		Role rolesremoved = rolesService.deleteUser(rolesDetail);
		return new ResponseEntity<Role>(rolesremoved, HttpStatus.OK);
	}
	@PutMapping(API.assignPermissions)
	public ResponseEntity<RoleDetailDto> assignPermissions( @RequestBody RoleDetailDto rolesDetail ){
		RoleDetailDto permissions = rolesService.assignPermissions(rolesDetail);
		return new ResponseEntity< RoleDetailDto>(permissions,HttpStatus.OK);
	}

	@GetMapping(API.getUsers)
	public ResponseEntity <UsersWithRolesDto> getUsersforRole(@PathVariable int id){
		UsersWithRolesDto usersforrole = rolesService.getUsersforRole(id);
		return new ResponseEntity<UsersWithRolesDto>(usersforrole,HttpStatus.OK);
	}
	@PutMapping(API.activate)
	public ResponseEntity<Role> setStatus(@RequestBody Role role){
		Role statuschanged = rolesService.setStatus(role);
	return new ResponseEntity<Role>(statuschanged,HttpStatus.ACCEPTED)	;
	}
}
