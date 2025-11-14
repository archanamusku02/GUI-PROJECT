package restaurant_package;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Home_page extends JFrame implements ActionListener {

	JLabel adminLabel, cashierLabel;
	JLabel hg, lg;
	ImageIcon hi, li;
	JLabel adminUserLabel, adminPassLabel, CashierUserLabel, CashierPassLabel;
	JTextField adminUserField, ChashierUserField;
	JPasswordField adminPassField, CashierPassField;
	JButton adminLoginBtn, waiterLoginBtn;
	String aname = "Gowtham Chandra";
	String apass = "gowtham@456";
	String cname = "Koushik Chary";
	String cpass = "koushik@178";

	public Home_page() {

		setLayout(null);
		// Inside your admin_page constructor — before adding components
		JPanel gradientPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				int width = getWidth();
				int height = getHeight();

				// 🔹 Royal Blue → Sky Blue gradient
				Color color1 = new Color(0, 153, 102); // Emerald Green
				Color color2 = new Color(102, 255, 204); // Sky Blue

				GradientPaint gp = new GradientPaint(0, 0, color1, width, 0, color2);
				g2d.setPaint(gp);
				g2d.fillRect(0, 0, width, height);
			}
		};

		// Use null layout if your JFrame uses setBounds for components
		gradientPanel.setLayout(null);
		li = new ImageIcon("C:\\Users\\HP\\eclipse-workspace\\Restaurant_project\\assets\\logo.jpg");
		lg = new JLabel(li);
		lg.setBounds(10, 10, 100, 100);
		setContentPane(gradientPanel);
		JLabel titleLabel = new JLabel("RusticRoots Kitchen");
		titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 60));
		titleLabel.setBounds(400, 20, 900, 60);
		add(titleLabel);

		hi = new ImageIcon(
				"C:\\Users\\HP\\eclipse-workspace\\Restaurant_project\\assets\\ChatGPT Image Nov 8, 2025, 11_08_44 PM (1).png");
		hg = new JLabel(hi);
		hg.setBounds(100, 90, 800, 550);

		// ====== ADMIN LOGIN SECTION ======
		adminLabel = new JLabel("Admin Login");
		adminLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		adminLabel.setForeground(Color.BLACK);
		adminLabel.setBounds(1000, 80, 300, 40);
		add(adminLabel);

		adminUserLabel = new JLabel("Username:");
		adminUserLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
		adminUserLabel.setForeground(Color.BLACK);
		adminUserLabel.setBounds(900, 140, 120, 30);
		add(adminUserLabel);

		adminUserField = new JTextField();
		adminUserField.setFont(new Font("Times New Roman", Font.BOLD, 20));
		adminUserField.setBounds(1050, 140, 200, 35);
		add(adminUserField);

		adminPassLabel = new JLabel("Password:");
		adminPassLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
		adminPassLabel.setForeground(Color.BLACK);
		adminPassLabel.setBounds(900, 190, 120, 30);
		add(adminPassLabel);

		adminPassField = new JPasswordField();
		adminPassField.setFont(new Font("Times New Roman", Font.BOLD, 20));
		adminPassField.setBounds(1050, 190, 200, 35);
		add(adminPassField);

		adminLoginBtn = new JButton("Login");
		adminLoginBtn.setBounds(1000, 250, 150, 40);
		adminLoginBtn.setBackground(new Color(0, 153, 204)); // Light blue
		adminLoginBtn.setForeground(Color.BLACK);
		adminLoginBtn.setFont(new Font("Times New Roman", Font.BOLD, 20));
		adminLoginBtn.setFocusable(false);
		add(adminLoginBtn);

		// ====== WAITER LOGIN SECTION ======
		cashierLabel = new JLabel("Cashier Login");
		cashierLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		cashierLabel.setForeground(Color.BLACK);
		cashierLabel.setBounds(1000, 360, 300, 40);
		add(cashierLabel);

		CashierUserLabel = new JLabel("Username:");
		CashierUserLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
		CashierUserLabel.setForeground(Color.black);
		CashierUserLabel.setBounds(900, 420, 120, 30);
		add(CashierUserLabel);

		ChashierUserField = new JTextField();
		ChashierUserField.setFont(new Font("Times New Roman", Font.BOLD, 20));
		ChashierUserField.setBounds(1050, 420, 200, 35);
		add(ChashierUserField);

		CashierPassLabel = new JLabel("Password:");
		CashierPassLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
		CashierPassLabel.setForeground(Color.BLACK);
		CashierPassLabel.setBounds(900, 470, 120, 30);
		add(CashierPassLabel);

		CashierPassField = new JPasswordField();
		CashierPassField.setFont(new Font("Times New Roman", Font.BOLD, 20));
		CashierPassField.setBounds(1050, 470, 200, 35);
		add(CashierPassField);

		waiterLoginBtn = new JButton("Login");
		waiterLoginBtn.setBounds(1000, 530, 150, 40);
		waiterLoginBtn.setBackground(new Color(0, 204, 102)); // Green shade
		waiterLoginBtn.setForeground(Color.BLACK);
		waiterLoginBtn.setFont(new Font("Times New Roman", Font.BOLD, 20));
		waiterLoginBtn.setFocusable(false);
		add(waiterLoginBtn);

		// Frame Settings
		setTitle("Restaurant Home Page");
		setSize(1700, 900);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		adminLoginBtn.addActionListener(this);
		waiterLoginBtn.addActionListener(this);
		add(lg);
		add(hg);
//		add(bg);

	}

	public static void main(String[] args) {
		new Home_page();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String adminname = adminUserField.getText();
		String apswd = adminPassField.getText();
		String cashname = ChashierUserField.getText();
		String cashpass = CashierPassField.getText();
		if (adminname.equals(aname) && apswd.equals(apass)) {
			if (ae.getSource().equals(adminLoginBtn)) {
				admin_page ap = new admin_page();
				ap.setTitle("Admin page");
				ap.setSize(1700, 900);
				ap.setVisible(true);
			}
		}
		if (cashname.equals(cname) && cashpass.equals(cpass)) {
			if (ae.getSource().equals(waiterLoginBtn)) {
				Waiter_page wp = new Waiter_page();
				wp.setTitle("Cashier page");
				wp.setSize(1700, 900);
				wp.setVisible(true);
			}
		}
	}
}
