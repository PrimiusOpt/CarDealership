import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CarUI extends JFrame {
    static JTextField modelField, yearField, priceField;
    static JTextArea resultArea;
    static JButton addButton, showButton, updateButton, deleteButton, showAllButton;

    public CarUI() {
        setTitle("Car Dealership Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Car Details"));

        modelField = createLabeledField(inputPanel, "Model:");
        yearField = createLabeledField(inputPanel, "Year:");
        priceField = createLabeledField(inputPanel, "Price:");
        add(inputPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        addButton = createButton(buttonPanel, "Add Car");
        showButton = createButton(buttonPanel, "Show Car");
        updateButton = createButton(buttonPanel, "Update Car");
        deleteButton = createButton(buttonPanel, "Delete Car");
        showAllButton = createButton(buttonPanel, "Show All Cars");
        add(buttonPanel, BorderLayout.SOUTH);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Button Actions
        addButton.addActionListener(e -> addCar());
        showButton.addActionListener(e -> showCar());
        updateButton.addActionListener(e -> updateCar());
        deleteButton.addActionListener(e -> deleteCar());
        showAllButton.addActionListener(e -> showAllCars());
    }

    private JTextField createLabeledField(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextField textField = new JTextField();
        panel.add(textField);
        return textField;
    }

    private JButton createButton(JPanel panel, String buttonText) {
        JButton button = new JButton(buttonText);
        panel.add(button);
        return button;
    }

    private void addCar() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO cars (model, year, price, availability) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, modelField.getText());
            pstmt.setInt(2, Integer.parseInt(yearField.getText()));
            pstmt.setDouble(3, Double.parseDouble(priceField.getText()));
            pstmt.setBoolean(4, true); // Assuming the car is available by default
            pstmt.executeUpdate();
            resultArea.setText("Car added successfully!");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void showCar() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM cars WHERE car_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(modelField.getText())); // Assuming modelField is used to enter the car id for simplicity
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                resultArea.setText("ID: " + rs.getInt("id") +
                        "\nModel: " + rs.getString("model") +
                        "\nYear: " + rs.getInt("year") +
                        "\nPrice: " + rs.getDouble("price"));
            } else {
                resultArea.setText("Car not found.");
            }
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void updateCar() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "UPDATE cars SET model = ?, year = ?, price = ? WHERE car_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, modelField.getText());
            pstmt.setInt(2, Integer.parseInt(yearField.getText()));
            pstmt.setDouble(3, Double.parseDouble(priceField.getText()));
            pstmt.setInt(4, Integer.parseInt(modelField.getText())); // Using modelField for car id as an example
            int rowsUpdated = pstmt.executeUpdate();
            resultArea.setText(rowsUpdated > 0 ? "Car updated successfully!" : "Car not found.");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void deleteCar() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM cars WHERE car_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(modelField.getText())); // Using modelField for car id as an example
            int rowsDeleted = pstmt.executeUpdate();
            resultArea.setText(rowsDeleted > 0 ? "Car deleted successfully!" : "Car not found.");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void showAllCars() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM cars";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder result = new StringBuilder("All Cars:\n");
            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("car_id"))
                        .append(", Model: ").append(rs.getString("model"))
                        .append(", Year: ").append(rs.getInt("year"))
                        .append(", Price: ").append(rs.getDouble("price")).append("\n");
            }
            resultArea.setText(result.toString());
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarUI().setVisible(true));
    }
}