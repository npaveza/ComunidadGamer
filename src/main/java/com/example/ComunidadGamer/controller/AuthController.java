package com.example.ComunidadGamer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ComunidadGamer.model.JwtResponse;
import com.example.ComunidadGamer.model.LoginRequest;
import com.example.ComunidadGamer.model.RegistroRequest;
import com.example.ComunidadGamer.model.Usuario;
import com.example.ComunidadGamer.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = "GENERATED_JWT_TOKEN"; // Aquí se generaría el token real con una utilidad JWT
        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", loginRequest.getEmail(), loginRequest.getEmail(), null));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registerUser(@RequestBody RegistroRequest registroRequest) {
        Usuario usuario = new Usuario();
        usuario.setUsername(registroRequest.getUsername());
        usuario.setEmail(registroRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
        usuarioService.save(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}