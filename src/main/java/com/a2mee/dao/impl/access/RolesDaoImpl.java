package com.a2mee.dao.impl.access;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.access.RolesDao;
import com.a2mee.model.access.UserDetails;
import com.a2mee.model.access.Permission;
import com.a2mee.model.access.Role;

@Transactional
@Repository
public class RolesDaoImpl implements RolesDao
{

	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	public void save(Role roles) {
		entityManager.persist(roles);
		entityManager.flush();		
		
	}


	@Override
	public List<Role> getAllRoles() {
		String hql ="  From Role";
		return entityManager.createQuery(hql).getResultList();
	}


	@Override
	public Role getRolesById(int id) {
		return entityManager.find(Role.class, id);
	}


	@Override
	public void deleteRoleById(int id) {
		String hql = "delete from Role where id=:id";
		entityManager.createQuery(hql).setParameter("id", id).executeUpdate();
		
	}


	@Override
	@Transactional
	public Role assignPermissions(Role rolesDetail) {
		int id = rolesDetail.getRoleId();
		Role existingRole = entityManager.find(Role.class, id);
		if(existingRole == null) {
			existingRole = rolesDetail;
			existingRole.setRoleId(null);
		} else {
			existingRole.setName(rolesDetail.getName());
			existingRole.getRolePermission().forEach(rolePermission -> {
				entityManager.remove(rolePermission);
			});
			existingRole.setRolePermission(rolesDetail.getRolePermission());
		}
		entityManager.persist(existingRole);
		existingRole.getRolePermission().forEach(rolePermission -> {
			entityManager.persist(rolePermission);
		});
		
		entityManager.flush();
		
		
		return  existingRole;
		
	}


	@Override
	public Role assignUser(Role rolesDetail) {
		int roleid = rolesDetail.getRoleId();
		Role existingRole = entityManager.find(Role.class, roleid);
		rolesDetail.getUser().forEach(user ->{
			existingRole.getUser().add(user);
		
		});
		entityManager.persist(existingRole);
		entityManager.flush();
		return  existingRole;
	}


	@Override
	public Role getUsersforRole(int id) {
		return entityManager.find(Role.class, id);
	}


	@Override
	public Role deleteUser(Role rolesDetail) {
		int id = rolesDetail.getRoleId();
		Role existingRole = entityManager.find(Role.class, id);
		for (UserDetails usertoremove : rolesDetail.getUser()) {
			for (Iterator iterator = existingRole.getUser().iterator(); iterator.hasNext();) {
				UserDetails user = (UserDetails) iterator.next();
				if (usertoremove.getId().equals(user.getId()))
					iterator.remove();
			}
		}
		entityManager.persist(existingRole);
		entityManager.flush();
		return existingRole;
	}


}
