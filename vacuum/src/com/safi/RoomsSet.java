package com.safi;

public class RoomsSet {

    private String path;
    private int nRoom;
    private Room[] rooms;

    RoomsSet(){}
    RoomsSet(String dir, int n){
        this.path = dir;
        this.nRoom = n;
        rooms = new Room[nRoom];
    }

    //Getters
    public String getPath (){
        return this.path;
    }
    public int getnRoom (){
        return this.nRoom;
    }
    public Room[] getRooms () {
        return this.rooms;
    }

    public int[] getDirtValuesArray () {
        int [] dirt  = new int [nRoom];
        for (int i = 0; i<this.nRoom; i++){
            dirt[i] = rooms[i].getDirtValue();
        }
        return dirt;
    }
    public String getRoomNameByIndex(int i){
        return this.rooms[i].getRoomName();
    }
    public int getDirtValueByIndex(int i){
        return this.rooms[i].getDirtValue();
    }

    //Setters
    public void setAll (String path, int nRoom, Room[] rooms){
        this.path = path;
        this.nRoom = nRoom;
        this.rooms = rooms;
    }
    public void setPath (String path){
        this.path = path;
    }
    public void setnRoom (int n) {
        this.nRoom = n;
    }

    public void initRoomByIndex (String name, int dirtValue, int i){
        rooms[i] = new Room(name, dirtValue);
    }
    public void setRoomNameByIndex (String value, int i){
        this.rooms[i].setName(value);
    }
    public void setDirtValueByIndex (int value, int i){
        this.rooms[i].setDirtValue(value);
    }

}
