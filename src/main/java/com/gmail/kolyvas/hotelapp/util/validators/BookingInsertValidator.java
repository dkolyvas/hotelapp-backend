package com.gmail.kolyvas.hotelapp.util.validators;



import com.gmail.kolyvas.hotelapp.dto.booking.BookingInsertDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookingInsertValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == BookingInsertDTO.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookingInsertDTO dto = (BookingInsertDTO)target;
        if(!dto.getDateTo().after(dto.getDateFrom())){
            errors.rejectValue("dateTo", "before", "Departure date must be after arrival date");
        }

    }
}
