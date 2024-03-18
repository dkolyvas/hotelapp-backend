package com.gmail.kolyvas.hotelapp.controllers;

import com.gmail.kolyvas.hotelapp.dto.customertype.CustomerTypeInsertDTO;
import com.gmail.kolyvas.hotelapp.dto.customertype.CustomerTypeShowDTO;
import com.gmail.kolyvas.hotelapp.dto.customertype.CustomerTypeUpdateDTO;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.gmail.kolyvas.hotelapp.service.CustomerTypeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/hotelapp/customertype")
public class CustomerTypeController {

    private final CustomerTypeService service;

    @Autowired
    public CustomerTypeController(CustomerTypeService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<CustomerTypeShowDTO>> getAll(){
        try{
            List<CustomerTypeShowDTO> results = service.getAll();
            return new ResponseEntity<>(results, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CustomerTypeShowDTO> getById(@PathVariable("id") long id){
        try{
            CustomerTypeShowDTO result = service.getById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "" , method = RequestMethod.POST)
    public ResponseEntity<Object> insertCustomerType(@Valid @RequestBody CustomerTypeInsertDTO insertDTO,
                                                                  BindingResult bindingResult){
            if(bindingResult.hasErrors()){
                List<String> errors = bindingResult.getAllErrors().stream().map(m -> m.getDefaultMessage()).toList();
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }
            try{
                CustomerTypeShowDTO result = service.insertType(insertDTO);
                URI path = ServletUriComponentsBuilder.fromCurrentRequest().path("/")
                        .buildAndExpand(result.getId()).toUri();
                return ResponseEntity.created(path).body(result);
            }catch (UnableToSaveDataException e){
                return new ResponseEntity(e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateCustomerType(@PathVariable("id")long id,
                                                     @Valid @RequestBody CustomerTypeUpdateDTO updateDTO,
                                                     BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream().map(m -> m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        if(id != updateDTO.getId()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try{
            CustomerTypeShowDTO result = service.updateType(updateDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<CustomerTypeShowDTO> deleteById(@PathVariable("id") long id){
        try{
            CustomerTypeShowDTO result = service.getById(id);
            service.deleteType(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
