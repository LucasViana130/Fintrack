package com.projeto.fintrack.service;

import com.projeto.fintrack.dto.request.UpdateUserRequest;
import com.projeto.fintrack.dto.response.UserResponse;
import com.projeto.fintrack.domain.entity.User;
import com.projeto.fintrack.domain.exception.ResourceNotFoundException;
import com.projeto.fintrack.mapper.UserMapper;
import com.projeto.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse getProfile() {
        return userMapper.toResponse(getAuthenticatedUser());
    }

    @Transactional
    public UserResponse update(UpdateUserRequest request) {
        User user = getAuthenticatedUser();

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        return userMapper.toResponse(userRepository.save(user));
    }

    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", 0L));
    }
}