package com.gmail.kolyvas.hotelapp.service;

import com.gmail.kolyvas.hotelapp.dto.customer.*;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.gmail.kolyvas.hotelapp.model.Customer;
import com.gmail.kolyvas.hotelapp.model.CustomerInformation;
import com.gmail.kolyvas.hotelapp.model.CustomerType;
import com.gmail.kolyvas.hotelapp.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.gmail.kolyvas.hotelapp.repositories.CustomerRepository;
import com.gmail.kolyvas.hotelapp.repositories.CustomerTypeRepository;
import com.gmail.kolyvas.hotelapp.repositories.UserRepository;
import com.gmail.kolyvas.hotelapp.repositories.specifications.CustomerSpecifications;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository repository;

    private final UserRepository userRepository;
    private final CustomerTypeRepository typeRepository;

    private final ModelMapper mapper;

    @Autowired
    public CustomerService(CustomerRepository repository, ModelMapper mapper, UserRepository userRepository, CustomerTypeRepository typeRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.typeRepository = typeRepository;
    }

    public List<CustomerShowDTO> getCustomers(CustomerSearchDTO searchDTO){
        Specification<Customer> spec = Specification.where(null);
        if(searchDTO.getType() != null) spec.and(CustomerSpecifications.isTypeOf(searchDTO.getType()));
        if(searchDTO.getEmail() != null && !searchDTO.getEmail().isEmpty()){
            spec = spec.and(CustomerSpecifications.hasEmail(searchDTO.getEmail()));
        }
        if(searchDTO.getPhone() != null && !searchDTO.getPhone().isEmpty()){
            spec = spec.and(CustomerSpecifications.hasPhoneNumber(searchDTO.getPhone()));
        }
        if(searchDTO.getSurname() != null && !searchDTO.getSurname().isEmpty()){
            spec = spec.and(CustomerSpecifications.hasSurnameLike(searchDTO.getSurname()));
        }
        if(searchDTO.getGivenName() != null && !searchDTO.getGivenName().isEmpty()){
            spec = spec.and(CustomerSpecifications.hasGivenNameLike(searchDTO.getGivenName()));
        }
        if(searchDTO.getCountry() !=null && !searchDTO.getCountry().isEmpty()){
            spec = spec.and(CustomerSpecifications.isFromCountry(searchDTO.getCountry()));
        }
        if(searchDTO.getPassportNo() != null && !searchDTO.getPassportNo().isEmpty()){

            spec = spec.and(CustomerSpecifications.hasPassport(searchDTO.getPassportNo()));
        }

        List<Customer> customers = repository.findAll(spec);
        List<CustomerShowDTO> resultList = new ArrayList<>();
        for(Customer customer: customers){
            CustomerShowDTO newItem = mapper.map(customer, CustomerShowDTO.class);
            resultList.add(newItem);
        }
        return resultList;
    }

    public CustomerShowDTO getCustomerById(Long id) throws EntityNotFoundException{
       try {
           Customer customer = repository.findCustomerById(id);
           if (customer == null) throw new EntityNotFoundException("customer");
           return mapper.map(customer, CustomerShowDTO.class);
       }catch(EntityNotFoundException e){
           log.warn(e.getMessage());
           throw e;
       }
    }

    public CustomerShowDTO getCustomerByUserId(Long userid) throws EntityNotFoundException{
       try {
           Customer customer = repository.findCustomerByUserid(userid);
           if (customer == null) throw new EntityNotFoundException("customer");
           return mapper.map(customer, CustomerShowDTO.class);
       }catch(EntityNotFoundException e){
           log.warn(e.getMessage());
           throw e;
       }
    }
    @Transactional
    public CustomerShowDTO insertCustomer(CustomerInsertDTO dto) throws UnableToSaveDataException, EntityNotFoundException {
        try {
            Customer customer = mapper.map(dto, Customer.class);

            if (dto.getUserId() != null) {
                User user = userRepository.getUserById(dto.getUserId());
                if (user != null) {
                    customer.addUser(user);
                }else {
                    throw new EntityNotFoundException("user");
                }
            }
            if(dto.getCustomerTypeId() != null){
                CustomerType type = typeRepository.findCustomerTypeById(dto.getCustomerTypeId());
                if(type == null) throw new EntityNotFoundException("customer type");
                customer.addType(type);
            }

            repository.save(customer);
            if (customer.getId() == null) throw new UnableToSaveDataException();


            return mapper.map(customer, CustomerShowDTO.class);
        }catch(UnableToSaveDataException e){
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Transactional
    public CustomerShowDTO updateCustomer(CustomerUpdateDTO dto) throws EntityNotFoundException{
         try {
             Customer customer = repository.findCustomerById(dto.getId());
             if (customer == null) throw new EntityNotFoundException("customer");
             customer.setEmail(dto.getEmail());
             customer.setCountry(dto.getCountry());
             customer.setAddress(dto.getAddress());
             customer.setGivenName(dto.getGivenName());
             customer.setSurname(dto.getSurname());
             customer.setPassportNo(dto.getPassportNo());
             customer.setPhone(dto.getPhone());

             if (dto.getUserId() != null) {
                 User user = userRepository.getUserById(dto.getUserId());
                 if (user != null) {
                     customer.addUser(user);
                 } else{
                     throw new EntityNotFoundException("user");
                 }
             }
             if(dto.getCustomerTypeId() != null){
                 CustomerType type = typeRepository.findCustomerTypeById(dto.getCustomerTypeId());
                 if(type == null) throw new EntityNotFoundException("customer type");
                 customer.addType(type);
             }

             repository.save(customer);
             return mapper.map(customer, CustomerShowDTO.class);
         }catch(EntityNotFoundException e){
             log.warn(e.getMessage());
             throw e;
         }
    }

    @Transactional
    public void deleteCustomer(Long id) throws EntityNotFoundException{
         try {
             Customer customer = repository.findCustomerById(id);
             if (customer == null) throw new EntityNotFoundException("customer");
             repository.delete(customer);
         }catch(EntityNotFoundException e){
             log.warn(e.getMessage());
             throw e;
         }
    }

    public List<CustomerInformationShowDTO> findCustomerSummaryForPeriod(Date from, Date to){
        List<CustomerInformation> data = repository.findCustomerWithBookingsForPeriod(from, to);
        List<CustomerInformationShowDTO> results = new ArrayList<>();
        for(CustomerInformation customer : data){
            CustomerInformationShowDTO dto = mapper.map(customer, CustomerInformationShowDTO.class);
            results.add(dto);
        }
        return results;
    }


}
