package com.gmail.kolyvas.hotelapp.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInformationShowDTO {
    private Long id;
    private String givenName;
    private String surname;
    private String country;
    private String email;
    private String phoneNo;
    private Float expenditure;
    private Long days;
    private String customerType;
}
