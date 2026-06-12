package com.example.inventario.service;

import com.example.inventario.model.Usuario;
import com.example.inventario.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Busca un usuario por username
     */
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
    
    /**
     * Registra un nuevo usuario (cifra la contraseña automáticamente)
     */
    public Usuario registrarUsuario(Usuario usuario) {
        // Cifrar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        // Por defecto, rol USER
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("USER");
        }
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Actualiza un usuario existente
     */
    public Usuario actualizarUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
        if (usuarioExistente.isPresent()) {
            Usuario usuarioActualizado = usuarioExistente.get();
            usuarioActualizado.setNombre(usuario.getNombre());
            usuarioActualizado.setUsername(usuario.getUsername());
            // Solo actualizar password si viene uno nuevo
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                usuarioActualizado.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            return usuarioRepository.save(usuarioActualizado);
        }
        return null;
    }
    
    /**
     * Elimina un usuario por ID
     */
    public boolean eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Lista todos los usuarios
     */
    public Iterable<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
    
    /**
      Método requerido por UserDetailsService (para Spring Security)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol())
                .build();
    }
}