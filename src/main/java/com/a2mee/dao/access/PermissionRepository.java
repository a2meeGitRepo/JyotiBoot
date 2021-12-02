package com.a2mee.dao.access;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a2mee.model.access.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer>
{

}
