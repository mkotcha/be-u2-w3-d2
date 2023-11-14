package org.emmek.beu2w3d2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserLoginDTO(
        @NotNull(message = "Username cannot be null")
        @NotEmpty(message = "Username cannot be empty")
        @Size(min = 6, max = 30, message = "Username must be between 6 e 30 chars")
        String username,
        @NotNull(message = "Password cannot be null")
        @NotEmpty(message = "Password cannot be empty")
        @Size(min = 6, max = 30, message = "Password must be between 6 e 30 chars")
        String password
) {
}