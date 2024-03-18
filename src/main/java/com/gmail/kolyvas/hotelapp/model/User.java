package com.gmail.kolyvas.hotelapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "USERS")
@AllArgsConstructor
@NoArgsConstructor

public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USERNAME", length = 50, nullable = false)
    private String username;

    @Column(name = "PASSWORD", length = 512, nullable = false)
    private String password;

    @Column(name = "EMPLOYEE", nullable = false)
    private Boolean isEmployee = false;

    @OneToOne(mappedBy = "user")
    private Customer customer;

    public void deleteCustomer(){
        Customer oldCustomer = customer;
        customer = null;
        oldCustomer.setUser(null);
       /* if(oldCustomer.getUser() == this){
            oldCustomer.deleteUser();
        }*/
    }

    public void addCustomer(Customer newCustomer){
        if(newCustomer == null || newCustomer.getUser() == this ) return;
        customer = newCustomer;
        customer.setUser(this);
       /* if(newCustomer.getUser() != this){
            newCustomer.addUser(this);
        }*/
    }
}
