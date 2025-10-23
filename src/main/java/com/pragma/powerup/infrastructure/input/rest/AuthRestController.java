package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.LoginRequestDto;
import com.pragma.powerup.application.dto.request.RegistroClienteRequestDto;
import com.pragma.powerup.application.dto.request.UsuarioRequestDto;
import com.pragma.powerup.application.dto.response.LoginResponseDto;
import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.application.handler.IUsuarioHandler;
import com.pragma.powerup.domain.api.IAuthServicePort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Login", description = "Endpoints para autenticacion de usuarios")

public class AuthRestController {

    private final IAuthServicePort authServicePort;
    private final IUsuarioHandler usuarioHandler;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        try {
            LoginResponseDto response = authServicePort.login(loginRequestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDto> registro(
            @Valid @RequestBody RegistroClienteRequestDto registroClienteRequestDto) {

        UsuarioResponseDto usuarioResponse = usuarioHandler.registrarUsuario(registroClienteRequestDto);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
    }



}
