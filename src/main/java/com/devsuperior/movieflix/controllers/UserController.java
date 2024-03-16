package com.devsuperior.movieflix.controllers;


import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.services.UserService;
import com.devsuperior.movieflix.services.exceptions.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService service;
    private final AuthService authService;


    public UserController(UserService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<UserDTO> findById() {
        UserDTO dto = service.findLoggerUsed();
        return ResponseEntity.ok().body(dto);
    }
}
