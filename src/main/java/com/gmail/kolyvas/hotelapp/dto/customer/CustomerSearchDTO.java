package com.gmail.kolyvas.hotelapp.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CustomerSearchDTO {
    private String passportNo;
    private Long type;
    private String surname;
    private String givenName;
    private String email;
    private String phone;
    private String country;
}
