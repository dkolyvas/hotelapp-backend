package com.gmail.kolyvas.hotelapp.dto.pricingpolicy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PricingPolicySearchDTO {
    private Long customerCategoryId;
    private long roomCategoryId;

}
