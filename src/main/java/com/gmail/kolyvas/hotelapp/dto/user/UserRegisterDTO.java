package com.gmail.kolyvas.hotelapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegisterDTO {
   @NotBlank(message = "You must specify a username")
    private String username;
   @NotBlank(message = "You must specify a password")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\W)(^.{8,}$)",
            message = "The password must contain at least one small, one capital letter, a digit a symbol and be at lest 8 characters long")
    private String password;
   private String confirmPassword;

   private boolean isEmployee;


}
