package com.depo.model;

public class Product {
    private final String code;   // benzersiz
    private String name;
    private String category;
    private String unit;
    private int criticalLevel;
    private String location;

    public Product(String code, String name, String category, String unit, int criticalLevel, String location) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.criticalLevel = criticalLevel;
        this.location = location;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getUnit() { return unit; }
    public int getCriticalLevel() { return criticalLevel; }
    public String getLocation() { return location; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setCriticalLevel(int criticalLevel) { this.criticalLevel = criticalLevel; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
