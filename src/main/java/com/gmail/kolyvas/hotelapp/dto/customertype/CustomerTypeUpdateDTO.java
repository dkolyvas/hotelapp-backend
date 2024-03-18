package com.gmail.kolyvas.hotelapp.dto.customertype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerTypeUpdateDTO {
    @NotNull(message = "id is required")
    private Long id;
    @NotBlank(message= "Customer type name is required")
    private String name;
}
