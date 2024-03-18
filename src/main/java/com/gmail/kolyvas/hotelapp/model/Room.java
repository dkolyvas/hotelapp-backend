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
@Table(name = "ROOMS")
public class Room {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name= "CODE", nullable = false, unique = true)
    private String code;

    @Column(name = "FLOOR", nullable = false)
    private Integer floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private RoomCategory roomCategory;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings = new ArrayList<>();



  /*  protected RoomCategory getRoomCategory() {
        return roomCategory;
    }

    protected void setRoomCategory(RoomCategory roomCategory) {
        this.roomCategory = roomCategory;
    }*/

    public void deleteRoomCategory(){
        RoomCategory oldCategory = roomCategory;
        roomCategory = null;
        oldCategory.getRooms().remove(this);

     /*   boolean found = false;
        RoomCategory oldCategory = roomCategory;
        roomCategory = null;
        if(oldCategory == null) return;
        for(Room room : oldCategory.getAllRooms()){
            if(room == this){
                found = true;
                break;
            }
        }
        if(found){
            oldCategory.deleteRoom(this);
        }*/
    }

    public void addRoomCategory(RoomCategory category) {

        if(category == null ) return;
        for(Room room : category.getRooms()){
            if(room == this){
                return;
            }
        }
        roomCategory = category;
        roomCategory.getRooms().add(this);
        /*deleteRoomCategory();
        roomCategory = category;
        boolean found = false;
        for(Room room : category.getAllRooms()){
            if(room == this){
                found = true;
                break;
            }
        }
        if(!found){
            category.addRoom(this);
        }
    }*/
    }

    public void addBooking(Booking booking){
        if(booking == null || booking.getRoom() == this) return;
        bookings.add(booking);
        booking.setRoom(this);

    }
}
