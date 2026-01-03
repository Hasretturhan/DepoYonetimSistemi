package com.depo.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.depo.exception.DuplicateProductException;
import com.depo.exception.ValidationException;
import com.depo.model.Product;
import com.depo.service.WarehouseService;

public class ProductPanel extends JPanel {

    private final WarehouseService service;

    private final JTextField txtCode = new JTextField(14);
    private final JTextField txtName = new JTextField(14);
    private final JTextField txtCategory = new JTextField(14);
    private final JComboBox<String> cmbUnit = new JComboBox<>(new String[]{"adet", "kg", "paket", "koli"});
    private final JTextField txtCritical = new JTextField(14);
    private final JTextField txtLocation = new JTextField(14);

    private final JButton btnAdd = new JButton("Ürün Ekle");
    private final JButton btnUpdate = new JButton("Güncelle");
    private final JButton btnDelete = new JButton("Sil");
    private final JButton btnClear = new JButton("Temizle");

    private final ProductTableModel tableModel = new ProductTableModel();
    private final JTable table = new JTable(tableModel);

    public ProductPanel(WarehouseService service) {
        this.service = service;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildForm(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        hookEvents();
        refreshTable();
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Ürün Yönetimi"));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;

        int r = 0;

        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Ürün Kodu:"), gc);
        gc.gridx = 1; form.add(txtCode, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Ürün Adı:"), gc);
        gc.gridx = 1; form.add(txtName, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Kategori:"), gc);
        gc.gridx = 1; form.add(txtCategory, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Birim:"), gc);
        gc.gridx = 1; form.add(cmbUnit, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Kritik Seviye:"), gc);
        gc.gridx = 1; form.add(txtCritical, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Konum (Raf):"), gc);
        gc.gridx = 1; form.add(txtLocation, gc);

        r++;
        JPanel buttons = new JPanel();
        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnClear);

        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 2;
        form.add(buttons, gc);

        return form;
    }

    private JScrollPane buildTable() {
        table.setRowHeight(24);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Ürün Listesi"));
        sp.setPreferredSize(new Dimension(800, 350));
        return sp;
    }

    private void hookEvents() {
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            int row = table.getSelectedRow();
            if (row < 0) return;

            Product p = tableModel.getAt(row);
            if (p == null) return;

            txtCode.setText(p.getCode());
            txtCode.setEditable(false);

            txtName.setText(p.getName());
            txtCategory.setText(p.getCategory());
            cmbUnit.setSelectedItem(p.getUnit());
            txtCritical.setText(String.valueOf(p.getCriticalLevel()));
            txtLocation.setText(p.getLocation());
        });
    }

    private void onAdd() {
        try {
            Product p = readProductFromForm();

            service.addProduct(p);

            JOptionPane.showMessageDialog(this, "Ürün eklendi: " + p.getCode(),
                    "Başarılı", JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            refreshTable();

        } catch (ValidationException | DuplicateProductException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Beklenmeyen hata: " + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onUpdate() {
        try {
            String code = txtCode.getText().trim();
            if (code.isEmpty()) {
                throw new ValidationException("Güncellemek için tablodan bir ürün seçmelisin.");
            }

            Product updated = readProductFromForm(); // code değişmiyor zaten
            service.updateProduct(updated);

            JOptionPane.showMessageDialog(this, "Ürün güncellendi: " + updated.getCode(),
                    "Başarılı", JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            refreshTable();

        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Beklenmeyen hata: " + ex.getMessage(), "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        try {
            String code = txtCode.getText().trim();
            if (code.isEmpty()) {
                throw new ValidationException("Silmek için tablodan bir ürün seçmelisin.");
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Seçili ürünü silmek istediğine emin misin?\nÜrün Kodu: " + code,
                    "Silme Onayı",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) return;

            service.deleteProduct(code);

            JOptionPane.showMessageDialog(this, "Ürün silindi: " + code,
                    "Başarılı", JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            refreshTable();

        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Beklenmeyen hata: " + ex.getMessage(), "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Product readProductFromForm() {
        String code = txtCode.getText().trim();
        String name = txtName.getText().trim();
        String category = txtCategory.getText().trim();
        String unit = (String) cmbUnit.getSelectedItem();
        String location = txtLocation.getText().trim();

        int critical;
        try {
            critical = Integer.parseInt(txtCritical.getText().trim());
        } catch (NumberFormatException ex) {
            throw new ValidationException("Kritik seviye sayı olmalı (örn: 5).");
        }

        return new Product(code, name, category, unit, critical, location);
    }

    private void refreshTable() {
        tableModel.setData(service.getAllProducts());
    }

    private void clearForm() {
        txtCode.setText("");
        txtName.setText("");
        txtCategory.setText("");
        txtCritical.setText("");
        txtLocation.setText("");
        cmbUnit.setSelectedIndex(0);

        txtCode.setEditable(true);
        table.clearSelection();

        txtCode.requestFocus();
    }
}
