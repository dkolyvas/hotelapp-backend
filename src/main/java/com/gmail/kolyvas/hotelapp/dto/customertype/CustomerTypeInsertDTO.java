package com.gmail.kolyvas.hotelapp.dto.customertype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerTypeInsertDTO {
    @NotBlank(message= "Customer type name is required")
    private String name;
}
