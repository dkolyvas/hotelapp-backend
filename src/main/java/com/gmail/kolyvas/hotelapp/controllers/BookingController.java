package com.gmail.kolyvas.hotelapp.controllers;

import com.gmail.kolyvas.hotelapp.dto.booking.*;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.RoomAlreadyBookedException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.gmail.kolyvas.hotelapp.service.BookingService;
import com.gmail.kolyvas.hotelapp.util.validators.BookingInsertValidator;
import com.gmail.kolyvas.hotelapp.util.validators.BookingUpdateValidator;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/hotelapp/booking")
public class BookingController {
    private final BookingService service;
    private final BookingUpdateValidator updateValidator;
    private final BookingInsertValidator insertValidator;

    @Autowired
    public BookingController(BookingService service, BookingUpdateValidator updateValidator, BookingInsertValidator insertValidator) {
        this.service = service;
        this.updateValidator = updateValidator;
        this.insertValidator = insertValidator;
    }

    @RequestMapping(value = "/room/{roomId}", method = RequestMethod.GET)
    public ResponseEntity<List<BookingShowDTO>> getRoomBookings(@PathVariable("roomId") long roomId,
                                                                @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy")Date dateFrom,
                                                                @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date dateTo){
        BookingRoomSearchDTO criteria = new BookingRoomSearchDTO(roomId, dateFrom,dateTo);
        if(criteria.getDateTo() != null && criteria.getDateFrom() != null && !criteria.getDateTo().after(dateFrom)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
       try{
           List<BookingShowDTO> results = service.findRoomBookingsForPeriod(criteria);
           return new ResponseEntity<>(results, HttpStatus.OK);
       }catch (Exception e){
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<List<BookingShowDTO>> getCustomerBookings(@PathVariable("customerId") long customerId,
                                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date dateFrom,
                                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date dateTo){
        BookingCustomerSearchDTO criteria = new BookingCustomerSearchDTO(customerId, dateFrom, dateTo);
        if(criteria.getDateTo() != null && criteria.getDateFrom() != null && !criteria.getDateTo().after(dateFrom)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            List<BookingShowDTO> results = service.findCustomerBookingForPeriod(criteria);
            return new ResponseEntity<>(results, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BookingShowDTO> getBookingById(@PathVariable("id") long id){
        try{
            BookingShowDTO result = service.findBookingById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> makeBooking(@Valid @RequestBody BookingInsertDTO insertDTO,
                                                      BindingResult bindingResult){
        insertValidator.validate(insertDTO, bindingResult);
        List<String> errors = new ArrayList<>();
        if(bindingResult.hasErrors()){
            errors = bindingResult.getAllErrors().stream().map(m -> m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try{
            BookingShowDTO result = service.makeBooking(insertDTO);
            URI path = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/")
                    .buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(path).body(result);
        }catch(UnableToSaveDataException e){
            errors.add(e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(RoomAlreadyBookedException e){
            errors.add(e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateBooking(@PathVariable("id") long id, @Valid @RequestBody BookingUpdateDTO updateDTO,
                                                        BindingResult bindingResult){
        updateValidator.validate(updateDTO, bindingResult);
        List<String> errors = new ArrayList<>();
        if(bindingResult.hasErrors()){
            errors = bindingResult.getAllErrors().stream().map(m -> m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        if(id != updateDTO.getId()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try{
            BookingShowDTO result = service.updateBooking(updateDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(UnableToSaveDataException e){
            errors.add(e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(RoomAlreadyBookedException e){
            errors.add(e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }catch (EntityNotFoundException e){
            errors.add(e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<BookingShowDTO> deleteBooking(@PathVariable("id") long id){
        try{
            BookingShowDTO result = service.findBookingById(id);
            service.cancelBooking(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
