package com.gmail.kolyvas.hotelapp.service;

import com.gmail.kolyvas.hotelapp.dto.roocategory.RoomCategoryInsertDTO;
import com.gmail.kolyvas.hotelapp.dto.roocategory.RoomCategoryShowDTO;
import com.gmail.kolyvas.hotelapp.dto.roocategory.RoomCategoryUpdateDTO;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.transaction.Transactional;
import com.gmail.kolyvas.hotelapp.model.RoomCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmail.kolyvas.hotelapp.repositories.RoomCategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomCategoryService {
    private final ModelMapper mapper;
    private final RoomCategoryRepository repository;

@Autowired
    public RoomCategoryService(ModelMapper mapper, RoomCategoryRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


    public List<RoomCategoryShowDTO> getAll(){
        List<RoomCategory> data = repository.findAll();
        List<RoomCategoryShowDTO> results = new ArrayList<>();
        for(RoomCategory category : data){
            RoomCategoryShowDTO dto = mapper.map(category, RoomCategoryShowDTO.class);
            results.add(dto);
        }
        return results;
    }

    public RoomCategoryShowDTO getById(long id) throws EntityNotFoundException{
        RoomCategory category = repository.findRoomCategoryById(id);
        if(category == null) throw new EntityNotFoundException("Room category");
        return mapper.map(category, RoomCategoryShowDTO.class);
    }

    @Transactional
    public RoomCategoryShowDTO insertCategory(RoomCategoryInsertDTO insertDTO) throws UnableToSaveDataException {
        RoomCategory category = mapper.map(insertDTO, RoomCategory.class);
        repository.save(category);
        if(category.getId() == null) throw new UnableToSaveDataException();
        return mapper.map(category, RoomCategoryShowDTO.class);
    }

    @Transactional
    public RoomCategoryShowDTO updateCategory(RoomCategoryUpdateDTO updateDTO) throws EntityNotFoundException{
        RoomCategory category = repository.findRoomCategoryById(updateDTO.getId());
        if(category == null) throw new EntityNotFoundException("room category");
        category.setPersonsNumber(updateDTO.getPersonsNumber());
        category.setDescription(updateDTO.getDescription());
        repository.save(category);
        return mapper.map(category, RoomCategoryShowDTO.class);
    }

    @Transactional
    public  void deleteCateogry(long id) throws EntityNotFoundException{
        RoomCategory category = repository.findRoomCategoryById(id);
        if(category == null) throw new EntityNotFoundException("room category");
        repository.delete(category);

    }


}
