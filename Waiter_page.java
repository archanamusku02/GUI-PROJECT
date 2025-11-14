package restaurant_package;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Waiter_page extends JFrame implements ActionListener {

    JButton viewItemsBtn, generateBillBtn, takeOrderBtn,logoutBtn;
    JLabel headingLabel;

    public Waiter_page() {
        // Frame setup
        setTitle("Cashier Page");
        setSize(1700, 900);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue tone

        // Heading label
        headingLabel = new JLabel("Welcome to Cashier Page");
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        headingLabel.setForeground(new Color(25, 25, 112)); // Royal Blue
        headingLabel.setBounds(300, 100, 900, 80);
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(headingLabel);

        // View Items button
        viewItemsBtn = new JButton("View Items");
        viewItemsBtn.setFont(new Font("Segoe UI", Font.BOLD, 28));
        viewItemsBtn.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        viewItemsBtn.setForeground(Color.WHITE);
        viewItemsBtn.setFocusPainted(false);
        viewItemsBtn.setBounds(500, 250, 400, 90);
        viewItemsBtn.addActionListener(this);
        add(viewItemsBtn);

        // Take Orders button
        takeOrderBtn = new JButton("Take Orders");
        takeOrderBtn.setFont(new Font("Segoe UI", Font.BOLD, 28));
        takeOrderBtn.setBackground(new Color(255, 140, 0)); // Dark Orange
        takeOrderBtn.setForeground(Color.WHITE);
        takeOrderBtn.setFocusPainted(false);
        takeOrderBtn.setBounds(500, 375, 400, 90); // Placed between the other two buttons
        takeOrderBtn.addActionListener(this);
        add(takeOrderBtn);
//         Logout Button
        logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
        logoutBtn.setBounds(880, 630, 150, 40);
        logoutBtn.setBackground(new Color(220, 20, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addActionListener(this);
        add(logoutBtn);

        

        setLocationRelativeTo(null);
        setVisible(true);
    }

//    public static void main(String[] args) {
//        new Waiter_page();
//    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(viewItemsBtn)) {
            items_page ip = new items_page();
            ip.setTitle("Items Page");
            ip.setSize(1700, 900);
            ip.setVisible(true);
        }
        if (ae.getSource().equals(takeOrderBtn)) {
            take_order_page top = new take_order_page();
            top.setTitle("Take Orders Page");
            top.setSize(1700, 900);
            top.setVisible(true);
        }
        else if (ae.getSource() == logoutBtn) {
            JOptionPane.showMessageDialog(this, "You have been logged out.");
            Home_page hp=new Home_page();
            hp.setTitle("home Page");
            hp.setSize(1700, 900);
            hp.setVisible(true);
        }
//        if (ae.getSource().equals(generateBillBtn)) {
//            bill_generator bg = new bill_generator();
//            bg.setTitle("Invoice Page");
//            bg.setSize(1700, 900);
//            bg.setVisible(true);
//        }
    }
}
