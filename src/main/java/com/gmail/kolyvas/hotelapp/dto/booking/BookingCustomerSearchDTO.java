package com.gmail.kolyvas.hotelapp.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
public class BookingCustomerSearchDTO {
    @NotNull(message = "You must specify a customer to search for")
    long customerId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    Date dateFrom;
    @JsonFormat(pattern = "dd/MM/yyyy")
    Date dateTo;
}
