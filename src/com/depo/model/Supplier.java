package com.depo.model;

public class Supplier {
    private final String supplierId;   // benzersiz
    private String name;
    private String phone;
    private String address;

    public Supplier(String supplierId, String name, String phone, String address) {
        this.supplierId = supplierId;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getSupplierId() { return supplierId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return supplierId + " - " + name;
    }
}
