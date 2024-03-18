package com.gmail.kolyvas.hotelapp.util.validators;

import com.gmail.kolyvas.hotelapp.dto.customer.CustomerUpdateDTO;
import com.gmail.kolyvas.hotelapp.model.Customer;
import com.gmail.kolyvas.hotelapp.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CustomerUpdateValidator implements Validator {

    private CustomerRepository customerRepository;
@Autowired
    public CustomerUpdateValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == CustomerUpdateValidator.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerUpdateDTO dto = (CustomerUpdateDTO)target;
        Customer customer = customerRepository.findCustomerByEmail(dto.getEmail());
        if(customer != null && customer.getId() != dto.getId()){
            errors.rejectValue("email", "exists", "the specified email corresponds to another customer");
        }
        customer = customerRepository.findCustomerByPassportNo(dto.getPassportNo());
        if(customer != null && customer.getId() != dto.getId()){
            errors.rejectValue("passportNo", "exists", "the specified passport no corresponds to another customer");
        }

    }
}
