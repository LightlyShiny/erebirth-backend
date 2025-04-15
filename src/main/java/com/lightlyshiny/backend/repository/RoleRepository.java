package com.lightlyshiny.backend.repository;

import com.lightlyshiny.backend.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

}