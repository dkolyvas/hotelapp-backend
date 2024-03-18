package com.gmail.kolyvas.hotelapp.dto.user;

import lombok.Data;

@Data
public class UserTokenDTO {
    private String token;
    private String role;
    private String username;

}
