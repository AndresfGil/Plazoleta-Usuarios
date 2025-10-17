package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.Usuario;
import com.pragma.powerup.infrastructure.out.jpa.entity.RolEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUsuarioEntityMapper {
    
    /**
     * Convierte Usuario del dominio a UsuarioEntity de persistencia
     */
    @Mapping(target = "rol", expression = "java(createRolEntity(usuario.getIdRol()))")
    UsuarioEntity toEntity(Usuario usuario);
    
    /**`
     * Convierte UsuarioEntity de persistencia a Usuario del dominio
     */
    @Mapping(target = "idRol", source = "rol.id")
    Usuario toUsuario(UsuarioEntity usuarioEntity);
    
    /**
     * Crea una entidad de Rol solo con el ID
     */
    default RolEntity createRolEntity(Long idRol) {
        if (idRol == null) {
            return null;
        }
        RolEntity rol = new RolEntity();
        rol.setId(idRol);
        return rol;
    }
}

