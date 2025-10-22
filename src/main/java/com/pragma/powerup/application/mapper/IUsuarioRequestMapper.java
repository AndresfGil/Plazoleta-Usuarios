package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.UsuarioRequestDto;
import com.pragma.powerup.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUsuarioRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Usuario toUsuario(UsuarioRequestDto usuarioRequestDto);
}

