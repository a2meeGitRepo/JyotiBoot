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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.a2mee.model.access.UserDetails;
import com.a2mee.model.dto.access.UserPermissionDto;
import com.a2mee.model.dto.access.UserRoleDto;
import com.a2mee.services.access.UserDetailService;
import com.a2mee.util.API;

@RestController
@RequestMapping(API.user)
@CrossOrigin
public class UserController {

	
	@Autowired
	UserDetailService userDetailService;
	
	@PostMapping(API.userpost)
	public @ResponseBody  ResponseEntity <UserDetails> addUser(@RequestBody UserDetails user) {
		userDetailService.addUser(user);
		return new ResponseEntity <UserDetails> (user,HttpStatus.CREATED);
		

	}
	@GetMapping(API.userCheck)
	public ResponseEntity <UserDetails> checkUser(@PathVariable String id)
	{
	UserDetails user=userDetailService.checkUser(id);
	if (user == null) {
		return new ResponseEntity<UserDetails>  (HttpStatus.OK);
	}
	return new ResponseEntity<UserDetails>  (HttpStatus.CONFLICT);
	
}
	@GetMapping(API.userGetAll)
	public ResponseEntity<List<UserRoleDto>> getUsers() {
		
		List<UserRoleDto> allUsers =	userDetailService.getUsers();
		return new ResponseEntity<List<UserRoleDto>>(allUsers, HttpStatus.OK);
	}
//	@GetMapping(API.userGetOne)
//	public ResponseEntity<UserDetailsDto> getUserById(@PathVariable String id){
//		UserDetailsDto singleuser = userDetailService.getUserById(id);
//		return new ResponseEntity< UserDetailsDto>(singleuser,HttpStatus.OK);
//		
//	}
	@GetMapping(API.getUserPermission)
	public ResponseEntity<UserPermissionDto> getUserPermissions(@PathVariable String id){
		UserPermissionDto useraccess = userDetailService.getUserDetail(id);
		if(useraccess != null) {
			return new ResponseEntity< UserPermissionDto>(useraccess,HttpStatus.OK);
			
		}
		return null;
		
		
	}
//	@GetMapping(API.getUserPermissionMobile)
//	public ResponseEntity<List<PermissionsOfUserDto>> getUserPermissionsMobile(@PathVariable String id){
//		List <PermissionsOfUserDto> useraccess = userDetailService.getUserDetailMobile(id);
//		return new ResponseEntity<List< PermissionsOfUserDto>>(useraccess,HttpStatus.OK);
//		
		
//	}
	
	@DeleteMapping(API.deleteUser)
	public   ResponseEntity deleteUserById(@PathVariable String id){
		userDetailService.deleteUserById(id);
		return new ResponseEntity (HttpStatus.OK);
	}
	
	@PutMapping(API.assignRoles)
	public ResponseEntity<UserDetails> assignRoles( @RequestBody UserDetails userDetail ){
		UserDetails rolesassigned = userDetailService.assignRoles(userDetail);
		return new ResponseEntity< UserDetails>(rolesassigned,HttpStatus.OK);
	}
	@PutMapping(API.deleteRoles)
	public ResponseEntity<UserDetails> deleteRoles( @RequestBody UserDetails userDetail ){
		UserDetails roledeleted = userDetailService.deleteRoles(userDetail);
		return new ResponseEntity< UserDetails>(roledeleted,HttpStatus.OK);
	}
	@GetMapping(API.getUserRoles)
		public ResponseEntity <UserRoleDto> getUserRoles (@PathVariable String id){
		UserRoleDto userRole = userDetailService.getUserDetailById(id);
		return new ResponseEntity< UserRoleDto>(userRole,HttpStatus.OK);
	}
	@PutMapping(API.editUser)
	public ResponseEntity editUser(@RequestBody UserDetails user){
		userDetailService.updateUser(user);
		return new ResponseEntity (HttpStatus.OK);
		
	}
	

	}
	
	

