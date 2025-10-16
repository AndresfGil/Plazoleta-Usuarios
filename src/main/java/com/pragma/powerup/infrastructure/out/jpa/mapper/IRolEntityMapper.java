package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.Rol;
import com.pragma.powerup.infrastructure.out.jpa.entity.RolEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRolEntityMapper {
    
    /**
     * Convierte Rol del dominio a RolEntity de persistencia
     */
    RolEntity toEntity(Rol rol);
    
    /**
     * Convierte RolEntity de persistencia a Rol del dominio
     */
    Rol toRol(RolEntity rolEntity);
}

