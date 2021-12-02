package com.a2mee.services.impl.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.access.UserDetailsDao;
import com.a2mee.model.dto.access.PermissionTypeDto;
import com.a2mee.model.dto.access.RoleDto;
import com.a2mee.model.dto.access.UserDetailsDto;
import com.a2mee.model.dto.access.UserPermissionDto;
import com.a2mee.model.dto.access.UserRoleDto;
import com.a2mee.model.access.RolePermission;
import com.a2mee.model.access.UserDetails;
import com.a2mee.services.access.UserDetailService;


@Service
public class UserDetailServiceImpl implements UserDetailService

{
	@Autowired
	UserDetailsDao userDetailsDao;
	

	@Override
	public UserDetails addUser(UserDetails user) {
		return userDetailsDao.addUser(user);
		
	}



	@Override
	public List<UserRoleDto> getUsers() {
		
	List<UserRoleDto> returnlist = new ArrayList <UserRoleDto>();
	List<UserDetails> existingusers = userDetailsDao.getUsers();
	
	existingusers.forEach(user ->{
		UserRoleDto dto = new UserRoleDto();
	dto.setFirstName(user.getFirstName());
	dto.setContactNo(user.getContactNo());
	dto.setLastName(user.getLastName());
	dto.setEmailId(user.getEmailId());
	dto.setGender(user.getGender());
	dto.setId(user.getId());
	dto.setPassword(user.getPassword());
	
	
	returnlist.add(dto);
	
	
	});
	return returnlist;
	}



	@Override
	public UserDetailsDto getUserById(String id) {
		UserDetails user = userDetailsDao.getUserById(id);
		UserDetailsDto dto = new UserDetailsDto();
		dto.setFirstName(user.getFirstName());
		dto.setContactNo(user.getContactNo());
		dto.setLastName(user.getLastName());
		dto.setEmailId(user.getEmailId());
		dto.setGender(user.getGender());
		dto.setId(user.getId());
//		dto.setPassword(user.getPassword());
		return dto;
	}



	@Override
	public void deleteUserById(String id) {
		userDetailsDao.deleteUser(id);
		
	}



	@Override
	public UserDetails assignRoles(UserDetails userDetail) {
		return userDetailsDao.addRoles(userDetail);
	}



	@Override
	public UserPermissionDto getUserDetail(String id) {
		 HashMap<String, RolePermission> hmap = new HashMap<String, RolePermission>();
		 UserPermissionDto returnbody = new UserPermissionDto();
		 List <PermissionTypeDto> list = new ArrayList<PermissionTypeDto>();

//		List<PermissionTypeDto> permissionlist = new ArrayList<PermissionTypeDto>();
		UserDetails user =userDetailsDao.getUserById(id);
		if(user !=null) {
		user.getRoles().forEach(role -> {
			if(role.isActivate()) {
				role.getRolePermission().forEach(permission ->{
					
					String perm = permission.getPk().getPermission().getName();
					String type = permission.getType();
					
					if(hmap.containsKey(perm)) {
						RolePermission existingType = hmap.get(perm);
						if (existingType.getType() != "E") {
							 hmap.put(perm,  permission);		
						}
					} else {
						 hmap.put(perm,  permission);
					}
				});
		}
		});
		hmap.forEach((k,v) ->  {
			PermissionTypeDto obj = new PermissionTypeDto(k, v.getType(), v.getPermission().getPvalue());
//			obj.setPermission(k);
//			obj.setType(v);
			list.add(obj);
		
	});
		
		returnbody.setPermissions(list);
		returnbody.setName(user.getFirstName());
		returnbody.setId(user.getId());
		return returnbody;
		}
		
			return null;
			
	
	
	}
		
		
	



	@Override
	public UserRoleDto getUserDetailById(String id) {
		UserRoleDto dto = new UserRoleDto();
		UserDetails user =  userDetailsDao.getUserDetailById(id);
		dto.setContactNo(user.getContactNo());
		dto.setEmailId(user.getEmailId());
		dto.setFirstName(user.getFirstName());
		dto.setGender(user.getGender());
		dto.setLastName(user.getLastName());
		
		user.getRoles().forEach(role -> {
			if(role.isActivate()) 
			{
			RoleDto roledto = new RoleDto();
			roledto.setId(role.getRoleId());
			roledto.setRoleName(role.getName());
			dto.getRoles().add(roledto);
			
			}
		});
		
		return dto ;
	}



	@Override
	public UserDetails checkUser(String id) {
	return userDetailsDao.checkUser(id);
		
	}



	@Override
	public void updateUser(UserDetails user) {
		UserDetails existinguser = userDetailsDao.getUserById(user.getId());
			existinguser.setContactNo(user.getContactNo());
			existinguser.setEmailId(user.getEmailId());
			existinguser.setFirstName(user.getFirstName());
			existinguser.setGender(user.getGender());
			existinguser.setLastName(user.getLastName());
			existinguser.setPassword(user.getPassword());
		
		userDetailsDao.updateUser(existinguser);
		
		
	}



	@Override
	public UserDetails deleteRoles(UserDetails userDetail) {
		return userDetailsDao.deleteRoles(userDetail);
	}



//	@Override
//	public List<PermissionsOfUserDto> getUserDetailMobile(String id) {
//		 HashMap<String, String> hmap = new HashMap<String, String>();
//		 UserPermissionDto returnbody = new UserPermissionDto();
//		 List <PermissionsOfUserDto> list = new ArrayList<PermissionsOfUserDto>();
//
////		List<PermissionTypeDto> permissionlist = new ArrayList<PermissionTypeDto>();
//		UserDetails user =userDetailsDao.getUserById(id);
//		user.getRoles().forEach(role -> {
//			role.getRolePermission().forEach(permission ->{
//				
//				String perm = permission.getPk().getPermission().getName();
//				String type = permission.getType();
//				
//				if(hmap.containsKey(perm)) {
//					String existingType = hmap.get(perm);
//					if (existingType != "E") {
//						 hmap.put(perm,  type);		
//					}
//				} else {
//					 hmap.put(perm,  type);
//				}
//			});
//		});
//		hmap.forEach((k,v) ->  {
//			PermissionsOfUserDto obj = new PermissionsOfUserDto(k, v);
////			obj.setPermission(k);
////			obj.setType(v);
//			list.add(obj);
//		
//	});
//		
//		returnbody.setPermissions(list);
//		returnbody.setName(user.getFirstName());
//		returnbody.setId(user.getId());
//		
//	
//		
//		return list;
//		
//	}

}
