package com.pragma.powerup.domain.api;

import com.pragma.powerup.application.dto.request.LoginRequestDto;
import com.pragma.powerup.application.dto.response.LoginResponseDto;

public interface IAuthServicePort {

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
