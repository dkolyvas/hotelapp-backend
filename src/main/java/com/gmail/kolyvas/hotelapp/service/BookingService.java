package com.gmail.kolyvas.hotelapp.service;

import com.gmail.kolyvas.hotelapp.dto.booking.*;
import com.gmail.kolyvas.hotelapp.exceptions.EntityNotFoundException;
import com.gmail.kolyvas.hotelapp.exceptions.RoomAlreadyBookedException;
import com.gmail.kolyvas.hotelapp.exceptions.UnableToSaveDataException;
import jakarta.transaction.Transactional;
import com.gmail.kolyvas.hotelapp.model.Booking;
import com.gmail.kolyvas.hotelapp.model.Customer;
import com.gmail.kolyvas.hotelapp.model.PricingPolicy;
import com.gmail.kolyvas.hotelapp.model.Room;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmail.kolyvas.hotelapp.repositories.BookingRepository;
import com.gmail.kolyvas.hotelapp.repositories.CustomerRepository;
import com.gmail.kolyvas.hotelapp.repositories.PricingPolicyRepository;
import com.gmail.kolyvas.hotelapp.repositories.RoomRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;
    private final PricingPolicyRepository pricingPolicyRepository;
    private ModelMapper mapper;

    @Autowired
    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, CustomerRepository customerRepository, PricingPolicyRepository pricingPolicyRepository, ModelMapper mapper) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
        this.pricingPolicyRepository = pricingPolicyRepository;
        this.mapper = mapper;
    }

    public List<BookingShowDTO> findRoomBookings(Long roomId){
        List<Booking> data = bookingRepository.findRoomBookings(roomId);
        List<BookingShowDTO> results = new ArrayList<>();
        for(Booking booking : data){
            BookingShowDTO resultItem = mapper.map(booking, BookingShowDTO.class);
            results.add(resultItem);
        }
        return results;
    }

    public BookingShowDTO findBookingById(long id) throws EntityNotFoundException{
        Booking booking = bookingRepository.findBookingById(id);
        if(booking == null) throw new EntityNotFoundException("booking");
        return mapper.map(booking, BookingShowDTO.class);
    }

    public List<BookingShowDTO> findCustomerBookings(Long customerId){
        List<Booking> data = bookingRepository.findCustomerBookings(customerId);
        List<BookingShowDTO> results = new ArrayList<>();
        for(Booking booking : data){
            BookingShowDTO resultItem = mapper.map(booking, BookingShowDTO.class);
            results.add(resultItem);
        }
        return results;
    }

    public List<BookingShowDTO> findRoomBookingsForPeriod(BookingRoomSearchDTO criteria){
        if(criteria.getDateFrom() == null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(2000,01,01);

            criteria.setDateFrom(calendar.getTime());
            }
        if(criteria.getDateTo() == null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(2099, 12,31);
            criteria.setDateTo(calendar.getTime());
        }
        List<Booking> data = bookingRepository.findRoomBookingInPeriod(criteria.getRoomId(), criteria.getDateFrom(), criteria.getDateTo());
        List<BookingShowDTO> results = new ArrayList<>();
        for(Booking booking : data){
            BookingShowDTO resultItem = mapper.map(booking, BookingShowDTO.class);
            results.add(resultItem);
        }
        return results;

    }

    public List<BookingShowDTO> findCustomerBookingForPeriod(BookingCustomerSearchDTO criteria){
        if(criteria.getDateFrom() == null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(2000,01,01);

            criteria.setDateFrom(calendar.getTime());
        }
        if(criteria.getDateTo() == null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(2099, 12,31);
            criteria.setDateTo(calendar.getTime());
        }
        List<Booking> data = bookingRepository.findCustomerBookingsForPeriod(criteria.getCustomerId(), criteria.getDateFrom(), criteria.getDateTo());
        List<BookingShowDTO> results = new ArrayList<>();
        for(Booking booking : data){
            BookingShowDTO resultItem = mapper.map(booking, BookingShowDTO.class);
            results.add(resultItem);
        }
        return results;
    }
