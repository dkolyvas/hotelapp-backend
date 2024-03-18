package com.gmail.kolyvas.hotelapp.dto.pricingpolicy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PricingPolicyInsertDTO {
    @NotNull(message = "You must speicify a price")
    private Float price;
    @NotBlank(message = "You must provide specification for the policy")
    private String specification;
    @NotNull(message = "You must assign the policy to a room category")
    private Long roomCategoryId;

    private Long customerTypeId;
}
