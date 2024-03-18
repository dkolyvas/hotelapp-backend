package com.gmail.kolyvas.hotelapp.dto.pricingpolicy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PricingPolicyUpdateDTO {
    @NotNull(message = "Id is rquired")
    private Long id;
    @NotNull(message = "You must speicify a price")
    private Float price;
    @NotBlank(message = "You must provide specification for the policy")
    private String specification;

}
