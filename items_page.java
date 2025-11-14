package restaurant_package;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class items_page extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;
    JScrollPane scrollPane;
    JLabel headingLabel, imageLabel;
    JButton backButton;

    public items_page() {
        // Frame setup
        setTitle("Items List");
        setSize(1200, 700);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));

        // Back button
        backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(25, 25, 112)); // Royal Blue
        backButton.setFocusPainted(false);
        backButton.setBounds(30, 30, 100, 40);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        add(backButton);

        // Back button action
        backButton.addActionListener(e -> {
            dispose(); // Close current page
            // Optionally open another frame here
            // new admin_page().setVisible(true);
        });

        // Heading label
        headingLabel = new JLabel("Available Items");
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headingLabel.setForeground(new Color(25, 25, 112)); // Royal Blue
        headingLabel.setBounds(450, 30, 400, 50);
        add(headingLabel);

        // Table model with column names
        String[] columns = {"Item", "Price", "Quantity", "Available", "Image Path"};
        model = new DefaultTableModel(columns, 0);

        // JTable setup
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.setGridColor(Color.GRAY);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.DARK_GRAY);

        // Scroll pane (table area)
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(100, 120, 650, 450);
        add(scrollPane);

        // Image label (to show item image)
        imageLabel = new JLabel();
        imageLabel.setBounds(800, 180, 300, 300);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setOpaque(true);
        add(imageLabel);

        // Label below image
        JLabel imageText = new JLabel("Item Image Preview");
        imageText.setFont(new Font("Segoe UI", Font.BOLD, 18));
        imageText.setBounds(830, 490, 250, 30);
        imageText.setForeground(new Color(25, 25, 112));
        add(imageText);

        // Fetch data from Oracle Database
        fetchDataFromDatabase();

        // Table row selection listener
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String imagePath = (String) model.getValueAt(selectedRow, 4);
                    displayImage(imagePath);
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Fetch data from Oracle database
    private void fetchDataFromDatabase() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "Rahul", "root");

            String query = "SELECT item_name, ITEM_PRICE, quantity, available, image_path FROM RESTAURANT_ITEMS";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                String item = rs.getString("item_name");
                double price = rs.getDouble("ITEM_PRICE");
                int quantity = rs.getInt("quantity");
                String available = rs.getString("available");
                String imagePath = rs.getString("image_path");

                model.addRow(new Object[]{item, price, quantity, available, imagePath});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Method to display image from path
    private void displayImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            imageLabel.setIcon(null);
            imageLabel.setText("No Image Available");
            return;
        }

        try {
            ImageIcon icon = new ImageIcon(imagePath);
            // Resize image to fit the label
            Image img = icon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText("");
        } catch (Exception e) {
            imageLabel.setIcon(null);
            imageLabel.setText("Image not found");
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(items_page::new);
//    }

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource().equals(backButton)) {
			Waiter_page wp = new Waiter_page();
			wp.setTitle("Cashier page");
			wp.setSize(1700, 900);
			wp.setVisible(true);
		}
	}
}
