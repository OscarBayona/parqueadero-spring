package com.parqueadero.parkingservice.controller;

import com.parqueadero.parkingservice.config.TokenBlacklist;
import com.parqueadero.parkingservice.dto.CreateUserRequest;
import com.parqueadero.parkingservice.dto.LoginRequest;
import com.parqueadero.parkingservice.dto.UserDto;
import com.parqueadero.parkingservice.entity.User;
import com.parqueadero.parkingservice.service.UserService;
import com.parqueadero.parkingservice.config.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.buscarPorEmail(request.email);
        if (user == null) {
            return ResponseEntity.badRequest().body("{\"mensaje\":\"Email no encontrado\"}");
        }

        return ResponseEntity.ok().body(
                "{ \"token\": \"" + jwtTokenProvider.createToken(user.getEmail(), user.getRole().name()) + "\" }"
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        // Extraemos el token del header
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            if (jwtTokenProvider.validateToken(token)) {
                String jti = jwtTokenProvider.getJtiFromToken(token);
                // Obtenemos la fecha de expiración del token en segundos desde Epoch
                long expEpoch = jwtTokenProvider.getClaimsFromToken(token).getExpiration().getTime() / 1000;
                // Agregamos el token a la blacklist
                TokenBlacklist.addToken(jti, expEpoch);
            }
        }
        return ResponseEntity.ok(Map.of("mensaje", "Logout exitoso. Token invalidado."));
    }
}