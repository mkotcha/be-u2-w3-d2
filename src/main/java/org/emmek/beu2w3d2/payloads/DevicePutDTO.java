package org.emmek.beu2w3d2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record DevicePutDTO(@NotEmpty(message = "Username cannot be empty")
                           @Size(min = 3, max = 30, message = "Username must be between 3 e 30 chars")
                           String category,
                           @NotEmpty(message = "Name cannot be empty")
                           @Size(min = 3, max = 30, message = "Name must be between 3 e 30 chars")
                           String state
) {
}
