package com.castores.app.controller;

import com.castores.app.model.Usuario;
import com.castores.app.repository.UsuarioRepository;
import com.castores.app.security.JwtUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoAndContrasena(
            usuario.getCorreo(), usuario.getContrasena()
        );
    
        if (usuarioOpt.isPresent()) {
            Usuario usuarioDB = usuarioOpt.get();
            String token = jwtUtil.generarToken(usuarioDB.getCorreo());

            Map<String, Object> datosUsuario = new HashMap<>();
           datosUsuario.put("correo", usuarioDB.getCorreo());
            datosUsuario.put("rol", usuarioDB.getIdRol());
    
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("token", token);
            response.put("usuario", datosUsuario);
    
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("success", false, "mensaje", "Credenciales incorrectas"));
        }
    }
    
}
