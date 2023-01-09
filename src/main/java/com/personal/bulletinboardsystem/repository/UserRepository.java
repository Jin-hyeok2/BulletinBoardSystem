package com.personal.bulletinboardsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personal.bulletinboardsystem.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

}
