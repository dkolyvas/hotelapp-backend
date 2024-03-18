package com.gmail.kolyvas.hotelapp.controllers;

import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicyInsertDTO;
import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicySearchDTO;
import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicyShowDTO;
import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicyUpdateDTO;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.gmail.kolyvas.hotelapp.service.PricingPolicyService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/hotelapp/pricingpolicy")

public class PricingPolicyController {
    private final PricingPolicyService service;

    @Autowired
    public PricingPolicyController(PricingPolicyService service) {
        this.service = service;
    }

    @RequestMapping(value= "", method = RequestMethod.GET)
    public ResponseEntity<List<PricingPolicyShowDTO>> getPolicies(@RequestParam(required = false) Long roomCategoryId,
                                                                  @RequestParam(required = false) Long customerTypeId){
        List<PricingPolicyShowDTO> results;
        try{
            if(roomCategoryId != null && customerTypeId !=null){
                PricingPolicySearchDTO criteria = new PricingPolicySearchDTO(customerTypeId, roomCategoryId);
                results = service.findSpecifiedPricingPolicies(criteria);
            } else if (roomCategoryId != null) {
                results = service.findPoliciesForRoomCategory(roomCategoryId);
            } else if (customerTypeId != null) {
                results = service.findPoliciesForCustomerType(customerTypeId);
            }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return  new ResponseEntity<>(results, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PricingPolicyShowDTO> getPolicyById(@PathVariable("id") long id){
        try{
            PricingPolicyShowDTO result = service.getPolicyById(id);
            return  new ResponseEntity<>(result, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> insertPolicy(@Valid @RequestBody PricingPolicyInsertDTO insertDTO,
                                                BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream().map(m -> m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try{
            PricingPolicyShowDTO result = service.insertPricingPolicy(insertDTO);
            URI path = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/").buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(path).body(result);
        }catch(EntityNotFoundException e){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(UnableToSaveDataException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updatePolicy(@PathVariable("id") long id, @Valid @RequestBody PricingPolicyUpdateDTO updateDTO,
                                               BindingResult bindingResult){
        if(id != updateDTO.getId()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream().map(m -> m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try{
            PricingPolicyShowDTO result = service.updatePricingPolicy(updateDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<PricingPolicyShowDTO> deletePolicy(@PathVariable("id") long id){
        try{
            PricingPolicyShowDTO result = service.getPolicyById(id);
            service.deletePricingPolicy(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
