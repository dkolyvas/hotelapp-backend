package com.gmail.kolyvas.hotelapp.dto.customer;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInsertDTO {
    @NotBlank(message = "Given Name is a required field")
    @Size(max = 30, message = "Given Name must not exceed 30 characters")
    @Pattern(regexp = "\\p{L}*", message = "Given name must contain only letters")
    private String givenName;
    @NotBlank(message = "Surname is a required field")
    @Size(max = 50, message = "Surname must not exceed 50 characters")
    @Pattern(regexp = "\\p{L}*", message = "Surname must contain only letters")
    private String surname;
    @Size(max = 75, message = "Address must not exceed 75 characters")
    private String address;
    @Size(max = 30, message = "Country name must not exceed 30 characters")
    private String country;
    @NotBlank(message= "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;
    @NotBlank(message = "Email is required")
    @Email(message = "You must submit a valid email address")
    @Size(max = 30, message = "Email must not exceed 30 characters")
    private String email;

    @NotBlank(message = "Passport number is required")
    @Size(max = 30,message = "passport number length cannot exceed 30 characters")
    private String passportNo;

    private Long userId;

    private Long customerTypeId;
}
