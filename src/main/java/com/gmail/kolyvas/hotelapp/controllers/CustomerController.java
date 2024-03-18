package com.gmail.kolyvas.hotelapp.controllers;

import com.gmail.kolyvas.hotelapp.dto.customer.*;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import com.gmail.kolyvas.hotelapp.util.validators.CustomerInsertValidator;
import com.gmail.kolyvas.hotelapp.util.validators.CustomerUpdateValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.gmail.kolyvas.hotelapp.service.CustomerService;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/hotelapp/customer")
public class CustomerController {
    private final CustomerService service;
    private final CustomerInsertValidator insertValidator;
    private final CustomerUpdateValidator updateValidator;
    @Autowired
    public CustomerController(CustomerService service, CustomerInsertValidator insertValidator, CustomerUpdateValidator updateValidator) {
        this.service = service;
        this.insertValidator = insertValidator;
        this.updateValidator = updateValidator;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<CustomerShowDTO>> findCustomers(@RequestParam(required = false) String passportNo,
                                                               @RequestParam(required = false) Long type,
                                                               @RequestParam(required = false) String surname,
                                                               @RequestParam(required = false) String givenName,
                                                               @RequestParam(required = false) String email,
                                                               @RequestParam(required = false) String phone,
                                                               @RequestParam(required = false) String country){
        CustomerSearchDTO criteria = new CustomerSearchDTO(passportNo,type,surname,givenName,email,phone,country);
         try {
             List<CustomerShowDTO> results = service.getCustomers(criteria);
             return new ResponseEntity<>(results, HttpStatus.OK);
         }catch (Exception e){
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public ResponseEntity<List<CustomerInformationShowDTO>> getCustomerInformation(@RequestParam() @DateTimeFormat(pattern = "dd-MM-yyyy") Date dateFrom,
                                                                                   @RequestParam() @DateTimeFormat(pattern = "dd-MM-yyyy") Date dateTo){
        if(!dateTo.after(dateFrom)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            List<CustomerInformationShowDTO> results = service.findCustomerSummaryForPeriod(dateFrom, dateTo);
            return new ResponseEntity<>(results, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}")
    public ResponseEntity<CustomerShowDTO> getById(@PathVariable("id")long id){
        try{
            CustomerShowDTO result = service.getCustomerById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/userid/{userid}")
    public ResponseEntity<CustomerShowDTO> getByUserId(@PathVariable("userid")long userId){
        try{
            CustomerShowDTO result = service.getCustomerByUserId(userId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> insertCustomer(@Valid @RequestBody CustomerInsertDTO insertDTO, BindingResult bindingResult){
        insertValidator.validate(insertDTO, bindingResult);
        if(bindingResult.hasErrors()){
            List<String> errors= bindingResult.getAllErrors().stream().map(m -> m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try{
            CustomerShowDTO result = service.insertCustomer(insertDTO);
            URI path = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/").buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(path).body(result);
        }catch (UnableToSaveDataException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateCustomer(@PathVariable("id") long id, @Valid @RequestBody CustomerUpdateDTO updateDTO,
                                                 BindingResult bindingResult){
        updateValidator.validate(updateDTO, bindingResult);
        if(bindingResult.hasErrors()){
            List<String> errors= bindingResult.getAllErrors().stream().map(m -> m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        if(id != updateDTO.getId()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try{
            CustomerShowDTO result = service.updateCustomer(updateDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<CustomerShowDTO> deleteCustomer(@PathVariable("id") long id){
        try{
            CustomerShowDTO result = service.getCustomerById(id);
            service.deleteCustomer(id);
            return  new ResponseEntity<>(result, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
