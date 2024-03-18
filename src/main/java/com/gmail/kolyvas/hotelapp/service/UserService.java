package com.gmail.kolyvas.hotelapp.service;

import com.gmail.kolyvas.hotelapp.dto.user.UserChangePasswordDTO;
import com.gmail.kolyvas.hotelapp.dto.user.UserLoginDTO;
import com.gmail.kolyvas.hotelapp.dto.user.UserRegisterDTO;
import com.gmail.kolyvas.hotelapp.dto.user.UserShowDTO;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.InvalidUserRegisterException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.transaction.Transactional;
import com.gmail.kolyvas.hotelapp.model.User;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.gmail.kolyvas.hotelapp.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;


    private final ModelMapper mapper;

@Autowired
    public UserService(UserRepository userRepository,  ModelMapper mapper) {
        this.userRepository = userRepository;

        this.mapper = mapper;
    }

    public UserShowDTO authenticateUser(UserLoginDTO credentials){
        User user = userRepository.getUserByUsername(credentials.getUsername());
        if(user == null) return null;

        boolean isAuthenticated = BCrypt.checkpw(credentials.getPassword(), user.getPassword());
        if(!isAuthenticated) return null;

        return mapper.map(user,UserShowDTO.class);
    }

    public UserShowDTO getUser(long userId) throws EntityNotFoundException{
        User user = userRepository.getUserById(userId);
        if(user == null) throw new EntityNotFoundException("user");
        return mapper.map(user, UserShowDTO.class);
    }
    @Transactional
    public UserShowDTO updateUserPassword(UserChangePasswordDTO dto) throws EntityNotFoundException, InvalidUserRegisterException {
        User user = userRepository.getUserById(dto.getId());
        if(user == null) throw new EntityNotFoundException("user");
        boolean isAuthenticated = BCrypt.checkpw(dto.getOldPassword(), user.getPassword());
        if(!isAuthenticated) throw new InvalidUserRegisterException("The password is incorrect");

        if(!dto.getPassword().equals(dto.getConfirmPassword())) throw new InvalidUserRegisterException("You must submit twice the same password");

        String hashed = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(7));
        user.setPassword(hashed);
        userRepository.save(user);
        return mapper.map(user, UserShowDTO.class);

    }

    public List<UserShowDTO> getAllUsers(){
        List<User> data = userRepository.findAll();
        List<UserShowDTO> results = new ArrayList<>();
        for(User user : data){
            UserShowDTO dto = mapper.map(user, UserShowDTO.class);
            results.add(dto);
        }
        return results;
    }

    public List<UserShowDTO> getAllEnterpriseUsers(){
        List<User> data = userRepository.findAllEnterpriseUsers();
        List<UserShowDTO> results = new ArrayList<>();
        for(User user : data){
            UserShowDTO dto = mapper.map(user, UserShowDTO.class);
            results.add(dto);
        }
        return results;
    }

    @Transactional
    public UserShowDTO registerUser(UserRegisterDTO registerDTO) throws InvalidUserRegisterException, UnableToSaveDataException{
        if(!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) throw new
                InvalidUserRegisterException("You must type twice the same password");
        if(userRepository.getUserByUsername(registerDTO.getUsername()) != null) throw new
                InvalidUserRegisterException("The username is already registered for another user");
        User user = new User();
        String hashed = BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt(7));

        user.setUsername(registerDTO.getUsername());
        user.setPassword(hashed);
        user.setIsEmployee(registerDTO.isEmployee());
        userRepository.save(user);
        if(user.getId() == null) throw new UnableToSaveDataException();
        return mapper.map(user, UserShowDTO.class);
    }

    @Transactional
    public void deleteUser(long userId) throws EntityNotFoundException{
        User user = userRepository.getUserById(userId);
        if(user == null) throw new EntityNotFoundException("user");
        userRepository.delete(user);
    }


}
