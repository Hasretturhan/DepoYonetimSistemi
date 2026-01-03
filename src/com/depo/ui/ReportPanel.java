package com.depo.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.depo.model.Product;
import com.depo.service.WarehouseService;

public class ReportPanel extends JPanel {

    private final WarehouseService service;

    private final AllStockTableModel stockModel = new AllStockTableModel();
    private final JTable stockTable = new JTable(stockModel);

    private final MovementTableModel movementModel = new MovementTableModel();
    private final JTable movementTable = new JTable(movementModel);

    public ReportPanel(WarehouseService service) {
        this.service = service;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Raporlar"));

        // Üst: stok tablosu
        stockTable.setRowHeight(24);
        stockTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Product p = stockModel.getAt(row);
                boolean critical = service.isCritical(p.getCode());
                if (!isSelected) {
                    c.setForeground(critical ? java.awt.Color.RED : java.awt.Color.BLACK);
                }
                return c;
            }
        });

        // Alt: hareket tablosu
        movementTable.setRowHeight(24);

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(stockTable),
                new JScrollPane(movementTable)
        );
        split.setResizeWeight(0.6); // üst %60, alt %40
        split.setDividerLocation(350);

        add(split, BorderLayout.CENTER);

        JLabel info = new JLabel("Üst: Stoklar (kritikler kırmızı) | Alt: Stok hareket geçmişi (giriş/çıkış kayıtları)");
        add(info, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        stockModel.setData(service.getAllProducts(), service);
        movementModel.setData(service.getMovements());
    }
}
