package com.gmail.kolyvas.hotelapp.dto.room;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RoomSearchDTO {
    private Integer personsNo;

    //@NotNull(message = "You must specify a period")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateFrom;

    //@NotNull(message= "You must specify a period")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateTo;

    private Long categoryId;

}
