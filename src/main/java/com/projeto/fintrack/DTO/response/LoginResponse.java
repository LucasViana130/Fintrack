package com.projeto.fintrack.DTO.response;

public record LoginResponse(
        String token,
        String type,
        UserResponse user
) {
    public LoginResponse(String token, UserResponse user) {
        this(token, "Bearer", user);
    }
}