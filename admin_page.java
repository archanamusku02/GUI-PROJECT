package restaurant_package;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class admin_page extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JButton addBtn, editBtn, updateBtn, deleteBtn, dailyTallyBtn, browseBtn, refreshBtn, logoutBtn;
    JTextField itemNameField, priceField, quantityField;
    JComboBox<String> availableBox;
    JLabel itemImageLabel;
    JFileChooser fileChooser;
    String selectedImagePath = null;

    public admin_page() {
        setTitle("Admin Page - Restaurant Management");
        setSize(1800, 900);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(250, 250, 250));

        model = new DefaultTableModel(new Object[]{"Item Name", "Price", "Quantity", "Available", "Image Path"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(550, 100, 600, 500);
        add(scrollPane);

        JLabel titleLabel = new JLabel("Admin - Menu Management");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 35));
        titleLabel.setBounds(400, 20, 500, 40);
        add(titleLabel);

        JLabel nameLabel = new JLabel("Item Name:");
        nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        nameLabel.setBounds(50, 100, 150, 30);
        add(nameLabel);

        itemNameField = new JTextField();
        itemNameField.setFont(new Font("Times New Roman", Font.BOLD, 18));
        itemNameField.setBounds(200, 100, 250, 30);
        add(itemNameField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        priceLabel.setBounds(50, 150, 150, 30);
        add(priceLabel);

        priceField = new JTextField();
        priceField.setFont(new Font("Times New Roman", Font.BOLD, 18));
        priceField.setBounds(200, 150, 250, 30);
        add(priceField);

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        qtyLabel.setBounds(50, 200, 150, 30);
        add(qtyLabel);

        quantityField = new JTextField();
        quantityField.setFont(new Font("Times New Roman", Font.BOLD, 18));
        quantityField.setBounds(200, 200, 250, 30);
        add(quantityField);

        JLabel availableLabel = new JLabel("Available:");
        availableLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        availableLabel.setBounds(50, 250, 150, 30);
        add(availableLabel);

        availableBox = new JComboBox<>(new String[]{"Yes", "No"});
        availableBox.setBounds(200, 250, 250, 30);
        add(availableBox);

        JLabel imageLabel = new JLabel("Item Image:");
        imageLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        imageLabel.setBounds(50, 300, 150, 30);
        add(imageLabel);

        itemImageLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        itemImageLabel.setBounds(200, 300, 250, 250);
        itemImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(itemImageLabel);

        browseBtn = new JButton("Browse");
        browseBtn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        browseBtn.setBounds(200, 570, 250, 35);
        browseBtn.setBackground(new Color(70, 130, 180));
        browseBtn.setForeground(Color.WHITE);
        browseBtn.addActionListener(this);
        add(browseBtn);

        addBtn = new JButton("Add Item");
        addBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
        addBtn.setBounds(50, 630, 150, 40);
        addBtn.setBackground(new Color(34, 139, 34));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(this);
        add(addBtn);

        editBtn = new JButton("Edit");
        editBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
        editBtn.setBounds(220, 630, 100, 40);
        editBtn.setBackground(new Color(255, 165, 0));
        editBtn.setForeground(Color.WHITE);
        editBtn.addActionListener(this);
        add(editBtn);

        updateBtn = new JButton("Update");
        updateBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
        updateBtn.setBounds(340, 630, 100, 40);
        updateBtn.setBackground(new Color(70, 130, 180));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.addActionListener(this);
        add(updateBtn);

        deleteBtn = new JButton("Delete");
        deleteBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
        deleteBtn.setBounds(460, 630, 100, 40);
        deleteBtn.setBackground(new Color(178, 34, 34));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(this);
        add(deleteBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
        refreshBtn.setBounds(580, 630, 120, 40);
        refreshBtn.setBackground(new Color(100, 149, 237));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(this);
        add(refreshBtn);

        dailyTallyBtn = new JButton("Daily Tally");
        dailyTallyBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
        dailyTallyBtn.setBounds(720, 630, 150, 40);
        dailyTallyBtn.setBackground(new Color(123, 104, 238));
        dailyTallyBtn.setForeground(Color.WHITE);
        dailyTallyBtn.addActionListener(this);
        add(dailyTallyBtn);

        // ✅ Added Logout Button
        logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
        logoutBtn.setBounds(880, 630, 150, 40);
        logoutBtn.setBackground(new Color(220, 20, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addActionListener(this);
        add(logoutBtn);

        fileChooser = new JFileChooser();

        // Load data from DB
        loadData();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ✅ Load items from DB into JTable
    private void loadData() {
        model.setRowCount(0);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "Rahul", "root");
            PreparedStatement psmt = con.prepareStatement("SELECT * FROM RESTAURANT_ITEMS");
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("ITEM_NAME"),
                        rs.getDouble("ITEM_PRICE"),
                        rs.getInt("QUANTITY"),
                        rs.getString("AVAILABLE"),
                        rs.getString("IMAGE_PATH")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void clearFields() {
        itemNameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        availableBox.setSelectedIndex(0);
        selectedImagePath = null;
        itemImageLabel.setIcon(null);
        itemImageLabel.setText("No Image Selected");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(browseBtn)) {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                    selectedImagePath = file.getAbsolutePath().replace("\\", "/");
                    ImageIcon icon = new ImageIcon(selectedImagePath);
                    Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                    itemImageLabel.setIcon(new ImageIcon(img));
                    itemImageLabel.setText("");
                }
            } else {
                selectedImagePath = null;
                itemImageLabel.setIcon(null);
                itemImageLabel.setText("No Image Selected");
            }
        } 
        else if (e.getSource().equals(addBtn)) {
            String name = itemNameField.getText();
            String price = priceField.getText();
            String qty = quantityField.getText();
            String available = (String) availableBox.getSelectedItem();

            if (name.isEmpty() || price.isEmpty() || qty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            try (Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "Rahul", "root");
                 PreparedStatement ps = con.prepareStatement("INSERT INTO RESTAURANT_ITEMS VALUES (?, ?, ?, ?, ?)")) {

                ps.setString(1, name);
                ps.setDouble(2, Double.parseDouble(price));
                ps.setInt(3, Integer.parseInt(qty));
                ps.setString(4, available);
                ps.setString(5, selectedImagePath);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item added successfully!");
                loadData();
                clearFields();
            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this, "Item name already exists!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } 
        else if (e.getSource().equals(editBtn)) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                itemNameField.setText(model.getValueAt(row, 0).toString());
                priceField.setText(model.getValueAt(row, 1).toString());
                quantityField.setText(model.getValueAt(row, 2).toString());
                availableBox.setSelectedItem(model.getValueAt(row, 3).toString());
                selectedImagePath = (String) model.getValueAt(row, 4);
                if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                    ImageIcon icon = new ImageIcon(selectedImagePath);
                    Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                    itemImageLabel.setIcon(new ImageIcon(img));
                } else {
                    itemImageLabel.setIcon(null);
                    itemImageLabel.setText("No Image Selected");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a row to edit!");
            }
        } 
        else if (e.getSource().equals(updateBtn)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a row to update!");
                return;
            }

            String name = itemNameField.getText();
            String price = priceField.getText();
            String qty = quantityField.getText();
            String available = (String) availableBox.getSelectedItem();

            try (Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "Rahul", "root");
                 PreparedStatement ps = con.prepareStatement(
                         "UPDATE RESTAURANT_ITEMS SET ITEM_PRICE=?, QUANTITY=?, AVAILABLE=?, IMAGE_PATH=? WHERE ITEM_NAME=?")) {

                ps.setDouble(1, Double.parseDouble(price));
                ps.setInt(2, Integer.parseInt(qty));
                ps.setString(3, available);
                ps.setString(4, selectedImagePath);
                ps.setString(5, name);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Item updated!");
                loadData();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating: " + ex.getMessage());
            }
        } 
        else if (e.getSource().equals(deleteBtn)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a row to delete!");
                return;
            }

            String name = model.getValueAt(row, 0).toString();

            try (Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "Rahul", "root");
                 PreparedStatement ps = con.prepareStatement("DELETE FROM RESTAURANT_ITEMS WHERE ITEM_NAME=?")) {
                ps.setString(1, name);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item deleted!");
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting: " + ex.getMessage());
            }
        } 
        else if (e.getSource() == refreshBtn) loadData();
        
        	else if (e.getSource() == dailyTallyBtn) {
        	    try {
        	        Class.forName("oracle.jdbc.driver.OracleDriver");
        	        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "Rahul", "root");

        	        String query = "SELECT TO_CHAR(BILL_DATE, 'DD-MON-YYYY HH24:MI:SS') AS BILL_TIME, TABLE_NUMBER, SUBTOTAL, TAX, DISCOUNT, FINAL_TOTAL FROM RESTAURANT_BILLS WHERE TRUNC(BILL_DATE) = TRUNC(SYSDATE)";
        	        PreparedStatement ps = con.prepareStatement(query);
        	        ResultSet rs = ps.executeQuery();

        	        if (!rs.isBeforeFirst()) {
        	            JOptionPane.showMessageDialog(this, "No sales found for today!");
        	            return;
        	        }

        	        // Display in JTable
        	        DefaultTableModel salesModel = new DefaultTableModel(new Object[]{"Time", "Table", "Subtotal", "Tax", "Discount", "Final Total"}, 0);
        	        double dailyTotal = 0;

        	        while (rs.next()) {
        	            salesModel.addRow(new Object[]{
        	                rs.getString("BILL_TIME"),
        	                rs.getString("TABLE_NUMBER"),
        	                rs.getDouble("SUBTOTAL"),
        	                rs.getDouble("TAX"),
        	                rs.getDouble("DISCOUNT"),
        	                rs.getDouble("FINAL_TOTAL")
        	            });
        	            dailyTotal += rs.getDouble("FINAL_TOTAL");
        	        }

        	        JTable tallyTable = new JTable(salesModel);
        	        JScrollPane scrollPane = new JScrollPane(tallyTable);
        	        scrollPane.setPreferredSize(new Dimension(700, 300));
        	        JOptionPane.showMessageDialog(this, scrollPane, "Today's Sales Report", JOptionPane.INFORMATION_MESSAGE);

        	        // ✅ Generate PDF report
        	        String reportName = "Daily_Tally_" + new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) + ".pdf";
        	        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        	        com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(reportName));
        	        document.open();

        	        com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("RusticRoots Kitchen - Daily Sales Report",
        	                new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 20, com.itextpdf.text.Font.BOLD));
        	        title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        	        document.add(title);
        	        document.add(new com.itextpdf.text.Paragraph("Date: " + new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())));
        	        document.add(new com.itextpdf.text.Paragraph("\n"));

        	        com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(6);
        	        pdfTable.addCell("Time");
        	        pdfTable.addCell("Table");
        	        pdfTable.addCell("Subtotal");
        	        pdfTable.addCell("Tax");
        	        pdfTable.addCell("Discount");
        	        pdfTable.addCell("Final Total");

        	        rs = ps.executeQuery(); // re-run to populate PDF
        	        while (rs.next()) {
        	            pdfTable.addCell(rs.getString("BILL_TIME"));
        	            pdfTable.addCell(rs.getString("TABLE_NUMBER"));
        	            pdfTable.addCell(String.format("₹%.2f", rs.getDouble("SUBTOTAL")));
        	            pdfTable.addCell(String.format("₹%.2f", rs.getDouble("TAX")));
        	            pdfTable.addCell(String.format("₹%.2f", rs.getDouble("DISCOUNT")));
        	            pdfTable.addCell(String.format("₹%.2f", rs.getDouble("FINAL_TOTAL")));
        	        }

        	        document.add(pdfTable);
        	        document.add(new com.itextpdf.text.Paragraph("\nTotal Sales: ₹" + String.format("%.2f", dailyTotal),
        	                new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD)));
        	        document.close();

        	        JOptionPane.showMessageDialog(this, "✅ Daily tally report saved as: " + reportName);

        	    } catch (Exception ex) {
        	        JOptionPane.showMessageDialog(this, "Error generating daily tally: " + ex.getMessage());
        	        ex.printStackTrace();
        	    }
        	}

        
            
         if (e.getSource() == logoutBtn) {
            JOptionPane.showMessageDialog(this, "You have been logged out.");
            Home_page hp=new Home_page();
            hp.setTitle("home Page");
            hp.setSize(1700, 900);
            hp.setVisible(true);
        }
    }
//         public static void main(String[] args) {
//             new admin_page();
//         
//         }
}

    

