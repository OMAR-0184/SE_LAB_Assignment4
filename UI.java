
import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.sql.*;

class StudentManagementUI {

    public static final String URL = "jdbc:postgresql://localhost:5432/mybd";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "Open@123";
    private static Connection conn;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                createUI();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void createUI() {
        JFrame frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the window

        // Create the header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(42, 157, 143)); // Teal color
        JLabel headerLabel = new JLabel("Student Management System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonsPanel.setBackground(new Color(245, 245, 245));

        JButton addStudentButton = createStyledButton("Add Student", new Color(42, 157, 143));
        JButton editMarksButton = createStyledButton("Edit Marks", new Color(233, 111, 81));
        JButton deleteStudentButton = createStyledButton("Delete Student", new Color(244, 162, 97));
        JButton showDataButton = createStyledButton("Show Data", new Color(38, 71, 82));

        // Add button actions
        addStudentButton.addActionListener(e -> addStudent());
        editMarksButton.addActionListener(e -> editMarks());
        deleteStudentButton.addActionListener(e -> deleteStudent());
        showDataButton.addActionListener(e -> showData());

        buttonsPanel.add(addStudentButton);
        buttonsPanel.add(editMarksButton);
        buttonsPanel.add(deleteStudentButton);
        buttonsPanel.add(showDataButton);

        // Add panels to frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(buttonsPanel, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    private static void addStudent() {
        JTextField nameField = new JTextField(15);
        JTextField rollField = new JTextField(15);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Roll No:"));
        panel.add(rollField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Enter Student Details", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            int roll = Integer.parseInt(rollField.getText());

            try {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO STUDENTS (NAME, ROLL) VALUES (?, ?)");
                stmt.setString(1, name);
                stmt.setInt(2, roll);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Student Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error Adding Student", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void editMarks() {
        JTextField rollField = new JTextField(15);
        JTextField marksField = new JTextField(15);
        String[] subjects = {"DSA", "DBMS", "CN", "OOP", "Math"};
        JComboBox<String> subjectComboBox = new JComboBox<>(subjects);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Roll No:"));
        panel.add(rollField);
        panel.add(new JLabel("Subject:"));
        panel.add(subjectComboBox);
        panel.add(new JLabel("Marks:"));
        panel.add(marksField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Edit Marks", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int roll = Integer.parseInt(rollField.getText());
                String subject = (String) subjectComboBox.getSelectedItem();
                float marks = Float.parseFloat(marksField.getText());

                PreparedStatement stmt = conn.prepareStatement("UPDATE STUDENTS SET " + subject + " = ? WHERE ROLL = ?");
                stmt.setFloat(1, marks);
                stmt.setInt(2, roll);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Marks Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error Updating Marks", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void deleteStudent() {
        JTextField rollField = new JTextField(15);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2, 10, 10));
        panel.add(new JLabel("Roll No:"));
        panel.add(rollField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Delete Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int roll = Integer.parseInt(rollField.getText());
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM STUDENTS WHERE ROLL = ?");
                stmt.setInt(1, roll);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Student Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Student Not Found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void showData() {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM STUDENTS");
            ResultSet rs = stmt.executeQuery();
            StringBuilder data = new StringBuilder("Student Data:\n");

            while (rs.next()) {
                data.append("Name: ").append(rs.getString("NAME")).append("\n")
                        .append("Roll: ").append(rs.getInt("ROLL")).append("\n")
                        .append("DSA: ").append(rs.getFloat("DSA")).append("\n")
                        .append("DBMS: ").append(rs.getFloat("DBMS")).append("\n")
                        .append("CN: ").append(rs.getFloat("CN")).append("\n")
                        .append("OOP: ").append(rs.getFloat("OOP")).append("\n")
                        .append("Math: ").append(rs.getFloat("MATH")).append("\n\n");
            }

            JOptionPane.showMessageDialog(null, data.toString(), "Student Data", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
