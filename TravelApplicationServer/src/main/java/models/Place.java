package models;

public class Place {
    private String name;
    private String description;
    private int ID;
    public Place(String name, String description, int ID) {
        this.name = name;
        this.description = description;
        this.ID = ID;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getID() {
        return ID;
    }
}
