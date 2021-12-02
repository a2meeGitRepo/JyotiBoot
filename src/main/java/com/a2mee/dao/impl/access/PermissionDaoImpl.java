package com.a2mee.dao.impl.access;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.access.PermissionDao;
import com.a2mee.model.access.Permission;

@Transactional
@Repository
public class PermissionDaoImpl implements PermissionDao

{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void addPermission(Permission userPermisson) {
		entityManager.persist(userPermisson);
		entityManager.flush();
		
	}

	@Override
	public List<Permission> getAllPermisions() {
		String hql ="  From Permission";
		return  entityManager.createQuery(hql).getResultList();
	}

	@Override
	public Permission getPermission(int id) {
		return entityManager.find(Permission.class, id);
	}

	@Override
	public  void deletePermissionById(int id) {
		String hql = "delete from Permission where id=:id";
		entityManager.createQuery(hql).setParameter("id", id).executeUpdate();
	}


}
