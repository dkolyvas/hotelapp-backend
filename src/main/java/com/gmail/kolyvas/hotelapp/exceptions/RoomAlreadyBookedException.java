package com.gmail.kolyvas.hotelapp.exceptions;

public class RoomAlreadyBookedException extends Exception{

    public RoomAlreadyBookedException() {
        super( "The requested room is already booked for the specified period");
    }
}
