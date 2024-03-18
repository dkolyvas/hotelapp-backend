package com.gmail.kolyvas.hotelapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ROOM_CATEGORIES")
public class RoomCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name= "ID")
    private Long id;



    @Column(name = "PERSONS_NUMBER", nullable = false, unique = false)
    private Integer personsNumber;

    @Column(name = "DESCRIPTION",length = 50,nullable = false, unique = false)
    private String description;

    @OneToMany(mappedBy = "roomCategory")
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "roomCategory")
    private List<PricingPolicy> pricingPolicies = new ArrayList<>();



    protected List<Room> getRooms() {
        return rooms;
    }

    protected void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }



    public void deleteRoom(Room room){

        rooms.remove(room);
        room.setRoomCategory(null);
        /*if(room.getRoomCategory()== this){
            room.deleteRoomCategory();
        }*/

    }

    public void addRoom(Room room){
        if(room == null || room.getRoomCategory()==this) return;
        rooms.add(room);
        room.setRoomCategory(this);
        /*if(room.getRoomCategory() != this){
            room.addRoomCategory(this);
        }*/
    }



    public void deletePolicy(PricingPolicy policy){
        if(policy == null || policy.getRoomCategory()== null) return;
        pricingPolicies.remove(policy);
        policy.setRoomCategory(null);
        /*if(policy.getRoomCategory() == this){
            policy.deleteCategory();
        }*/
    }

    public void addPricingPolicy(PricingPolicy policy){
        if(policy == null  || policy.getRoomCategory() == this) return;
        pricingPolicies.add(policy);
        policy.setRoomCategory(this);
        /*if(policy.getRoomCategory() != this){
            policy.addCategory(this);
        }*/

    }
}
