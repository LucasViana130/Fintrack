package com.projeto.fintrack.controller;

import com.projeto.fintrack.DTO.request.UpdateUserRequest;
import com.projeto.fintrack.DTO.response.UserResponse;
import com.projeto.fintrack.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints do perfil do usuário")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Buscar perfil do usuário autenticado")
    public UserResponse getProfile() {
        return userService.getProfile();
    }

    @PutMapping("/me")
    @Operation(summary = "Atualizar dados do usuário autenticado")
    public UserResponse update(@Valid @RequestBody UpdateUserRequest request) {
        return userService.update(request);
    }
}

