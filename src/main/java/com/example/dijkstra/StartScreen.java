package com.example.dijkstra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartScreen extends JFrame {
    private JButton startButton;

    public StartScreen() {
        // Cài đặt các thuộc tính của JFrame
        setTitle("Dijkstra Algorithm Visualizer");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Hiển thị ứng dụng ở giữa màn hình

        // Tạo button "Start" và thêm ActionListener để xử lý sự kiện khi nút được nhấn
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMainScreen(); // Gọi phương thức mở màn hình chính
            }
        });

        // Thêm button vào JPanel và đặt layout của JPanel thành GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 10, 0); // Khoảng cách dưới cùng của nút
        panel.add(startButton, gbc);

        // Thêm JPanel vào BorderLayout.CENTER để nút nằm ở giữa bottom
        add(panel, BorderLayout.SOUTH);
    }

    private void openMainScreen() {
        // Gọi màn hình chính của bạn từ đây
        // Ví dụ: MainScreen mainScreen = new MainScreen();
        // mainScreen.setVisible(true);

        // Đóng màn hình bắt đầu
        dispose();
    }

    public static void main(String[] args) {
        // Chạy ứng dụng từ màn hình bắt đầu
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StartScreen startScreen = new StartScreen();
                startScreen.setVisible(true);
            }
        });
    }
}
