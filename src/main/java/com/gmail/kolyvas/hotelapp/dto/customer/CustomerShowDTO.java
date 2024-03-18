package com.gmail.kolyvas.hotelapp.dto.customer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerShowDTO {

    private Long id;

    private String givenName;

    private String surname;
    private String address;
    private String country;
    private String phone;
    private String email;
    private String username;
    private String passportNo;
    private String customerTypeName;
}
