package com.example.spring_boot_security.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_boot_security.models.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {
    Optional<UserEntity> findByUserName(String userName);
}
