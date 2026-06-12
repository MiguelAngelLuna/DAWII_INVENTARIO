package com.example.inventario.controller;

import com.example.inventario.model.Usuario;
import com.example.inventario.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * DTO simple para recibir el JSON del login
     */
    public static class LoginRequest {
        public String username;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Buscar usuario por username
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(loginRequest.username);

            // 2. Validar si existe y si la contraseña coincide (usando BCrypt)
            if (usuarioOpt.isPresent() && 
                passwordEncoder.matches(loginRequest.password, usuarioOpt.get().getPassword())) {
                
                Usuario usuario = usuarioOpt.get();
                
                // 3. Retornar respuesta de éxito con el rol
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Bienvenido " + usuario.getNombre());
                response.put("role", usuario.getRol());
                response.put("id", usuario.getId());
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Credenciales inválidas");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }
}