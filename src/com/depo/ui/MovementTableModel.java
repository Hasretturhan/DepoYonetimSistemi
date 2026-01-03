package com.depo.ui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.depo.model.StockMovement;

public class MovementTableModel extends AbstractTableModel {

    private final String[] columns = {"Tarih/Saat", "Ürün Kodu", "Tip", "Miktar", "Not"};
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private List<StockMovement> data = new ArrayList<>();

    public void setData(List<StockMovement> movements) {
        this.data = movements == null ? new ArrayList<>() : movements;
        fireTableDataChanged();
    }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int c) { return columns[c]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        StockMovement m = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> m.getDateTime().format(fmt);
            case 1 -> m.getProductCode();
            case 2 -> (m.getType() == StockMovement.Type.IN) ? "GİRİŞ" : "ÇIKIŞ";
            case 3 -> m.getAmount();
            case 4 -> m.getNote();
            default -> "";
        };
    }
}
