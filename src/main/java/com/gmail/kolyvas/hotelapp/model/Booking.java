package com.gmail.kolyvas.hotelapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "BOOKINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.DATE)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.DATE)
    private Date dateTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;

    @Column(name = "PRICE", nullable = false)
    private Float price;

    @Column(name = "TOTAL_COST")
    private Float totalCost;

   @Column(name = "DURATION", nullable = false)
    private Integer duration;


    public void addRoom(Room newRoom){
        if(newRoom == null) return;
        for(Booking booking : newRoom.getBookings()){
            if(booking == this ) return;
        }
        room = newRoom;
        room.getBookings().add(this);

    }

    public void addCustomer(Customer newCustomer){
        if(newCustomer == null) return;
        for(Booking booking : newCustomer.getBookings()){
            if(booking == this) return;
        }
        customer = newCustomer;
        customer.getBookings().add(this);
    }


}
