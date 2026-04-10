package com.projeto.fintrack.service;

import com.projeto.fintrack.dto.request.LoginRequest;
import com.projeto.fintrack.dto.request.RegisterRequest;
import com.projeto.fintrack.dto.response.LoginResponse;
import com.projeto.fintrack.dto.response.UserResponse;
import com.projeto.fintrack.domain.entity.User;
import com.projeto.fintrack.domain.exception.BusinessException;
import com.projeto.fintrack.mapper.UserMapper;
import com.projeto.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado.");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return userMapper.toResponse(userRepository.save(user));
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        String token = jwtService.generateToken(user);
        return new LoginResponse(token, userMapper.toResponse(user));
    }
}

