package com.a2mee.dao.access;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.a2mee.model.access.UserDetails;

@Repository
public interface UserDetailsDao extends JpaRepository<UserDetails, String>, UserDetailsDaoCustom {

	

}
