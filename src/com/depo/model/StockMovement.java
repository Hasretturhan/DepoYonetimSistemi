package com.depo.model;

import java.time.LocalDateTime;

public class StockMovement {

    public enum Type { IN, OUT }

    private final LocalDateTime dateTime;
    private final String productCode;
    private final Type type;
    private final int amount;
    private final String note;

    public StockMovement(LocalDateTime dateTime, String productCode, Type type, int amount, String note) {
        this.dateTime = dateTime;
        this.productCode = productCode;
        this.type = type;
        this.amount = amount;
        this.note = note;
    }

    public LocalDateTime getDateTime() { return dateTime; }
    public String getProductCode() { return productCode; }
    public Type getType() { return type; }
    public int getAmount() { return amount; }
    public String getNote() { return note; }
}
