package com.gmail.kolyvas.hotelapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "PRICING_POLICIES")
@NoArgsConstructor
@AllArgsConstructor
public class PricingPolicy {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name ="PRICE", nullable = false)
    private Float price;

    @Column(name = "SPECIFICATION", nullable = false)
    private String specification;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private RoomCategory roomCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_TYPE_ID", nullable = true)
    private CustomerType customerType;

    public void deleteCategory(){
        RoomCategory oldCategory = roomCategory;
        roomCategory = null;
        oldCategory.getPricingPolicies().remove(this);
        /*boolean found = false;
        for(PricingPolicy policy : oldCategory.getAllPolicies()){
            if(policy == this){
                found = true;
                break;
            }
        }*/
       /* if(found) oldCategory.deletePolicy(this);*/

    }

    public void addCategory(RoomCategory category){

        if(category == null ) return;
        for(PricingPolicy policy : category.getPricingPolicies()){
            if(policy  == this){
                return;
            }
        }
        roomCategory = category;
        roomCategory.getPricingPolicies().add(this);

       /* deleteCategory();
        roomCategory = category;
        boolean found = false;
        for(PricingPolicy policy : category.getAllPolicies()){
            if(policy.getRoomCategory() == category){
                found = true;
                break;
            }
        }
        if(!found){
            category.addPricingPolicy(this);
        }*/

    }

    public void deleteCustomerType(){
        CustomerType oldType = customerType;
        customerType = null;
        oldType.getPolicies().remove(this);
        /*boolean found = false;
        for(PricingPolicy policy : oldType.getAllPolicies()){
            if(policy == this){
                found = true;
                break;
            }
        }
        if(found){
            oldType.deletePolicy(this);
        }*/
    }

    public void addCustomerType(CustomerType type){
        if(type == null ) return;
        for(PricingPolicy policy : type.getAllPolicies()){
            if(policy == this){
                return;
            }
        }
        customerType = type;
        customerType.getPolicies().add(this);
        /*deleteCustomerType();
        customerType = type;
        boolean found = false;
        for(PricingPolicy policy : type.getAllPolicies()){
            if(policy == this){
                found = true;
                break;
            }
        }
        if(!found){
            type.addPolicy(this);
        }*/
    }



}
