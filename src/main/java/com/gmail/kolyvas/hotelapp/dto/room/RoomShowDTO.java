package com.gmail.kolyvas.hotelapp.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomShowDTO {
    private Long id;
    private String code;
    private Integer floor;
    private String category;



}
