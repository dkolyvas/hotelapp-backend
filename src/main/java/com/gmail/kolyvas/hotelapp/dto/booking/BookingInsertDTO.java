package com.gmail.kolyvas.hotelapp.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
@Data
public class BookingInsertDTO {
    @NotNull(message = "You must specify a room")
    private Long roomId;
    @NotNull(message = "You must specify a customer")
    private Long customerId;
    @NotNull(message = "You must specify a period")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateFrom;
    @NotNull(message = "You must specify a period")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateTo;
    @NotNull(message = "You must specify a pricing policy")
    private Long pricingPolicyId;

}
