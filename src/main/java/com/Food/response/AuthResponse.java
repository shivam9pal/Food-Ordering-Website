package com.Food.response;

import com.Food.models.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class AuthResponse {
    private String jwt;
    private String message;

    private USER_ROLE role;
}
