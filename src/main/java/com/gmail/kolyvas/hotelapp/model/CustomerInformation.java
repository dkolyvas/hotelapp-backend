package com.gmail.kolyvas.hotelapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInformation {
    private Customer customer;
    private Double expenditure;
    private Long days;
}
