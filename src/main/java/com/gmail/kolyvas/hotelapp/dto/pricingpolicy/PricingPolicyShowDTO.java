package com.gmail.kolyvas.hotelapp.dto.pricingpolicy;

import lombok.Data;

@Data
public class PricingPolicyShowDTO {
    private Long id;
    private Float price;
    private String specification;
    private Long roomCategoryId;
    private Long customerTypeId;

}
