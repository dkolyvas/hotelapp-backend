package com.gmail.kolyvas.hotelapp.dto.room;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomInsertDTO {

    @NotNull(message = "room code is a required field")
    private String code;
    @NotNull(message = "floor is a required field")
    private Integer floor;
    @NotNull(message = "Category is a required field")
    private Long categoryId;

}
