package com.gmail.kolyvas.hotelapp.dto.room;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdateDTO {
    @NotNull(message = "id is required")
    private Long id;
    @NotNull(message = "room code is a required field")
    private String code;
    @NotNull(message = "floor is a required field")
    private Integer floor;
    @NotNull(message = "Category is a required field")
    private Long categoryId;
}
