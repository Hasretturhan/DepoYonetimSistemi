package com.depo.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.depo.exception.DuplicateProductException;
import com.depo.exception.InsufficientStockException;
import com.depo.exception.ValidationException;
import com.depo.model.Product;

public class WarehouseService {

    private final Map<String, Product> products = new LinkedHashMap<>();
    private final Map<String, Integer> stock = new LinkedHashMap<>();

    private final List<com.depo.model.StockMovement> movements = new ArrayList<>();
    private final Map<String, com.depo.model.Supplier> suppliers = new LinkedHashMap<>();
    private final Map<String, com.depo.model.Shipment> shipments = new LinkedHashMap<>();

    public List<com.depo.model.StockMovement> getMovements() {
        return new ArrayList<>(movements);
    }

    // =========================
    // PRODUCT CRUD
    // =========================
    public void addProduct(Product p) {
        validate(p);

        if (products.containsKey(p.getCode())) {
            throw new DuplicateProductException("Bu ürün kodu zaten kayıtlı: " + p.getCode());
        }
        products.put(p.getCode(), p);
        stock.put(p.getCode(), 0);
    }

    public void updateProduct(Product updated) {
        if (updated == null) throw new ValidationException("Ürün boş olamaz.");
        if (isBlank(updated.getCode())) throw new ValidationException("Ürün kodu boş olamaz.");

        if (!products.containsKey(updated.getCode())) {
            throw new ValidationException("Güncellenecek ürün bulunamadı: " + updated.getCode());
        }

        validate(updated);

        Product p = products.get(updated.getCode());
        p.setName(updated.getName());
        p.setCategory(updated.getCategory());
        p.setUnit(updated.getUnit());
        p.setCriticalLevel(updated.getCriticalLevel());
        p.setLocation(updated.getLocation());
    }

    public void deleteProduct(String productCode) {
        if (isBlank(productCode)) throw new ValidationException("Ürün kodu boş olamaz.");
        if (!products.containsKey(productCode)) {
            throw new ValidationException("Silinecek ürün bulunamadı: " + productCode);
        }

        int currentStock = stock.getOrDefault(productCode, 0);
        if (currentStock > 0) {
            throw new ValidationException("Stokta miktar varken ürün silinemez. Mevcut stok: " + currentStock);
        }

        products.remove(productCode);
        stock.remove(productCode);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    // =========================
    // REPORTS
    // =========================
    public List<Product> getCriticalProducts() {
        List<Product> criticals = new ArrayList<>();
        for (Product p : products.values()) {
            int current = stock.getOrDefault(p.getCode(), 0);
            if (current <= p.getCriticalLevel()) {
                criticals.add(p);
            }
        }
        return criticals;
    }

    public int getCriticalLevel(String productCode) {
        Product p = products.get(productCode);
        return p == null ? 0 : p.getCriticalLevel();
    }

    public boolean isCritical(String productCode) {
        Product p = products.get(productCode);
        if (p == null) return false;
        return getStock(productCode) <= p.getCriticalLevel();
    }

    // =========================
    // STOCK
    // =========================
    public void stockIn(String productCode, int amount) {
        if (!products.containsKey(productCode)) {
            throw new ValidationException("Ürün bulunamadı.");
        }
        if (amount <= 0) {
            throw new ValidationException("Giriş miktarı 0'dan büyük olmalı.");
        }

        stock.put(productCode, stock.get(productCode) + amount);

        movements.add(new com.depo.model.StockMovement(
                java.time.LocalDateTime.now(),
                productCode,
                com.depo.model.StockMovement.Type.IN,
                amount,
                "Stok girişi"
        ));
    }

    public void stockOut(String productCode, int amount) {
        if (!products.containsKey(productCode)) {
            throw new ValidationException("Ürün bulunamadı.");
        }
        if (amount <= 0) {
            throw new ValidationException("Çıkış miktarı 0'dan büyük olmalı.");
        }

        int current = stock.getOrDefault(productCode, 0);
        if (amount > current) {
            throw new InsufficientStockException(
                    "Yetersiz stok. Mevcut: " + current + ", İstenen: " + amount
            );
        }

        stock.put(productCode, current - amount);

        movements.add(new com.depo.model.StockMovement(
                java.time.LocalDateTime.now(),
                productCode,
                com.depo.model.StockMovement.Type.OUT,
                amount,
                "Stok çıkışı"
        ));
    }

    public int getStock(String productCode) {
        return stock.getOrDefault(productCode, 0);
    }

    // =========================
    // SUPPLIER
    // =========================
    public void addSupplier(com.depo.model.Supplier s) {
        if (s == null) throw new com.depo.exception.ValidationException("Tedarikçi boş olamaz.");
        if (isBlank(s.getSupplierId())) throw new com.depo.exception.ValidationException("Tedarikçi ID boş olamaz.");
        if (isBlank(s.getName())) throw new com.depo.exception.ValidationException("Tedarikçi adı boş olamaz.");

        if (suppliers.containsKey(s.getSupplierId())) {
            throw new com.depo.exception.DuplicateSupplierException("Bu tedarikçi ID zaten kayıtlı: " + s.getSupplierId());
        }
        suppliers.put(s.getSupplierId(), s);
    }

    public List<com.depo.model.Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    // =========================
    // SHIPMENT
    // =========================
    public void addShipment(com.depo.model.Shipment sh) {
        if (sh == null) throw new com.depo.exception.ValidationException("Sevkiyat boş olamaz.");
        if (isBlank(sh.getShipmentNo())) throw new com.depo.exception.ValidationException("Sevkiyat no boş olamaz.");
        if (sh.getAmount() <= 0) throw new com.depo.exception.ValidationException("Sevkiyat miktarı 0'dan büyük olmalı.");
        if (!products.containsKey(sh.getProductCode())) throw new com.depo.exception.ValidationException("Sevkiyat ürünü bulunamadı.");
        if (!suppliers.containsKey(sh.getSupplierId())) throw new com.depo.exception.ValidationException("Tedarikçi bulunamadı.");

        if (shipments.containsKey(sh.getShipmentNo())) {
            throw new com.depo.exception.DuplicateShipmentException("Bu sevkiyat no zaten kayıtlı: " + sh.getShipmentNo());
        }

        shipments.put(sh.getShipmentNo(), sh);

        if (sh.getType() == com.depo.model.Shipment.Type.INBOUND) {
            stockIn(sh.getProductCode(), sh.getAmount());
        } else {
            stockOut(sh.getProductCode(), sh.getAmount());
        }
    }

    public List<com.depo.model.Shipment> getAllShipments() {
        return new ArrayList<>(shipments.values());
    }

    // =========================
    // VALIDATION HELPERS
    // =========================
    private void validate(Product p) {
        if (p == null) throw new ValidationException("Ürün boş olamaz.");
        if (isBlank(p.getCode())) throw new ValidationException("Ürün kodu boş olamaz.");
        if (isBlank(p.getName())) throw new ValidationException("Ürün adı boş olamaz.");
        if (isBlank(p.getCategory())) throw new ValidationException("Kategori boş olamaz.");
        if (isBlank(p.getUnit())) throw new ValidationException("Birim boş olamaz.");
        if (p.getCriticalLevel() < 0) throw new ValidationException("Kritik seviye 0 veya daha büyük olmalı.");
        if (isBlank(p.getLocation())) throw new ValidationException("Konum boş olamaz.");
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
