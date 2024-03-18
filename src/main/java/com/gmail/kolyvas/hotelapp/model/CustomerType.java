package com.gmail.kolyvas.hotelapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "CUSTOMER_TYPES")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CustomerType {
    @Id
    @Column(name= "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "type")
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "customerType")
    private List<PricingPolicy> policies = new ArrayList<>();

    public List<Customer> getAllCustomers(){ return Collections.unmodifiableList(customers);
    }

    public List<PricingPolicy> getAllPolicies(){ return Collections.unmodifiableList(policies);}

    public void deleteCustomer(Customer customer){
        customers.remove(customer);
        customer.setType(null);

    }

    public void addCustomer(Customer customer){
        if(customer == null || customer.getType() == this) return;
        customers.add(customer);
        customer.setType(this);

    }

    public void deletePolicy(PricingPolicy policy){
        policies.remove(policy);
        policy.setCustomerType(null);
        //if(policy.getCustomerType() == this) policy.deleteCustomerType();
    }

    public void addPolicy(PricingPolicy policy){
        if(policy == null || policy.getCustomerType() == this) return;
        policies.add(policy);
        policy.setCustomerType(this);
        //if(policy.getCustomerType() != this) policy.addCustomerType(this);

    }
}
