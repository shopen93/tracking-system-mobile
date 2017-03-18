package pl.lodz.trackingsystem.models;

import java.util.List;

/**
 * Created by Mateusz on 18.03.2017.
 */

public class User {

    private int id;

    private String name;

    private List<Coordinates> coords;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Coordinates> getCoords() {
        return coords;
    }

    public void setCoords(List<Coordinates> coords) {
        this.coords = coords;
    }

}
