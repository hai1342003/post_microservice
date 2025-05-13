package com.example.user_service.repository;

import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);

    Optional<User> findByEmail(String email);


    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


    List<User> findByRole(Role role);
}

