package com.gmail.kolyvas.hotelapp.controllers;

import com.gmail.kolyvas.hotelapp.dto.roocategory.RoomCategoryInsertDTO;
import com.gmail.kolyvas.hotelapp.dto.roocategory.RoomCategoryShowDTO;
import com.gmail.kolyvas.hotelapp.dto.roocategory.RoomCategoryUpdateDTO;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.gmail.kolyvas.hotelapp.service.RoomCategoryService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hotelapp/roomcategory")
public class RoomCategoryController {
    private final RoomCategoryService service;

    @Autowired
    public RoomCategoryController(RoomCategoryService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<RoomCategoryShowDTO>> getAll(){
        try{
            List<RoomCategoryShowDTO> results = service.getAll();
            return new ResponseEntity<>(results, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RoomCategoryShowDTO> getById(@PathVariable("id") long id){
        try{
            RoomCategoryShowDTO result = service.getById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> insertRoomCategory(@Valid @RequestBody RoomCategoryInsertDTO insertDTO,
                                                     BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream().map(m -> m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try{
            RoomCategoryShowDTO result = service.insertCategory(insertDTO);
            URI path = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/").buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(path).body(result);
        }catch(UnableToSaveDataException e){
            return new ResponseEntity<>(new ArrayList<String>().add(e.getMessage()),  HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateRoomCategory(@PathVariable("id") long id,
                                                                  @Valid @RequestBody RoomCategoryUpdateDTO updateDTO,
                                                                  BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream().map(m -> m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        if(id != updateDTO.getId()) return new ResponseEntity<>(new ArrayList<String>().add("Unauthorised"),HttpStatus.UNAUTHORIZED );
        try{
            RoomCategoryShowDTO result = service.updateCategory(updateDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(new ArrayList<String>().add(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RoomCategoryShowDTO> deleteRoomCategory(@PathVariable("id") long id){
        try{
            RoomCategoryShowDTO result = service.getById(id);
            service.deleteCateogry(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
