package com.lightlyshiny.backend.service;

import com.lightlyshiny.backend.dto.LoginRequestDTO;
import com.lightlyshiny.backend.dto.LoginResponseDTO;
import com.lightlyshiny.backend.entity.UserEntity;
import com.lightlyshiny.backend.exception.InactiveAccountException;
import com.lightlyshiny.backend.exception.UserNotFoundException;
import com.lightlyshiny.backend.exception.WrongPasswordException;
import com.lightlyshiny.backend.repository.UserRepository;
import com.lightlyshiny.backend.security.JWTUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtilities jwtUtilities;

    public LoginResponseDTO login(LoginRequestDTO request) {
        Optional<UserEntity> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (! user.get().getActive()) {
            throw new InactiveAccountException();
        }
        if (! passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new WrongPasswordException();
        }
        String token = jwtUtilities.generate(user.get().getEmail(), user.get().getRole().getName());
        LoginResponseDTO response = new LoginResponseDTO(token);
        return response;
    }
}