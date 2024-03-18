package com.gmail.kolyvas.hotelapp.dto.user;

import lombok.Data;

@Data
public class UserShowDTO {
    private Long id;
    private String username;
    private Boolean isEmployee = false;
    private Long customerId;
}
