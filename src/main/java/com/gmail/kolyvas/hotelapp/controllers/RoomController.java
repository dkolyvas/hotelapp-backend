package com.gmail.kolyvas.hotelapp.controllers;

import com.gmail.kolyvas.hotelapp.dto.room.RoomInsertDTO;
import com.gmail.kolyvas.hotelapp.dto.room.RoomSearchDTO;
import com.gmail.kolyvas.hotelapp.dto.room.RoomShowDTO;
import com.gmail.kolyvas.hotelapp.dto.room.RoomUpdateDTO;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.gmail.kolyvas.hotelapp.service.RoomService;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/hotelapp/room")
public class RoomController {
    private final RoomService service;

    @Autowired
    public RoomController(RoomService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method= RequestMethod.GET)
    public ResponseEntity<List<RoomShowDTO>> findRooms(@RequestParam(required = false) Integer personsNo,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy")Date dateFrom,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date dateTo
                                                       ){
//        if(!(dateTo ==null) &&!(dateFrom==null) && !dateTo.after(dateFrom)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        RoomSearchDTO criteria = new RoomSearchDTO(personsNo,dateFrom, dateTo,categoryId);
//        List<RoomShowDTO> results;
//
//        try {
//            results = service.findRooms(criteria);
//            return new ResponseEntity<>(results, HttpStatus.OK);
//        }catch(Exception e){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//        }
        List<RoomShowDTO> results;
        try{
            if(dateTo == null && dateFrom == null && personsNo == null) results = service.findRooms();
            else if(dateTo != null && dateFrom != null && personsNo != null){
                results = service.findRooms(personsNo, dateFrom, dateTo);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(results, HttpStatus.OK);
        }catch(Exception e){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RoomShowDTO> getRoomById(@PathVariable("id") long id){
        try{
            RoomShowDTO room = service.findRoom(id);
            return new ResponseEntity<>(room, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/code/{code}", method = RequestMethod.GET)
    public ResponseEntity<RoomShowDTO> getRoomByCode(@PathVariable("code") String code){
        try{
            RoomShowDTO room = service.findRoom(code);
            return new ResponseEntity<>(room, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> insertRoom(@Valid @RequestBody RoomInsertDTO insertDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }
        try{
            RoomShowDTO result = service.insertRoom(insertDTO);
            URI path = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("{id}")
                    .buildAndExpand(result.getId())
                    .toUri();
            return ResponseEntity.created(path).body(result);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RoomShowDTO> deleteRoom(@PathVariable("id") long id){
        try{
            RoomShowDTO room = service.findRoom(id);
            service.deleteRoom(id);
            return new ResponseEntity<>(room, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateRoom(@PathVariable("id") long id, @Valid @RequestBody RoomUpdateDTO updateDTO,
                                                  BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        if(id != updateDTO.getId()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try{
            RoomShowDTO result = service.updateRoom(updateDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
