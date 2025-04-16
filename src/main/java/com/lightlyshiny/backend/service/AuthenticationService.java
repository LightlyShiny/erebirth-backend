package com.lightlyshiny.backend.service;

import com.lightlyshiny.backend.dto.LoginRequestDTO;
import com.lightlyshiny.backend.dto.LoginResponseDTO;
import com.lightlyshiny.backend.dto.RegisterRequestDTO;
import com.lightlyshiny.backend.model.RoleEntity;
import com.lightlyshiny.backend.model.TokenEntity;
import com.lightlyshiny.backend.model.UserEntity;
import com.lightlyshiny.backend.exception.*;
import com.lightlyshiny.backend.repository.RoleRepository;
import com.lightlyshiny.backend.repository.TokenRepository;
import com.lightlyshiny.backend.repository.UserRepository;
import com.lightlyshiny.backend.security.JWTUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtilities jwtUtilities;
    private final MailService mailService;

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

    public void register(RegisterRequestDTO request) {
        Optional<UserEntity> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new UserFoundException();
        }
        Optional<RoleEntity> role = roleRepository.findByName(request.getRole().toString());
        if (role.isEmpty()) {
            throw new RoleNotFoundException();
        }
        UserEntity newUser = new UserEntity(null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                false,
                role.get());
        newUser = userRepository.save(newUser);
        TokenEntity newToken = new TokenEntity(null, UUID.randomUUID().toString(), newUser);
        newToken = tokenRepository.save(newToken);
        mailService.sendActivationLink(newUser, newToken);
    }

    public void activate(String uuid) {
        Optional<TokenEntity> token = tokenRepository.findByToken(uuid);
        if (token.isEmpty()) {
            throw new TokenNotFoundException();
        }
        UserEntity user = token.get().getUser();
        user.setActive(true);
        userRepository.save(user);
        tokenRepository.delete(token.get());
    }
}