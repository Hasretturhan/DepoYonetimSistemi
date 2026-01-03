package com.depo.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import com.depo.exception.DuplicateSupplierException;
import com.depo.exception.ValidationException;
import com.depo.model.Supplier;
import com.depo.service.WarehouseService;

public class SupplierPanel extends JPanel {

    private final WarehouseService service;

    private final JTextField txtId = new JTextField(14);
    private final JTextField txtName = new JTextField(14);
    private final JTextField txtPhone = new JTextField(14);
    private final JTextField txtAddress = new JTextField(14);
    private final JButton btnAdd = new JButton("Tedarikçi Ekle");
    private final JButton btnClear = new JButton("Temizle");

    private final SupplierTableModel model = new SupplierTableModel();
    private final JTable table = new JTable(model);

    public SupplierPanel(WarehouseService service) {
        this.service = service;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildForm(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.setRowHeight(24);
        hook();
        refresh();
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Tedarikçi Ekle"));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;

        int r = 0;
        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Tedarikçi ID:"), gc);
        gc.gridx = 1; p.add(txtId, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Ad:"), gc);
        gc.gridx = 1; p.add(txtName, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Telefon:"), gc);
        gc.gridx = 1; p.add(txtPhone, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Adres:"), gc);
        gc.gridx = 1; p.add(txtAddress, gc);

        r++;
        JPanel btns = new JPanel();
        btns.add(btnAdd);
        btns.add(btnClear);

        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 2;
        p.add(btns, gc);

        return p;
    }

    private void hook() {
        btnAdd.addActionListener(e -> {
            try {
                Supplier s = new Supplier(
                        txtId.getText().trim(),
                        txtName.getText().trim(),
                        txtPhone.getText().trim(),
                        txtAddress.getText().trim()
                );
                service.addSupplier(s);
                JOptionPane.showMessageDialog(this, "Tedarikçi eklendi: " + s.getSupplierId(),
                        "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                clear();
                refresh();
            } catch (ValidationException | DuplicateSupplierException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Beklenmeyen hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnClear.addActionListener(e -> clear());
    }

    private void clear() {
        txtId.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtId.requestFocus();
    }

    public void refresh() {
        model.setData(service.getAllSuppliers());
    }

    private static class SupplierTableModel extends AbstractTableModel {
        private final String[] cols = {"ID", "Ad", "Telefon", "Adres"};
        private java.util.List<Supplier> data = new java.util.ArrayList<>();

        public void setData(java.util.List<Supplier> suppliers) {
            this.data = suppliers == null ? new java.util.ArrayList<>() : suppliers;
            fireTableDataChanged();
        }

        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override
        public Object getValueAt(int row, int col) {
            Supplier s = data.get(row);
            return switch (col) {
                case 0 -> s.getSupplierId();
                case 1 -> s.getName();
                case 2 -> s.getPhone();
                case 3 -> s.getAddress();
                default -> "";
            };
        }
    }
}
