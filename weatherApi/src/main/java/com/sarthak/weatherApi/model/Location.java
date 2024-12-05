package com.sarthak.weatherApi.model;

import java.util.Objects;

public class Location {
    private String name; // City name
    private double lat; // Latitude
    private double lon; // Longitude
    private String country; // Country code

    // Default constructor
    public Location() {
    }

    // Parameterized constructor
    public Location(String name, double lat, double lon, String country) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Override toString method
    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", country='" + country + '\'' +
                '}';
    }

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.lat, lat) == 0 &&
                Double.compare(location.lon, lon) == 0 &&
                Objects.equals(name, location.name) &&
                Objects.equals(country, location.country);
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(name, lat, lon, country);
    }
}
