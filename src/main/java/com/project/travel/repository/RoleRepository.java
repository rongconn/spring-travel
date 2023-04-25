package com.project.travel.repository;

import java.util.Optional;

import com.project.travel.enums.ERole;
import com.project.travel.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
