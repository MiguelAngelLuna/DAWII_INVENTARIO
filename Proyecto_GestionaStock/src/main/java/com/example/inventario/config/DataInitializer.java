package com.example.inventario.config;

import com.example.inventario.model.Usuario;
import com.example.inventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Traemos los usuarios que insertó tu script de MySQL
        List<Usuario> usuarios = usuarioRepository.findAll();

        for (Usuario usuario : usuarios) {
            // 🔐 Si la contraseña NO está encriptada (no empieza con la firma de BCrypt $2a$)
            if (!usuario.getPassword().startsWith("$2a$")) {
                System.out.println("Cifrando contraseña para el usuario: " + usuario.getUsername());
                
                // Spring genera el hash de BCrypt aquí
                String passwordCifrada = passwordEncoder.encode(usuario.getPassword());
                usuario.setPassword(passwordCifrada);
                
                // Guardamos los cambios de vuelta en SQL Server
                usuarioRepository.save(usuario);
            }
        }
        System.out.println("¡Todos los usuarios han sido verificados y cifrados con BCrypt por Spring!");
    }
}