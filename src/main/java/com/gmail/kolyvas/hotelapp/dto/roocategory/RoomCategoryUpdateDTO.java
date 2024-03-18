package com.gmail.kolyvas.hotelapp.dto.roocategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RoomCategoryUpdateDTO {
    @NotNull(message = "You must provide an id")
    private Long id;
    @NotNull(message = "You must specify the number of persons for this room category")
    private Integer personsNumber;
    @NotBlank(message = "You must provide a description")
    @Size(max = 50, message = "The description of the category must not exceed 50 characters")
    private String description;
}
