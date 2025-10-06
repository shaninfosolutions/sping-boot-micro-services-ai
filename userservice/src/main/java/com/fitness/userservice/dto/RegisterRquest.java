package com.fitness.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRquest {

    @NotBlank(message="Email is required")
    @Email(message = "Invalid Email format")
    private String email;

    @NotBlank(message="Password is required")
    @Size(min=6, message="Pasword must be at least 6")
    private String password;
    private String firstName;
    private String lastName;

}

