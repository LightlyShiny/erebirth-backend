package com.lightlyshiny.backend.controller;

import com.lightlyshiny.backend.dto.*;
import com.lightlyshiny.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/activate")
    public ResponseEntity<Void> activate(@RequestParam String uuid) {
        authenticationService.activate(uuid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO request) {
        authenticationService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recover")
    public ResponseEntity<Void> recover(@Valid @RequestBody RecoverRequestDTO request) {
        authenticationService.recover(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset(@Valid @RequestBody ResetRequestDTO request) {
        authenticationService.reset(request);
        return ResponseEntity.ok().build();
    }
}