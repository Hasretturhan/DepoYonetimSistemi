package com.depo.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.depo.model.Product;
import com.depo.service.WarehouseService;

public class AllStockTableModel extends AbstractTableModel {

    private final String[] columns = {"Kod", "Ad", "Kategori", "Konum", "Mevcut Stok", "Kritik Seviye", "Durum"};
    private final List<Product> data = new ArrayList<>();
    private WarehouseService service;

    public void setData(List<Product> products, WarehouseService service) {
        this.data.clear();
        if (products != null) this.data.addAll(products);
        this.service = service;
        fireTableDataChanged();
    }

    public Product getAt(int row) {
        return (row < 0 || row >= data.size()) ? null : data.get(row);
    }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int c) { return columns[c]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product p = data.get(rowIndex);
        int stock = service.getStock(p.getCode());
        int critical = p.getCriticalLevel();

        return switch (columnIndex) {
            case 0 -> p.getCode();
            case 1 -> p.getName();
            case 2 -> p.getCategory();
            case 3 -> p.getLocation();
            case 4 -> stock;
            case 5 -> critical;
            case 6 -> (stock <= critical) ? "KRİTİK" : "Normal";
            default -> "";
        };
    }
}
