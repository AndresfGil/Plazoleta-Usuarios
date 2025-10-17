package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.UsuarioRequestDto;
import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.application.handler.IUsuarioHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Endpoints para la gestión de usuarios del sistema")
public class UsuarioRestController {

    private final IUsuarioHandler usuarioHandler;

    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema. El usuario debe ser mayor de 18 años y se creará con estado activo por defecto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Usuario creado",
                                    value = "{\n" +
                                            "  \"id\": 1,\n" +
                                            "  \"nombre\": \"Juan\",\n" +
                                            "  \"apellido\": \"Pérez\",\n" +
                                            "  \"documentoIdentidad\": 12345678,\n" +
                                            "  \"celular\": \"+573001234567\",\n" +
                                            "  \"fechaNacimiento\": \"1995-01-15\",\n" +
                                            "  \"correo\": \"juan.perez@example.com\",\n" +
                                            "  \"idRol\": 4,\n" +
                                            "  \"activo\": true\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o usuario menor de edad",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Campo obligatorio faltante",
                                            value = "{\"message\": \"El documento de identidad es obligatorio\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Usuario menor de edad",
                                            value = "{\"message\": \"El usuario debe ser mayor de 18 años\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Formato de celular inválido",
                                            value = "{\"message\": \"El celular debe tener entre 10 y 13 dígitos\"}"
                                    )
                            }
                    )
            )
    })
            @PostMapping()
            public ResponseEntity<UsuarioResponseDto> guardarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de usuario Cliente",
                                    description = "Roles: 1=ADMINISTRADOR, 2=PROPIETARIO, 3=EMPLEADO, 4=CLIENTE",
                                    value = "{\n" +
                                            "  \"nombre\": \"Juan\",\n" +
                                            "  \"apellido\": \"Pérez\",\n" +
                                            "  \"documentoIdentidad\": 12345678,\n" +
                                            "  \"celular\": \"+573001234567\",\n" +
                                            "  \"fechaNacimiento\": \"1995-01-15\",\n" +
                                            "  \"correo\": \"juan.perez@example.com\",\n" +
                                            "  \"clave\": \"password123\",\n" +
                                            "  \"idRol\": 4\n" +
                                            "}"
                            )
                    )
            )
                    @Valid @RequestBody UsuarioRequestDto usuarioRequestDto) {
                UsuarioResponseDto usuarioResponse = usuarioHandler.guardarUsuario(usuarioRequestDto);
                return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
            }

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Consulta un usuario específico por su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Usuario no encontrado con ID: 1\"}")
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioResponseDto usuarioResponse = usuarioHandler.obtenerUsuarioPorId(id);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }
}
