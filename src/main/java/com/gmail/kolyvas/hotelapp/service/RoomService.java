package com.gmail.kolyvas.hotelapp.service;

import com.gmail.kolyvas.hotelapp.dto.room.RoomInsertDTO;
import com.gmail.kolyvas.hotelapp.dto.room.RoomSearchDTO;
import com.gmail.kolyvas.hotelapp.dto.room.RoomShowDTO;
import com.gmail.kolyvas.hotelapp.dto.room.RoomUpdateDTO;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.gmail.kolyvas.hotelapp.model.Room;
import com.gmail.kolyvas.hotelapp.model.RoomCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.gmail.kolyvas.hotelapp.repositories.RoomCategoryRepository;
import com.gmail.kolyvas.hotelapp.repositories.RoomRepository;
import com.gmail.kolyvas.hotelapp.repositories.specifications.RoomSpeicifications;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RoomService {
    private final ModelMapper mapper;
    private final RoomRepository roomRepository;
    private final RoomCategoryRepository categoryRepository;

    @Autowired
    public RoomService(ModelMapper mapper, RoomRepository roomRepository, RoomCategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.roomRepository = roomRepository;
        this.categoryRepository = categoryRepository;
    }

//    public List<RoomShowDTO> findRooms(RoomSearchDTO searchDTO){
//        Specification<Room> spec = Specification.where(null);
//        if(searchDTO.getPersonsNo() != null){
//            spec = spec.and(RoomSpeicifications.isForPersons(searchDTO.getPersonsNo()));
//        }
//        if(searchDTO.getDateFrom() != null && searchDTO.getDateTo() != null){
//            spec = spec.and(RoomSpeicifications.isAvailable(searchDTO.getDateFrom(), searchDTO.getDateTo()));
//        }
//        if(searchDTO.getCategoryId() != null){
//            spec = spec.and(RoomSpeicifications.isCategory(searchDTO.getCategoryId()));
//        }
//        List<Room> data = roomRepository.findAll(spec);
//        List<RoomShowDTO> results = new ArrayList<>();
//
//        for(Room room : data){
//            RoomShowDTO dto = mapper.map(room, RoomShowDTO.class);
//            results.add(dto);
//        }
//        return results;
//
//    }

    public List<RoomShowDTO> findRooms(int personsNo, Date startDate, Date endDate){
        List<Room> data = roomRepository.findAvailableRooms(personsNo,endDate, startDate);
        List<RoomShowDTO> results = new ArrayList<>();

        for(Room room : data){
            RoomShowDTO dto = mapper.map(room, RoomShowDTO.class);
            results.add(dto);
        }
        return results;
    }

    public List<RoomShowDTO> findRooms(){
        List<Room> data = roomRepository.findAll();
        List<RoomShowDTO> results = new ArrayList<>();

        for(Room room : data){
            RoomShowDTO dto = mapper.map(room, RoomShowDTO.class);
            results.add(dto);
        }
        return results;
    }

    public RoomShowDTO findRoom(Long id) throws EntityNotFoundException{
        try {
            Room room = roomRepository.findRoomById(id);
            if (room == null) throw new EntityNotFoundException("room");
            return mapper.map(room, RoomShowDTO.class);
        }catch(EntityNotFoundException e){
            log.warn(e.getMessage());
            throw e;
        }
    }

    public RoomShowDTO findRoom(String code) throws EntityNotFoundException{
        try {
            Room room = roomRepository.findRoomByCode(code);
            if (room == null) throw new EntityNotFoundException("room");
            return mapper.map(room, RoomShowDTO.class);
        }catch(EntityNotFoundException e){
            log.warn(e.getMessage());
            throw e;
        }
    }
    @Transactional
    public RoomShowDTO insertRoom(RoomInsertDTO insertDTO) throws EntityNotFoundException, UnableToSaveDataException {
        try {
            Room room = mapper.map(insertDTO, Room.class);
            RoomCategory category = categoryRepository.findRoomCategoryById(insertDTO.getCategoryId());
            if (category == null) throw new EntityNotFoundException("room category");
            room.addRoomCategory(category);
            roomRepository.save(room);
            if (room.getId() == null) throw new UnableToSaveDataException();
            return mapper.map(room, RoomShowDTO.class);
        }catch(EntityNotFoundException e){
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Transactional
    public RoomShowDTO updateRoom(RoomUpdateDTO updateDTO) throws EntityNotFoundException{
        try {
            Room room = roomRepository.findRoomById(updateDTO.getId());
            if (room == null) throw new EntityNotFoundException("room");
            room.setCode(updateDTO.getCode());
            room.setFloor(updateDTO.getFloor());
            RoomCategory category = categoryRepository.findRoomCategoryById(updateDTO.getCategoryId());
            if (category == null) throw new EntityNotFoundException("room category");
            room.addRoomCategory(category);
            roomRepository.save(room);
            return mapper.map(room, RoomShowDTO.class);
        }catch (EntityNotFoundException e){
            log.warn(e.getMessage());
            throw e;
        }
    }


    @Transactional
    public void deleteRoom(Long id) throws EntityNotFoundException {
        try {
            Room room = roomRepository.findRoomById(id);
            if (room == null) throw new EntityNotFoundException("room");
            roomRepository.delete(room);
        } catch (EntityNotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }
}
