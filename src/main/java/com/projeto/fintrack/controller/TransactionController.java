package com.projeto.fintrack.controller;

import com.projeto.fintrack.DTO.request.TransactionFilterRequest;
import com.projeto.fintrack.DTO.request.TransactionRequest;
import com.projeto.fintrack.DTO.response.TransactionResponse;
import com.projeto.fintrack.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Gerenciamento de receitas e despesas")
@SecurityRequirement(name = "Bearer Authentication")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar nova transação")
    public TransactionResponse create(@Valid @RequestBody TransactionRequest request) {
        return transactionService.create(request);
    }

    @GetMapping
    @Operation(summary = "Listar transações com filtros e paginação",
            description = "Parâmetros: startDate, endDate, type, categoryId, accountId, page, size")
    public Page<TransactionResponse> listWithFilters(TransactionFilterRequest filter) {
        return transactionService.listWithFilters(filter);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar transação por ID")
    public TransactionResponse findById(@PathVariable Long id) {
        return transactionService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar transação")
    public TransactionResponse update(@PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        return transactionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar transação")
    public void delete(@PathVariable Long id) {
        transactionService.delete(id);
    }
}