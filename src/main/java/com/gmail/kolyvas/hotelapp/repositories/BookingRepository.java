package com.gmail.kolyvas.hotelapp.repositories;

import com.gmail.kolyvas.hotelapp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    public Booking findBookingById(Long id);
    @Query("select b from Booking  b where b.customer.id = ?1")
    public List<Booking> findCustomerBookings(Long customerId);

    @Query("select b from Booking  b where b.room.id = ?1")
    public List<Booking> findRoomBookings(Long roomId);

    @Query("select b from Booking b where b.dateFrom >= ?1 and b.dateTo < ?2")
    public List<Booking> findBookingForPeriod(Date dateFrom, Date dateSince);

    @Query("select b from Booking b where b.room.id = ?1 and b.dateTo > ?2 and b.dateFrom < ?3")
    public List<Booking> findRoomBookingInPeriod(Long roomId, Date dateFrom, Date dateTo);

    @Query("select b from Booking b where b.customer.id = ?1 and b.dateTo > ?2 and b.dateFrom <?3")
    public List<Booking> findCustomerBookingsForPeriod(Long customerId, Date dateFrom, Date dateTo);




}
