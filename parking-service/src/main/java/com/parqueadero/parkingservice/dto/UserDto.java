package com.parqueadero.parkingservice.dto;

import jakarta.validation.constraints.*;
import com.parqueadero.parkingservice.entity.Role;
import com.parqueadero.parkingservice.entity.User;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private Role role;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

}

