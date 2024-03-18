package com.gmail.kolyvas.hotelapp.exceptions;

public class EntityNotFoundException extends Exception{

    public EntityNotFoundException(String message) {
        super("The requested " + message + "wasnt't found");
    }
}
