package com.projeto.fintrack.controller;

import com.projeto.fintrack.DTO.request.CategoryRequest;
import com.projeto.fintrack.DTO.response.CategoryResponse;
import com.projeto.fintrack.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Gerenciamento de categorias")
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar categoria personalizada")
    public CategoryResponse create(@Valid @RequestBody CategoryRequest request) {
        return categoryService.create(request);
    }

    @GetMapping
    @Operation(summary = "Listar categorias padrão + personalizadas do usuário")
    public List<CategoryResponse> listAll() {
        return categoryService.listAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID")
    public CategoryResponse findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria personalizada")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativar categoria personalizada")
    public void deactivate(@PathVariable Long id) {
        categoryService.deactivate(id);
    }
}
