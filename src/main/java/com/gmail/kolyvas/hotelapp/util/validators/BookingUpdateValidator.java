package com.gmail.kolyvas.hotelapp.util.validators;

import com.gmail.kolyvas.hotelapp.dto.booking.BookingUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class BookingUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == BookingUpdateDTO.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookingUpdateDTO dto = (BookingUpdateDTO)target;
        if(!dto.getDateTo().after(dto.getDateFrom())){
            errors.rejectValue("dateTo", "before", "Departure date must be after arrival date");
        }

    }
}