@Transactional
    public BookingShowDTO makeBooking(BookingInsertDTO insertDTO) throws RoomAlreadyBookedException, EntityNotFoundException, UnableToSaveDataException{
        Booking booking = mapper.map(insertDTO, Booking.class);

         List<Booking> bookings = bookingRepository.findRoomBookingInPeriod(insertDTO.getRoomId(), insertDTO.getDateFrom(), insertDTO.getDateTo());
        boolean isAvailable = bookings.isEmpty();
         if(!isAvailable) throw new RoomAlreadyBookedException();

        Room room = roomRepository.findRoomById(insertDTO.getRoomId());
        if(room == null) throw new EntityNotFoundException("room");

        Customer customer = customerRepository.findCustomerById(insertDTO.getCustomerId());
        if(customer == null ) throw new EntityNotFoundException("customer");

        PricingPolicy policy = pricingPolicyRepository.findPricingPolicyById(insertDTO.getPricingPolicyId());
        if(policy == null ) throw new EntityNotFoundException("pricing policy");

        int duration = (int)calculateDaySpan(insertDTO.getDateFrom(), insertDTO.getDateTo());
        Float totalCost = duration * policy.getPrice();

        booking.addCustomer(customer);
        booking.addRoom(room);
        booking.setPrice(policy.getPrice());
        booking.setDuration(duration);
        booking.setTotalCost(totalCost);
        bookingRepository.save(booking);

        return mapper.map(booking, BookingShowDTO.class);
    }
@Transactional
    public BookingShowDTO updateBooking(BookingUpdateDTO updateDTO) throws EntityNotFoundException, RoomAlreadyBookedException, UnableToSaveDataException{
        Booking booking = bookingRepository.findBookingById(updateDTO.getId());
        if(booking == null) throw new EntityNotFoundException("booking");

        boolean isAvailable = bookingRepository.findRoomBookingInPeriod(updateDTO.getRoomId(), updateDTO.getDateFrom(), updateDTO.getDateTo()).isEmpty();
        if(!isAvailable) throw new RoomAlreadyBookedException();

        Room room = roomRepository.findRoomById(updateDTO.getRoomId());
        if(room == null) throw new EntityNotFoundException("room");

        Customer customer = customerRepository.findCustomerById(updateDTO.getCustomerId());
        if(customer == null ) throw new EntityNotFoundException("customer");

        PricingPolicy policy = pricingPolicyRepository.findPricingPolicyById(updateDTO.getPricingPolicyId());
        if(policy == null ) throw new EntityNotFoundException("pricing policy");

        int duration = (int)calculateDaySpan(updateDTO.getDateFrom(), updateDTO.getDateTo());
        Float totalCost = duration * policy.getPrice();

        booking.setDateFrom(updateDTO.getDateFrom());
        booking.setDateTo(updateDTO.getDateTo());
        booking.addCustomer(customer);
        booking.addRoom(room);
        booking.setPrice(policy.getPrice());
        booking.setDuration(duration);
        booking.setTotalCost(totalCost);
        bookingRepository.save(booking);
        if(booking.getId() == null) throw new UnableToSaveDataException();
        return mapper.map(booking, BookingShowDTO.class);

    }
    @Transactional
    public void cancelBooking(Long bookingId) throws EntityNotFoundException{
        Booking booking = bookingRepository.findBookingById(bookingId);
        if(booking == null ) throw new EntityNotFoundException("booking");
        bookingRepository.delete(booking);
    }





    private int calculateDaySpan(Date dateFrom, Date dateTo){
        long diffInMs = dateTo.getTime() - dateFrom.getTime();
        return (int)TimeUnit.DAYS.convert(diffInMs,TimeUnit.MILLISECONDS);
    }
}
