package com.gmail.kolyvas.hotelapp.service;

import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicyInsertDTO;
import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicySearchDTO;
import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicyShowDTO;
import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicyUpdateDTO;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.transaction.Transactional;
import com.gmail.kolyvas.hotelapp.model.CustomerType;
import com.gmail.kolyvas.hotelapp.model.PricingPolicy;
import com.gmail.kolyvas.hotelapp.model.RoomCategory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.gmail.kolyvas.hotelapp.repositories.CustomerTypeRepository;
import com.gmail.kolyvas.hotelapp.repositories.PricingPolicyRepository;
import com.gmail.kolyvas.hotelapp.repositories.RoomCategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PricingPolicyService {
    private final ModelMapper mapper;
    private final PricingPolicyRepository repository;
    private final RoomCategoryRepository roomCategoryRepository;
    private final CustomerTypeRepository customerTypeRepository;

    public PricingPolicyService(ModelMapper mapper, PricingPolicyRepository repository,
                                RoomCategoryRepository roomCategoryRepository, CustomerTypeRepository customerTypeRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.roomCategoryRepository = roomCategoryRepository;
        this.customerTypeRepository = customerTypeRepository;
    }

    public List<PricingPolicyShowDTO> findPoliciesForCustomerType(long typeId){
        List<PricingPolicy> data = repository.findPricingPolicyForCustomerType(typeId);
        List<PricingPolicyShowDTO> results = new ArrayList<>();
        for(PricingPolicy policy : data){
            PricingPolicyShowDTO dto = mapper.map(policy, PricingPolicyShowDTO.class);
            results.add(dto);
        }
        return results;
    }

    public List<PricingPolicyShowDTO> findPoliciesForRoomCategory(long categoryId){
        List<PricingPolicy> data = repository.findPricingPolicyForRoomCategory(categoryId);
        List<PricingPolicyShowDTO> results = new ArrayList<>();
        for(PricingPolicy policy : data){
            PricingPolicyShowDTO dto = mapper.map(policy, PricingPolicyShowDTO.class);
            results.add(dto);
        }
        return results;
    }

    public List<PricingPolicyShowDTO> findSpecifiedPricingPolicies(PricingPolicySearchDTO criteria){
        List<PricingPolicy> data;
        List<PricingPolicyShowDTO> results = new ArrayList<>();
        if(criteria.getCustomerCategoryId() !=null){
            data = repository.findSpecificPricingPolicies(criteria.getRoomCategoryId(), criteria.getCustomerCategoryId());
            if(!data.isEmpty()){
                for(PricingPolicy policy : data){
                    PricingPolicyShowDTO dto = mapper.map(policy, PricingPolicyShowDTO.class);
                    results.add(dto);
                }
                return results;
            }
        }
        data = repository.findDefaultPricingPolicies(criteria.getRoomCategoryId());
        for(PricingPolicy policy : data){
            PricingPolicyShowDTO dto = mapper.map(policy, PricingPolicyShowDTO.class);
            results.add(dto);
            }
        return results;
    }

    @Transactional
    public PricingPolicyShowDTO insertPricingPolicy(PricingPolicyInsertDTO insertDTO)
            throws EntityNotFoundException, UnableToSaveDataException {
        PricingPolicy policy = mapper.map(insertDTO, PricingPolicy.class);

        RoomCategory roomCategory = roomCategoryRepository.findRoomCategoryById(insertDTO.getRoomCategoryId());
        if(roomCategory == null) throw new EntityNotFoundException("room category");
        policy.addCategory(roomCategory);

        if(insertDTO.getCustomerTypeId() != null){
            CustomerType type = customerTypeRepository.findCustomerTypeById(insertDTO.getCustomerTypeId());
            if(type == null) throw new EntityNotFoundException("custoer type");
            policy.addCustomerType(type);
        }

        repository.save(policy);
        if(policy.getId() == null) throw new UnableToSaveDataException();
        return mapper.map(policy, PricingPolicyShowDTO.class);

    }

    @Transactional
    public PricingPolicyShowDTO updatePricingPolicy(PricingPolicyUpdateDTO updateDTO)
    throws EntityNotFoundException{
        PricingPolicy policy = repository.findPricingPolicyById(updateDTO.getId());
        if(policy == null) throw new EntityNotFoundException("pricing policy");
        policy.setPrice(updateDTO.getPrice());
        policy.setSpecification(updateDTO.getSpecification());

        repository.save(policy);
        return mapper.map(policy,PricingPolicyShowDTO.class);

    }

    @Transactional
    public void deletePricingPolicy(long id) throws EntityNotFoundException{
        PricingPolicy policy = repository.findPricingPolicyById(id);
        if(policy == null) throw new EntityNotFoundException("pricing polichy");
        repository.delete(policy);
    }

    public PricingPolicyShowDTO getPolicyById(long id) throws EntityNotFoundException{
        PricingPolicy policy = repository.findPricingPolicyById(id);
        if(policy == null) throw new EntityNotFoundException("pricing policy");
        return mapper.map(policy, PricingPolicyShowDTO.class);
    }

}

