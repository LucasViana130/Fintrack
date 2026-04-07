package com.projeto.fintrack.service;

import com.projeto.fintrack.DTO.request.CategoryRequest;
import com.projeto.fintrack.DTO.response.CategoryResponse;
import com.projeto.fintrack.domain.entity.Category;
import com.projeto.fintrack.domain.entity.User;
import com.projeto.fintrack.domain.exception.BusinessException;
import com.projeto.fintrack.domain.exception.ResourceNotFoundException;
import com.projeto.fintrack.mapper.CategoryMapper;
import com.projeto.fintrack.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserService userService;

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        User user = userService.getAuthenticatedUser();

        if (categoryRepository.existsByNameAndUserId(request.name(), user.getId())) {
            throw new BusinessException("Você já possui uma categoria com este nome.");
        }

        Category category = Category.builder()
                .user(user)
                .name(request.name())
                .type(request.type())
                .color(request.color())
                .active(true)
                .build();

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> listAll() {
        User user = userService.getAuthenticatedUser();
        return categoryRepository.findAllAvailableForUser(user.getId())
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        User user = userService.getAuthenticatedUser();
        Category category = categoryRepository.findAvailableByIdForUser(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return categoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        User user = userService.getAuthenticatedUser();

        Category category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        boolean nameChanged = !category.getName().equalsIgnoreCase(request.name());
        if (nameChanged && categoryRepository.existsByNameAndUserId(request.name(), user.getId())) {
            throw new BusinessException("Você já possui uma categoria com este nome.");
        }

        category.setName(request.name());
        category.setColor(request.color());

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deactivate(Long id) {
        User user = userService.getAuthenticatedUser();

        Category category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        category.setActive(false);
        categoryRepository.save(category);
    }
}

