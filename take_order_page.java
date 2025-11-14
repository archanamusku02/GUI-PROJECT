package restaurant_package;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

// iText imports
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.Image;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Font;

public class take_order_page extends JFrame implements ActionListener {

    JTable menuTable, orderTable, tables;
    DefaultTableModel menuModel, orderModel, tableModel;
    JButton addToOrderBtn, removeOrderBtn, generateBillBtn, assignTableBtn;
    JLabel totalLabel, taxLabel, discountLabel, finalLabel, selectedTableLabel;
    Connection conn;

    String selectedTableNumber = null;

    public take_order_page() {
        setTitle("Restaurant Order Page");
        setSize(1700, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(235, 245, 255));

        // ---------- Menu Table ----------
        String[] menuColumns = {"Item Name", "Item Price", "Quantity", "Available"};
        menuModel = new DefaultTableModel(menuColumns, 0);
        menuTable = new JTable(menuModel);
        JScrollPane menuScroll = new JScrollPane(menuTable);
        menuScroll.setBounds(50, 60, 500, 500);
        add(menuScroll);

        JLabel menuLabel = new JLabel("Menu Items");
        menuLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 20));
        menuLabel.setBounds(50, 20, 200, 30);
        add(menuLabel);

        // ---------- Order Table ----------
        String[] orderColumns = {"Item Name", "Item Price"};
        orderModel = new DefaultTableModel(orderColumns, 0);
        orderTable = new JTable(orderModel);
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setBounds(790, 60, 500, 250);
        add(orderScroll);

        JLabel orderLabel = new JLabel("Order List");
        orderLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 20));
        orderLabel.setBounds(790, 20, 200, 30);
        add(orderLabel);

        // ---------- Tables Table ----------
        String[] tableColumns = {"Table Number", "Seats", "Status"};
        tableModel = new DefaultTableModel(tableColumns, 0);
        tables = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(tables);
        tableScroll.setBounds(790, 350, 500, 250);
        add(tableScroll);

        JLabel tableLabel = new JLabel("Table List");
        tableLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 20));
        tableLabel.setBounds(790, 310, 200, 30);
        add(tableLabel);

        // ---------- Buttons ----------
        addToOrderBtn = new JButton("Add Selected Items ➜");
        addToOrderBtn.setBounds(570, 150, 200, 40);
        addToOrderBtn.setBackground(new Color(90, 150, 250));
        addToOrderBtn.setForeground(Color.WHITE);
        addToOrderBtn.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 15));
        add(addToOrderBtn);

        removeOrderBtn = new JButton("Remove Selected Item");
        removeOrderBtn.setBounds(570, 200, 200, 40);
        removeOrderBtn.setBackground(new Color(230, 90, 80));
        removeOrderBtn.setForeground(Color.WHITE);
        removeOrderBtn.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 15));
        add(removeOrderBtn);

        generateBillBtn = new JButton("Generate Bill (PDF)");
        generateBillBtn.setBounds(570, 250, 200, 40);
        generateBillBtn.setBackground(new Color(0, 130, 80));
        generateBillBtn.setForeground(Color.WHITE);
        generateBillBtn.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 15));
        add(generateBillBtn);

        assignTableBtn = new JButton("Assign Table");
        assignTableBtn.setBounds(570, 300, 200, 40);
        assignTableBtn.setBackground(new Color(120, 80, 200));
        assignTableBtn.setForeground(Color.WHITE);
        assignTableBtn.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 15));
        add(assignTableBtn);

        selectedTableLabel = new JLabel("Selected Table: None");
        selectedTableLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 15));
        selectedTableLabel.setBounds(570, 360, 300, 30);
        add(selectedTableLabel);

        // ---------- Bill Labels ----------
        totalLabel = new JLabel("Total: ₹0.00");
        taxLabel = new JLabel("Tax (5%): ₹0.00");
        discountLabel = new JLabel("Discount (10%): ₹0.00");
        finalLabel = new JLabel("Final Bill: ₹0.00");

        totalLabel.setBounds(570, 420, 300, 30);
        taxLabel.setBounds(570, 460, 300, 30);
        discountLabel.setBounds(570, 500, 300, 30);
        finalLabel.setBounds(570, 540, 300, 30);

        java.awt.Font labelFont = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 15);
        totalLabel.setFont(labelFont);
        taxLabel.setFont(labelFont);
        discountLabel.setFont(labelFont);
        finalLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        finalLabel.setForeground(new Color(0, 120, 60));

        add(totalLabel);
        add(taxLabel);
        add(discountLabel);
        add(finalLabel);

        // ---------- Actions ----------
        addToOrderBtn.addActionListener(this);
        removeOrderBtn.addActionListener(this);
        generateBillBtn.addActionListener(this);
        assignTableBtn.addActionListener(this);

        // ---------- Database ----------
        connectDatabase();
        loadMenuItems();
        loadTableList();
    }

    private void connectDatabase() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "Rahul", "root");
            System.out.println("✅ Database Connected!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMenuItems() {
        try {
            if (conn == null) return;
            menuModel.setRowCount(0);
            String query = "SELECT item_name, item_price, quantity, available FROM Restaurant_items";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                menuModel.addRow(new Object[]{
                        rs.getString("item_name"),
                        rs.getDouble("item_price"),
                        rs.getInt("quantity"),
                        rs.getString("available")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load menu items: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableList() {
        try {
            if (conn == null) return;
            tableModel.setRowCount(0);
            String query = "SELECT table_number, seats, status FROM Restaurant_tables";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("table_number"),
                        rs.getInt("seats"),
                        rs.getString("status")
                });
               
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load table list: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();

        if (src == addToOrderBtn) {
            if (selectedTableNumber == null) {
                JOptionPane.showMessageDialog(this, "Please assign a table before taking order!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int[] selectedRows = menuTable.getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Please select items from menu!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (int row : selectedRows) {
                String name = menuModel.getValueAt(row, 0).toString();
                double price = Double.parseDouble(menuModel.getValueAt(row, 1).toString());
                orderModel.addRow(new Object[]{name, price});
            }
        }

        else if (src == removeOrderBtn) {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an item to remove!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            orderModel.removeRow(selectedRow);
        }

        else if (src == assignTableBtn) {
            int row = tables.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a table to assign!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String tableNum = tableModel.getValueAt(row, 0).toString();
            String status = tableModel.getValueAt(row, 2).toString();

            if (status.equalsIgnoreCase("Occupied")) {
                JOptionPane.showMessageDialog(this, "This table is already occupied!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            selectedTableNumber = tableNum;
            selectedTableLabel.setText("Selected Table: " + selectedTableNumber);

            try {
                PreparedStatement ps = conn.prepareStatement("UPDATE Restaurant_tables SET status = 'Occupied' WHERE table_number = ?");
                ps.setString(1, tableNum);
                ps.executeUpdate();
                loadTableList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
// Generate bill button
       
        	else if (src == generateBillBtn) {
        	    if (orderModel.getRowCount() == 0) {
        	        JOptionPane.showMessageDialog(this, "No items selected for billing!", "Error", JOptionPane.ERROR_MESSAGE);
        	        return;
        	    }

        	    double total = 0;
        	    for (int i = 0; i < orderModel.getRowCount(); i++) {
        	        total += Double.parseDouble(orderModel.getValueAt(i, 1).toString());
        	    }

        	    double tax = total * 0.05;
        	    double discount = total * 0.10;
        	    double finalBill = total + tax - discount;

        	    totalLabel.setText("Total: ₹" + String.format("%.2f", total));
        	    taxLabel.setText("Tax (5%): ₹" + String.format("%.2f", tax));
        	    discountLabel.setText("Discount (10%): ₹" + String.format("%.2f", discount));
        	    finalLabel.setText("Final Bill: ₹" + String.format("%.2f", finalBill));

        	    try {
        	        // ✅ Insert bill details into DB
        	        PreparedStatement insertBill = conn.prepareStatement(
        	            "INSERT INTO RESTAURANT_BILLS (TABLE_NUMBER, BILL_DATE, SUBTOTAL, TAX, DISCOUNT, FINAL_TOTAL) VALUES (?, SYSDATE, ?, ?, ?, ?)"
        	        );
        	        insertBill.setString(1, selectedTableNumber);
        	        insertBill.setDouble(2, total);
        	        insertBill.setDouble(3, tax);
        	        insertBill.setDouble(4, discount);
        	        insertBill.setDouble(5, finalBill);
        	        insertBill.executeUpdate();

        	        // ✅ PDF Generation
        	        String fileName = "RusticRoots_Bill_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
        	        Document document = new Document();
        	        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        	        document.open();

        	        com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance("C:\\Users\\HP\\eclipse-workspace\\Restaurant_project\\assets\\logo.jpg");
        	        logo.scaleAbsolute(80f, 80f);
        	        logo.setAlignment(Element.ALIGN_LEFT);
        	        document.add(logo);

        	        Paragraph title = new Paragraph("RusticRoots Kitchen",
        	                new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD));
        	        title.setAlignment(Element.ALIGN_CENTER);
        	        document.add(title);
        	        Paragraph address = new Paragraph("Uppal, Pillar Number 123\nPh: 9502996651\n\n",
        	                new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL));
        	        address.setAlignment(Element.ALIGN_CENTER);
        	        document.add(address);

        	        document.add(new Paragraph("Table No: " + selectedTableNumber));
        	        document.add(new Paragraph("Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())));
        	        document.add(new Paragraph("\nItems:"));

        	        for (int i = 0; i < orderModel.getRowCount(); i++) {
        	            String item = orderModel.getValueAt(i, 0).toString();
        	            double price = Double.parseDouble(orderModel.getValueAt(i, 1).toString());
        	            document.add(new Paragraph(String.format("  %-25s ₹%.2f", item, price)));
        	        }

        	        document.add(new LineSeparator());
        	        document.add(new Paragraph(String.format("Subtotal: ₹%.2f", total)));
        	        document.add(new Paragraph(String.format("Tax (5%%): ₹%.2f", tax)));
        	        document.add(new Paragraph(String.format("Discount (10%%): ₹%.2f", discount)));
        	        document.add(new Paragraph(String.format("Final Total: ₹%.2f\n", finalBill),
        	                new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD)));

        	        document.add(new Paragraph("\nThank you for visiting RusticRoots Kitchen!",
        	                new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC)));

        	        document.close();

        	        JOptionPane.showMessageDialog(this, "✅ Bill generated and recorded successfully:\n" + fileName,
        	                "Success", JOptionPane.INFORMATION_MESSAGE);

        	        // Reset Table to Available
        	        if (selectedTableNumber != null) {
        	            PreparedStatement ps = conn.prepareStatement("UPDATE restaurant_tables SET status = 'Available' WHERE table_number = ?");
        	            ps.setString(1, selectedTableNumber);
        	            ps.executeUpdate();
        	            loadTableList();
        	        }

        	        orderModel.setRowCount(0);
        	        selectedTableNumber = null;
        	        selectedTableLabel.setText("Selected Table: None");

        	    } catch (Exception e) {
        	        JOptionPane.showMessageDialog(this, "Error generating bill: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        	        e.printStackTrace();
        	    }
        	}
        }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new take_order_page().setVisible(true));
    }
}
