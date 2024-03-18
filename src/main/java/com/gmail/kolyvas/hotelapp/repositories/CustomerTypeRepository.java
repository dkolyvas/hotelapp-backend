package com.gmail.kolyvas.hotelapp.repositories;

import com.gmail.kolyvas.hotelapp.model.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {
    public CustomerType findCustomerTypeById(Long id);
}
