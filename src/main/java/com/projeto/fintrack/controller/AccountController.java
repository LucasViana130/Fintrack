package com.projeto.fintrack.controller;

import com.projeto.fintrack.dto.request.AccountRequest;
import com.projeto.fintrack.dto.response.AccountResponse;
import com.projeto.fintrack.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Contas", description = "Gerenciamento de contas financeiras")
@SecurityRequirement(name = "Bearer Authentication")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova conta")
    public AccountResponse create(@Valid @RequestBody AccountRequest request) {
        return accountService.create(request);
    }

    @GetMapping
    @Operation(summary = "Listar todas as contas ativas")
    public List<AccountResponse> listAll() {
        return accountService.listAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta por ID")
    public AccountResponse findById(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar conta")
    public AccountResponse update(@PathVariable Long id, @Valid @RequestBody AccountRequest request) {
        return accountService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativar conta (soft delete)")
    public void deactivate(@PathVariable Long id) {
        accountService.deactivate(id);
    }
}