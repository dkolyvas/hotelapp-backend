package com.gmail.kolyvas.hotelapp.repositories;

import com.gmail.kolyvas.hotelapp.model.Customer;
import com.gmail.kolyvas.hotelapp.model.CustomerInformation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public List<Customer> findAll(Specification<Customer> specification);
    public Customer findCustomerById(Long id);

    @Query("select c from Customer c where c.user.id = ?1")
    public Customer findCustomerByUserid(Long userId);

    public Customer findCustomerByEmail(String email);

    public Customer findCustomerByPassportNo(String passportNo);

    @Query("select new com.gmail.kolyvas.hotelapp.model.CustomerInformation(b.customer, sum(b.totalCost) , sum(b.duration))  from Booking b  where b.dateFrom >=?1 and b.dateTo <= ?2 group by b.customer")
    public List<CustomerInformation> findCustomerWithBookingsForPeriod(Date dateFrom, Date dateTo);


    @Query("select c from Customer c where (select count(b) from Booking b where b.customer = c and b.dateFrom > ?2) >?1")
    public List<Customer> findCustomersWithMoreBookingsThanSince(int noOfBookings, Date since);
}
