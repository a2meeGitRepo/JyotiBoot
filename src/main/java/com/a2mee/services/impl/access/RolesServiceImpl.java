package com.a2mee.services.impl.access;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.access.RolesDao;
import com.a2mee.dao.access.UserDetailsDao;
import com.a2mee.model.dto.access.PermissionTypeDto;
import com.a2mee.model.dto.access.RoleDetailDto;
import com.a2mee.model.dto.access.RoleDto;
import com.a2mee.model.dto.access.UserDto;
import com.a2mee.model.dto.access.UsersWithRolesDto;
import com.a2mee.model.access.Permission;
import com.a2mee.model.access.Role;
import com.a2mee.model.access.RolePermission;
import com.a2mee.model.access.UserDetails;
import com.a2mee.services.access.RolesService;

@Service
public class RolesServiceImpl implements RolesService 
{
	@Autowired
	RolesDao rolesDao;
	
	@Autowired
	UserDetailsDao userDetailsDao;

	

	@Override
	public void addRole(Role roles) {
		rolesDao.save(roles);
		
	}




	@Override
	public List<RoleDto> getAllRoles() {
		List<RoleDto> returnbody = new ArrayList <RoleDto>();
		
		List<Role> rolelist = rolesDao.getAllRoles();
		rolelist.forEach(role ->{
			RoleDto dto = new RoleDto();
			dto.setId(role.getRoleId());
			dto.setRoleName(role.getName());
			returnbody.add(dto);
		});
		return returnbody;
	}

	@Override
	public List<RoleDetailDto> getAllRoleDetails() {
		List<RoleDetailDto> returnbody = new ArrayList <RoleDetailDto>();
		
		List<Role> rolelist = rolesDao.getAllRoles();
		rolelist.forEach(role ->{
			RoleDetailDto dto = new RoleDetailDto();
			dto.setId(role.getRoleId());
			dto.setRoleName(role.getName());
			dto.setActive(role.isActivate());
			for (RolePermission rolePermission : role.getRolePermission()) {
				PermissionTypeDto permissionTypeDto = new PermissionTypeDto();
				permissionTypeDto.setPermissionId(rolePermission.getPk().getPermission().getPermissionId());
				permissionTypeDto.setPermission(rolePermission.getPk().getPermission().getName());
				permissionTypeDto.setPermissionValue(rolePermission.getPk().getPermission().getPvalue());
				permissionTypeDto.setType(rolePermission.getType());
				dto.getPermission().add(permissionTypeDto);
			}
			
			returnbody.add(dto);
		});
		return returnbody;
	}



	@Override
	public RoleDto getRolesById(int id) {
		RoleDto returnbody = new RoleDto();
		Role role = rolesDao.getRolesById(id);
		returnbody.setId(role.getRoleId());
		returnbody.setRoleName(role.getName());
		return returnbody;
	}




	@Override
	public void deleteRoleById(int id) {
		 rolesDao.deleteRoleById(id);
		
	}




	@Override
	public RoleDetailDto assignPermissions(RoleDetailDto rolesDetail) {
		Role role = new Role();
		role.setName(rolesDetail.getRoleName());
		role.setRoleId(rolesDetail.getId());
		for (PermissionTypeDto permissionTypeDto : rolesDetail.getPermission()) {
			RolePermission rp = new RolePermission();
			Permission permission = new Permission();
			permission.setName(permissionTypeDto.getPermission());
			permission.setPermissionId(permissionTypeDto.getPermissionId());
			rp.setType(permissionTypeDto.getType());
			rp.getPk().setPermission(permission);
			rp.getPk().setRole(role);
			role.getRolePermission().add(rp);
			
		}
		
		rolesDao.assignPermissions(role);
		return rolesDetail;
	}




	@Override
	public Role assignUser(Role rolesDetail) {
		return rolesDao.assignUser(rolesDetail);
	}




	@Override
	public UsersWithRolesDto getUsersforRole(int id) {
		UsersWithRolesDto returnbody = new UsersWithRolesDto();
		List<UserDto> userlist = new ArrayList<UserDto>();
		Role usersforrole = rolesDao.getUsersforRole(id);
		usersforrole.getUser().forEach( user ->{
			UserDto users = new UserDto();
			users.setFirstName(user.getFirstName());
			users.setId(user.getId());
			userlist.add(users);
		});
//		userlist
		
		List<UserDetails> users = userDetailsDao.getUsers();
		List<UserDto> rolelessuser = new ArrayList<UserDto>();
		for (Iterator iterator = users.iterator(); iterator.hasNext();) {
			UserDetails eachuser = (UserDetails) iterator.next();
			boolean found = false;
			for (UserDto userinrole : userlist) {
				
				if(userinrole.getId().equals(eachuser.getId())) {
					found =true ;
					break;
				}
			}
			if(!found) {
				UserDto oneuser = new UserDto();
				oneuser.setFirstName(eachuser.getFirstName());
				oneuser.setId(eachuser.getId());
				rolelessuser.add(oneuser);
			}
			
//			rolelessuser
		}
		
		returnbody.setUserwithoutrole(rolelessuser);
		returnbody.setUserwithrole(userlist);
		
		return returnbody;
		
	}




	@Override
	public Role deleteUser(Role rolesDetail) {
		return rolesDao.deleteUser(rolesDetail);
	}




	@Override
	public Role setStatus(Role role) {
		Role existingrole = rolesDao.getRolesById(role.getRoleId());
		existingrole.setActivate(role.isActivate());
		rolesDao.save(existingrole);
		return existingrole;
	} 

	





}
