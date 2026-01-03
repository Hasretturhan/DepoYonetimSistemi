package com.depo.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.depo.service.WarehouseService;

public class MainFrame extends JFrame {

    private final WarehouseService service = new WarehouseService();

    public MainFrame() {
        setTitle("Depo Yönetim Sistemi");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        ProductPanel productPanel = new ProductPanel(service);
        StockInPanel stockInPanel = new StockInPanel(service);
        StockOutPanel stockOutPanel = new StockOutPanel(service);
        ReportPanel reportPanel = new ReportPanel(service);
        SupplierPanel supplierPanel = new SupplierPanel(service);
        ShipmentPanel shipmentPanel = new ShipmentPanel(service);


        tabbedPane.addTab("Ürün Yönetimi", productPanel);
        tabbedPane.addTab("Stok Giriş", stockInPanel);
        tabbedPane.addTab("Stok Çıkış", stockOutPanel);
        tabbedPane.addTab("Raporlar", reportPanel);
        tabbedPane.addTab("Tedarikçi", supplierPanel);
        tabbedPane.addTab("Sevkiyat", shipmentPanel);


        // 🔥 SEKME DEĞİŞİNCE YENİLE
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = tabbedPane.getSelectedIndex();
                String title = tabbedPane.getTitleAt(index);

                if ("Stok Giriş".equals(title)) {
                    stockInPanel.refreshProducts();
                }
                if ("Stok Çıkış".equals(title)) {
                    stockOutPanel.refreshProducts();
                }
                if ("Raporlar".equals(title)) {
                    reportPanel.refresh();
                }
                if ("Tedarikçi".equals(title)) {
                    supplierPanel.refresh();
                }
                if ("Sevkiyat".equals(title)) {
                    shipmentPanel.refresh();
                }
            }
        });

        add(tabbedPane);
    }
}
