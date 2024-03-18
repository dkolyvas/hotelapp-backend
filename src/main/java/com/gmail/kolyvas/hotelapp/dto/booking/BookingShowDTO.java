package com.gmail.kolyvas.hotelapp.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingShowDTO {
    private Long id;
    private Long roomId;
    private String roomCode;
    private Long customerId;
    private String customerName;
    private String customerSurname;
    private Float price;

    private Date dateFrom;
    
    private Date dateTo;

}
