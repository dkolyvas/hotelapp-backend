package com.gmail.kolyvas.hotelapp.controllers;

import com.gmail.kolyvas.hotelapp.dto.user.*;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.InvalidUserRegisterException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import com.gmail.kolyvas.hotelapp.service.CustomUserDetailsService;
import com.gmail.kolyvas.hotelapp.util.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.gmail.kolyvas.hotelapp.service.UserService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hotelapp/user")
public class UserController {

    private final UserService service;

    private final CustomUserDetailsService authenticationService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public UserController(UserService service, CustomUserDetailsService authenticationService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.service = service;
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserShowDTO> getUserById(@PathVariable("id") long id){
        try{
            UserShowDTO result = service.getUser(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<UserShowDTO>> getAll(){
        try{
            List<UserShowDTO> results = service.getAllUsers();
            return new ResponseEntity<>(results, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/enterprise", method = RequestMethod.GET)
    public ResponseEntity<List<UserShowDTO>> getAllEnterpriseUsers(){
        try{
            List<UserShowDTO> results = service.getAllEnterpriseUsers();
            return new ResponseEntity<>(results, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> register(UserRegisterDTO registerDTO){
        List<String> errors = new ArrayList<>();
        try{
            UserShowDTO result = service.registerUser(registerDTO);
            URI path = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/").buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(path).body(result);
        }catch(InvalidUserRegisterException e){
            errors.add(e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }catch(UnableToSaveDataException e){
            errors.add(e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Object> registerClientUser(@Valid @RequestBody UserRegisterDTO registerDTO, BindingResult bindingResult){

        registerDTO.setEmployee(false);
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream().map(m ->m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return register(registerDTO);


    }

    @RequestMapping(value = "/enterprise", method = RequestMethod.POST)
    public ResponseEntity<Object> registerEnterpriseUser(@Valid @RequestBody UserRegisterDTO registerDTO, BindingResult bindingResult){
        registerDTO.setEmployee(true);
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream().map(m ->m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return register(registerDTO);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateUser(@PathVariable("id") long id, @Valid @RequestBody UserChangePasswordDTO updateDto,
                                             BindingResult bindingResult){
        if(updateDto.getId() != id) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<String> errors = new ArrayList<>();
        if(bindingResult.hasErrors()){
            errors = bindingResult.getAllErrors().stream().map(m ->m.getDefaultMessage()).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try{
            UserShowDTO result = service.updateUserPassword(updateDto);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            errors.add(e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        }catch(InvalidUserRegisterException e){
            errors.add(e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<UserShowDTO> deleteUser(@PathVariable("id") long id){
        try{
            UserShowDTO result = service.getUser(id);
            service.deleteUser(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO credentials){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(authentication);
            UserTokenDTO result = new UserTokenDTO();
            result.setToken(token);
            result.setRole(authentication.getAuthorities().toArray()[0].toString());
            result.setUsername(credentials.getUsername());
            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
