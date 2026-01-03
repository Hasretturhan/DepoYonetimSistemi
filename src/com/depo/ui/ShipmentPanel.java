package com.depo.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import com.depo.exception.DuplicateShipmentException;
import com.depo.exception.InsufficientStockException;
import com.depo.exception.ValidationException;
import com.depo.model.Product;
import com.depo.model.Shipment;
import com.depo.model.Supplier;
import com.depo.service.WarehouseService;

public class ShipmentPanel extends JPanel {

    private final WarehouseService service;

    private final JTextField txtShipmentNo = new JTextField(14);
    private final JComboBox<String> cmbType = new JComboBox<>(new String[]{"GİRİŞ (INBOUND)", "ÇIKIŞ (OUTBOUND)"});
    private final JComboBox<Product> cmbProduct = new JComboBox<>();
    private final JComboBox<Supplier> cmbSupplier = new JComboBox<>();
    private final JTextField txtAmount = new JTextField(10);
    private final JTextField txtNote = new JTextField(14);

    private final JButton btnAdd = new JButton("Sevkiyat Kaydet");
    private final JButton btnClear = new JButton("Temizle");

    private final ShipmentTableModel model = new ShipmentTableModel();
    private final JTable table = new JTable(model);

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public ShipmentPanel(WarehouseService service) {
        this.service = service;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildForm(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.setRowHeight(24);

        refresh();
        hook();
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Sevkiyat Kaydı"));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;

        int r = 0;

        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Sevkiyat No:"), gc);
        gc.gridx = 1; p.add(txtShipmentNo, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Tip:"), gc);
        gc.gridx = 1; p.add(cmbType, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Ürün:"), gc);
        gc.gridx = 1; p.add(cmbProduct, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Tedarikçi:"), gc);
        gc.gridx = 1; p.add(cmbSupplier, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Miktar:"), gc);
        gc.gridx = 1; p.add(txtAmount, gc);

        r++;
        gc.gridx = 0; gc.gridy = r; p.add(new JLabel("Not:"), gc);
        gc.gridx = 1; p.add(txtNote, gc);

        r++;
        JPanel btns = new JPanel();
        btns.add(btnAdd);
        btns.add(btnClear);

        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 2;
        p.add(btns, gc);

        return p;
    }

    private void hook() {
        btnAdd.addActionListener(e -> onAdd());
        btnClear.addActionListener(e -> clear());
    }

    private void onAdd() {
        try {
            String no = txtShipmentNo.getText().trim();
            int amount;
            try {
                amount = Integer.parseInt(txtAmount.getText().trim());
            } catch (NumberFormatException ex) {
                throw new ValidationException("Miktar sayı olmalı.");
            }

            Product product = (Product) cmbProduct.getSelectedItem();
            Supplier supplier = (Supplier) cmbSupplier.getSelectedItem();
            if (product == null) throw new ValidationException("Ürün seçilmedi.");
            if (supplier == null) throw new ValidationException("Tedarikçi seçilmedi.");

            Shipment.Type type = (cmbType.getSelectedIndex() == 0) ? Shipment.Type.INBOUND : Shipment.Type.OUTBOUND;

            Shipment sh = new Shipment(
                    no,
                    LocalDateTime.now(),
                    type,
                    product.getCode(),
                    amount,
                    supplier.getSupplierId(),
                    txtNote.getText().trim()
            );

            service.addShipment(sh);

            JOptionPane.showMessageDialog(this,
                    "Sevkiyat kaydedildi.\n" +
                    "Tarih: " + sh.getDateTime().format(fmt) + "\n" +
                    "Güncel stok: " + service.getStock(product.getCode()),
                    "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);

            clear();
            refresh();

        } catch (ValidationException | DuplicateShipmentException | InsufficientStockException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Beklenmeyen hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clear() {
        txtShipmentNo.setText("");
        txtAmount.setText("");
        txtNote.setText("");
        txtShipmentNo.requestFocus();
    }

    public void refresh() {
        cmbProduct.removeAllItems();
        for (Product p : service.getAllProducts()) cmbProduct.addItem(p);

        cmbSupplier.removeAllItems();
        for (Supplier s : service.getAllSuppliers()) cmbSupplier.addItem(s);

        model.setData(service.getAllShipments());
    }

    private static class ShipmentTableModel extends AbstractTableModel {
        private final String[] cols = {"Sevkiyat No", "Tip", "Ürün Kodu", "Miktar", "Tedarikçi ID", "Not"};
        private java.util.List<Shipment> data = new java.util.ArrayList<>();

        public void setData(java.util.List<Shipment> shipments) {
            this.data = shipments == null ? new java.util.ArrayList<>() : shipments;
            fireTableDataChanged();
        }

        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override
        public Object getValueAt(int row, int col) {
            Shipment sh = data.get(row);
            return switch (col) {
                case 0 -> sh.getShipmentNo();
                case 1 -> (sh.getType() == Shipment.Type.INBOUND) ? "GİRİŞ" : "ÇIKIŞ";
                case 2 -> sh.getProductCode();
                case 3 -> sh.getAmount();
                case 4 -> sh.getSupplierId();
                case 5 -> sh.getNote();
                default -> "";
            };
        }
    }
}
