package com.depo.model;

import java.time.LocalDateTime;

public class Shipment {

    public enum Type { INBOUND, OUTBOUND } // Giriş sevkiyatı / Çıkış sevkiyatı

    private final String shipmentNo; // benzersiz
    private final LocalDateTime dateTime;
    private final Type type;

    private final String productCode;
    private final int amount;
    private final String supplierId; // tedarikçi
    private final String note;

    public Shipment(String shipmentNo, LocalDateTime dateTime, Type type,
                    String productCode, int amount, String supplierId, String note) {
        this.shipmentNo = shipmentNo;
        this.dateTime = dateTime;
        this.type = type;
        this.productCode = productCode;
        this.amount = amount;
        this.supplierId = supplierId;
        this.note = note;
    }

    public String getShipmentNo() { return shipmentNo; }
    public LocalDateTime getDateTime() { return dateTime; }
    public Type getType() { return type; }
    public String getProductCode() { return productCode; }
    public int getAmount() { return amount; }
    public String getSupplierId() { return supplierId; }
    public String getNote() { return note; }
}
