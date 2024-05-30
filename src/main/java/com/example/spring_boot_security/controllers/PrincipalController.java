package com.example.spring_boot_security.controllers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot_security.controllers.request.CreateUserDTO;
import com.example.spring_boot_security.models.ERole;
import com.example.spring_boot_security.models.RoleEntity;
import com.example.spring_boot_security.models.UserEntity;
import com.example.spring_boot_security.repositories.UserRepository;

import jakarta.validation.Valid;

@RestController
public class PrincipalController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/helloSecurity")
    public String helloSecurity() {
        return "Hello Security!";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        Set<RoleEntity> roles = createUserDTO.getRoles()
                                            .stream()
                                            .map(role -> RoleEntity.builder()
                                                .name(ERole.valueOf(role))
                                                .build())
                                            .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                                    .userName(createUserDTO.getUserName())
                                    .password(createUserDTO.getPassword())
                                    .email(createUserDTO.getEmail())
                                    .roles(roles)
                                    .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id) {
        userRepository.deleteById(Long.parseLong(id));
        return String.format("User with id %s deleted", id);
    }
}
