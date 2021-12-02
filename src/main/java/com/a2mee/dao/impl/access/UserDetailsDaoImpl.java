package com.a2mee.dao.impl.access;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.access.UserDetailsDao;
import com.a2mee.dao.access.UserDetailsDaoCustom;
import com.a2mee.model.access.Role;
import com.a2mee.model.access.UserDetails;



@Transactional
@Repository
public class UserDetailsDaoImpl implements UserDetailsDaoCustom
{
	


	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public UserDetails addUser(UserDetails user) {
		entityManager.persist(user);
		entityManager.flush();
		return user;
		
	}

	@Override
	public List<UserDetails> getUsers() {
		String hql ="From UserDetails";
		return entityManager.createQuery(hql, UserDetails.class).getResultList();
	}

	

	
	@Override
	public UserDetails getUserById(String id) {
		return entityManager.find(UserDetails.class, id);
	}

	@Override
	public void deleteUser(String id) {
//		UserDetails user = entityManager.find(UserDetails.class, id);
		String hql = "delete from UserDetails ud where ud.id= :id";
		entityManager.createQuery(hql).setParameter("id", id).executeUpdate();
		
	
		
	}

	@Override
	public UserDetails addRoles(UserDetails userDetail) {
		String id = userDetail.getId();
		UserDetails existingUser = entityManager.find(UserDetails.class, id);
		Set<Role> roleset = new HashSet<>();
		existingUser.getRoles().forEach(role ->{
			roleset.add(role);
		});
		userDetail.getRoles().forEach(role ->{
			roleset.add(role);
		});
		existingUser.setRoles(roleset);
		entityManager.persist(existingUser);
		entityManager.flush();
		return  existingUser;
	}

	@Override
	public UserDetails getUserDetailById(String id) {
		return entityManager.find(UserDetails.class, id);
	}

	@Override
	public UserDetails checkUser(String id) {
	UserDetails user = entityManager.find(UserDetails.class, id);
	if(user != null) {
		return user;
	}
	
		return null;
	}

	@Override
	public void updateUser(UserDetails existinguser) {
		String hql ="Update UserDetails ud SET  ud.id = :id , ud.firstName = :firstname , ud.lastName = :lastName, ud.emailId =:emailId , ud.gender = :gender , ud.password = :password , ud.contactNo = :contactNo WHERE ud.id=:id";
				entityManager.createQuery(hql)
				.setParameter("firstname", existinguser.getFirstName())
				.setParameter("lastName", existinguser.getLastName())
				.setParameter("emailId", existinguser.getEmailId())
				.setParameter("gender", existinguser.getGender())
				.setParameter("password", existinguser.getPassword())
				.setParameter("contactNo", existinguser.getContactNo())
				.setParameter("id", existinguser.getId())
				.executeUpdate();		
		
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public UserDetails deleteRoles(UserDetails userDetail) {
		String id = userDetail.getId();
		UserDetails existingUser = entityManager.find(UserDetails.class, id);
		for (Role newrole : userDetail.getRoles()) {
			System.out.println(newrole.getRoleId());
			for (Iterator iterator = existingUser.getRoles().iterator(); iterator.hasNext();) {
				Role role = (Role) iterator.next();
				System.out.println(role.getRoleId());
				if(newrole.getRoleId().equals(role.getRoleId())) {
//					System.out.println(iterator.toString());
					iterator.remove();
				}
			}
		}
			
		
		
		entityManager.persist(existingUser);
		entityManager.flush();
		return existingUser;
	}
	
	

}
