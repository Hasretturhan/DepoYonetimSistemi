package com.depo.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.depo.model.Product;

public class CriticalTableModel extends AbstractTableModel {

    private final String[] columns = {"Kod", "Ad", "Mevcut Stok", "Kritik Seviye", "Konum"};
    private List<Object[]> data = new ArrayList<>();

    public void setData(List<Product> products, com.depo.service.WarehouseService service) {
        data.clear();
        if (products != null) {
            for (Product p : products) {
                data.add(new Object[] {
                        p.getCode(),
                        p.getName(),
                        service.getStock(p.getCode()),
                        p.getCriticalLevel(),
                        p.getLocation()
                });
            }
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() { return data.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public String getColumnName(int column) { return columns[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }
}
