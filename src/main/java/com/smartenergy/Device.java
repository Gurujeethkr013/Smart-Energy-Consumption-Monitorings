package com.smartenergy;

public class Device {
    private int id;
    private String name;
    private String category;
    private int watts;
    private String status;

    public Device(int id, String name, String category, int watts, String status) {
        this.id = id; this.name = name; this.category = category;
        this.watts = watts; this.status = status;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getWatts() { return watts; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
