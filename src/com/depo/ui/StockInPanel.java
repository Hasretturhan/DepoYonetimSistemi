package com.depo.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.depo.exception.ValidationException;
import com.depo.model.Product;
import com.depo.service.WarehouseService;

public class StockInPanel extends JPanel {

    private final WarehouseService service;

    private final JComboBox<Product> cmbProducts = new JComboBox<>();
    private final JTextField txtAmount = new JTextField(10);
    private final JButton btnAdd = new JButton("Stok Girişi Yap");

    public StockInPanel(WarehouseService service) {
        this.service = service;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Stok Giriş"));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.anchor = GridBagConstraints.WEST;

        gc.gridx = 0; gc.gridy = 0;
        add(new JLabel("Ürün:"), gc);
        gc.gridx = 1;
        add(cmbProducts, gc);

        gc.gridx = 0; gc.gridy = 1;
        add(new JLabel("Miktar:"), gc);
        gc.gridx = 1;
        add(txtAmount, gc);

        gc.gridx = 1; gc.gridy = 2;
        add(btnAdd, gc);

        loadProducts();
        hookEvents();
    }

    private void loadProducts() {
        cmbProducts.removeAllItems();
        for (Product p : service.getAllProducts()) {
            cmbProducts.addItem(p);
        }
    }

    private void hookEvents() {
        btnAdd.addActionListener(e -> {
            try {
                Product p = (Product) cmbProducts.getSelectedItem();
                if (p == null) {
                    throw new ValidationException("Ürün seçilmedi.");
                }

                int amount;
                try {
                    amount = Integer.parseInt(txtAmount.getText().trim());
                } catch (NumberFormatException ex) {
                    throw new ValidationException("Miktar sayı olmalı.");
                }

                service.stockIn(p.getCode(), amount);

                JOptionPane.showMessageDialog(this,
                        "Stok girişi yapıldı.\nGüncel stok: " + service.getStock(p.getCode()),
                        "Başarılı",
                        JOptionPane.INFORMATION_MESSAGE);

                txtAmount.setText("");

            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    public void refreshProducts() {
        loadProducts();
    }

}
