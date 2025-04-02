package com.parqueadero.parkingservice.controller;

import com.parqueadero.parkingservice.dto.CreateUserRequest;
import com.parqueadero.parkingservice.dto.UserDto;
import com.parqueadero.parkingservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-socio")
    public ResponseEntity<?> createSocio(@RequestBody CreateUserRequest request) {
        UserDto socio = userService.crearSocio(request.email, request.password);
        return ResponseEntity.status(HttpStatus.CREATED).body(socio);
    }

    @PostMapping("/registro")
    public ResponseEntity<UserDto> registerUser(@RequestBody CreateUserRequest request) {
        UserDto createdUser = userService.registerUser(request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
