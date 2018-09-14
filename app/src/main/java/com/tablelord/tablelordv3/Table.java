package com.tablelord.tablelordv3;

public class Table {
    private String tableNumber;
    private long tableSeats;
    private String roomNumber;
    private boolean tableOccupied;


    public Table(String tableNumber, long tableSeats, String roomNumber, boolean tableOccupied) {
        this.tableNumber = tableNumber;
        this.tableSeats = tableSeats;
        this.roomNumber = roomNumber;
        this.tableOccupied = tableOccupied;
    }

    public Table() {
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public long getTableSeats() {
        return tableSeats;
    }

    public void setTableSeats(long tableSeats) {
        this.tableSeats = tableSeats;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isTableOccupied() {
        return tableOccupied;
    }

    public void setTableOccupied(boolean tableOccupied) {
        this.tableOccupied = tableOccupied;
    }

    public String stringIsTableOccupied(){
        String tableOccupiedString = Boolean.toString(this.tableOccupied);
        return tableOccupiedString;
    }
}
