package com.example.login.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
/**
 * UserDTO är ett dataöverföringsobjekt som används för att validera användarens inmatning vid registrering.
 */
@Setter
@Getter
public class UserDTO implements Serializable {

    @NotBlank(message = "Email is required")
  @Email(message = "Please provide a valid email")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;

}
