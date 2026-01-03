package com.depo.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.depo.model.Product;

public class ProductTableModel extends AbstractTableModel {

    private final String[] columns = {"Kod", "Ad", "Kategori", "Birim", "Kritik", "Konum"};
    private List<Product> data = new ArrayList<>();

    public void setData(List<Product> products) {
        this.data = products == null ? new ArrayList<>() : products;
        fireTableDataChanged();
    }

    public Product getAt(int row) {
        if (row < 0 || row >= data.size()) return null;
        return data.get(row);
    }

    @Override
    public int getRowCount() { return data.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public String getColumnName(int column) { return columns[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product p = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> p.getCode();
            case 1 -> p.getName();
            case 2 -> p.getCategory();
            case 3 -> p.getUnit();
            case 4 -> p.getCriticalLevel();
            case 5 -> p.getLocation();
            default -> "";
        };
    }
}
