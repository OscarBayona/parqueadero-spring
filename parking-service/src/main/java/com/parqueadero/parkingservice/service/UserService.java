package com.parqueadero.parkingservice.service;

import com.parqueadero.parkingservice.dto.UserDto;
import com.parqueadero.parkingservice.entity.Role;
import com.parqueadero.parkingservice.entity.User;
import com.parqueadero.parkingservice.exception.ParkingException;
import com.parqueadero.parkingservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto registerUser(String email, String password) {

        if(userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("El email ya está registrado");
        }
        User user = new User();
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        return new UserDto(savedUser);
    }

    public User buscarPorEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public UserDto crearSocio(String email, String rawPassword) {
        // Validar que no exista un usuario con ese email
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new RuntimeException("El email ya está registrado");
        });

        User socio = new User();
        socio.setEmail(email);
        socio.setPassword(passwordEncoder.encode(rawPassword));
        socio.setRole(Role.SOCIO);

        User saved = userRepository.save(socio);
        return new UserDto(saved); // Retornamos el DTO mapeado
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDto::new)  // Asumiendo que UserDto tiene un constructor que recibe un objeto User
                .collect(Collectors.toList());
    }
}