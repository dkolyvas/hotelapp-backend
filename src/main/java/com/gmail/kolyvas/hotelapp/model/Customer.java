package com.gmail.kolyvas.hotelapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CUSTOMERS")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Customer {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "GIVENNAME", length = 50, nullable = false)
    private String givenName;

    @Column(name = "SURNAME", length = 70, nullable = false)
    private String surname;

    @Column(name = "ADDRESS", length = 100)
    private String address;

    @Column(name = "COUNTRY", length = 50)
    private String country;

    @Column(name = "PHONE", length = 30)
    private String phone;

    @Column(name = "EMAIL", length = 100, unique = true)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = true, unique = true)
    private User user;

    @Column(name = "PASSPORT_NO",length = 30, nullable = false)
    private String passportNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_TYPE_ID", nullable = true)
    private CustomerType type;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings = new ArrayList<>();

    public void deleteUser(){
        User oldUser = user;
        user = null;
        oldUser.setCustomer(null);
    }
    public void addUser(User newUser){
        if(newUser == null || newUser.getCustomer() == this) return;

        user = newUser;
        user.setCustomer(this);

        /*deleteUser();
        user = newUser;
        if(newUser.getCustomer() != this){
            newUser.addCustomer(this);
        }*/

    }

    public void deleteType(){
        CustomerType oldType = type;
        type = null;
        oldType.getCustomers().remove(this);

    }

    public void addType(CustomerType newType){
        if(newType == null) return;
        for(Customer customer : newType.getCustomers()){
            if(customer == this){
                return;
            }
        }
        type = newType;
        type.getCustomers().add(this);

        /*deleteType();
        type = newType;
        boolean found = false;
        for(Customer customer : newType.getCustomers()){
            if(customer == this){
                found = true;
                break;
            }
        }
        if(!found){
            type.addCustomer(this);
        }*/
    }

    public void addBooking(Booking booking){
        if(booking == null || booking.getCustomer() == this) return;
        bookings.add(booking);
        booking.setCustomer(this);
    }
}
