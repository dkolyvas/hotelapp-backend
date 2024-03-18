package com.gmail.kolyvas.hotelapp.service;

import com.gmail.kolyvas.hotelapp.dto.customertype.CustomerTypeInsertDTO;
import com.gmail.kolyvas.hotelapp.dto.customertype.CustomerTypeShowDTO;
import com.gmail.kolyvas.hotelapp.dto.customertype.CustomerTypeUpdateDTO;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.transaction.Transactional;
import com.gmail.kolyvas.hotelapp.model.CustomerType;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.gmail.kolyvas.hotelapp.repositories.CustomerTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerTypeService {

    private final CustomerTypeRepository repository;
    private final ModelMapper mapper;

    public CustomerTypeService(CustomerTypeRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CustomerTypeShowDTO> getAll(){
        List<CustomerType> data = repository.findAll();
        List<CustomerTypeShowDTO> results = new ArrayList<>();
        for(CustomerType type : data){
            CustomerTypeShowDTO dto = mapper.map(type, CustomerTypeShowDTO.class);
            results.add(dto);
        }
        return  results;
    }

    public CustomerTypeShowDTO getById(long id) throws EntityNotFoundException{
        CustomerType type = repository.findCustomerTypeById(id);
        if(type == null) throw new EntityNotFoundException("customer type");
        return mapper.map(type, CustomerTypeShowDTO.class);
    }

    @Transactional
    public CustomerTypeShowDTO insertType(CustomerTypeInsertDTO insertDTO) throws UnableToSaveDataException {
        CustomerType type = new CustomerType();
        type.setName(insertDTO.getName());
        repository.save(type);
        if(type.getId() == null) throw new UnableToSaveDataException();
        return mapper.map(type, CustomerTypeShowDTO.class);
    }

    @Transactional
    public CustomerTypeShowDTO updateType(CustomerTypeUpdateDTO updateDTO) throws
            EntityNotFoundException{
        CustomerType type = repository.findCustomerTypeById(updateDTO.getId());
        if(type == null) throw new EntityNotFoundException("Customer type");
        type.setName(updateDTO.getName());
        repository.save(type);
        return mapper.map(type, CustomerTypeShowDTO.class);
    }

    @Transactional
    public void deleteType(long id) throws EntityNotFoundException{
        CustomerType type = repository.findCustomerTypeById(id);
        if(type == null) throw new EntityNotFoundException("customer type");
        repository.delete(type);
    }

}
