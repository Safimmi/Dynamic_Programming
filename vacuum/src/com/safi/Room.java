package com.safi;

public class Room {

    private String name;
    private int dirtValue;

    Room(){}
    Room(String name, int value) {
        this.name = name;
        this.dirtValue = value;
    }

    //Getters
    public String getRoomName () {
        return name;
    }
    public int getDirtValue (){
        return dirtValue;
    }

    //Setters
    public void setName (String name) {
        this.name = name;
    }
    public void setDirtValue (int dirtValue) {
        this.dirtValue = dirtValue;
    }


}
