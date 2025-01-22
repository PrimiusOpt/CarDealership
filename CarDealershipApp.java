import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CarDealershipApp {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Welcome to the Car Dealership Management System");

            while (true) {
                System.out.println("\n1. View Cars");
                System.out.println("2. Add Car");
                System.out.println("3. Update Car");
                System.out.println("4. Delete Car");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> viewCars(conn);
                    case 2 -> addCar(conn, scanner);
                    case 3 -> updateCar(conn, scanner);
                    case 4 -> deleteCar(conn, scanner);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewCars(Connection conn) {
        String query = "SELECT * FROM Cars";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAvailable Cars:");
            while (rs.next()) {
                System.out.printf("ID: %d, Make: %s, Model: %s, Year: %d, Price: %.2f, Available: %b\n",
                        rs.getInt("car_id"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getDouble("price"), rs.getBoolean("availability"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addCar(Connection conn, Scanner scanner) {
        System.out.print("Enter make: ");
        String make = scanner.next();
        System.out.print("Enter model: ");
        String model = scanner.next();

        // Validating year input
        int year = 0;
        while (true) {
            try {
                System.out.print("Enter year: ");
                year = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid year.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        // Validating price input
        double price = 0;
        while (true) {
            try {
                System.out.print("Enter price: ");
                price = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        // Validating availability input
        boolean availability = false;
        while (true) {
            System.out.print("Is it available? (true/false): ");
            if (scanner.hasNextBoolean()) {
                availability = scanner.nextBoolean();
                break;
            } else {
                System.out.println("Invalid input. Please enter true or false.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        String query = "INSERT INTO Cars (make, model, year, price, availability) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, make);
            pstmt.setString(2, model);
            pstmt.setInt(3, year);
            pstmt.setDouble(4, price);
            pstmt.setBoolean(5, availability);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Car added successfully!" : "Failed to add car.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateCar(Connection conn, Scanner scanner) {
        System.out.print("Enter the ID of the car you want to update: ");
        int carId = scanner.nextInt();
        
        // Checking if the car exists
        String checkQuery = "SELECT * FROM Cars WHERE car_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, carId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("Car with the given ID not found.");
                return;
            }

            // Proceed with updating the car details
            System.out.println("Current details:");
            System.out.printf("Make: %s, Model: %s, Year: %d, Price: %.2f, Available: %b\n",
                    rs.getString("make"), rs.getString("model"),
                    rs.getInt("year"), rs.getDouble("price"), rs.getBoolean("availability"));

            // Get updated details from user
            System.out.print("Enter new make (or press Enter to keep current): ");
            scanner.nextLine(); // Clear the buffer
            String make = scanner.nextLine();
            if (make.isEmpty()) make = rs.getString("make");

            System.out.print("Enter new model (or press Enter to keep current): ");
            String model = scanner.nextLine();
            if (model.isEmpty()) model = rs.getString("model");

            System.out.print("Enter new year (or press Enter to keep current): ");
            int year = rs.getInt("year");
            try {
                String yearInput = scanner.nextLine();
                if (!yearInput.isEmpty()) {
                    year = Integer.parseInt(yearInput);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid year input. Keeping the current year.");
            }

            System.out.print("Enter new price (or press Enter to keep current): ");
            double price = rs.getDouble("price");
            try {
                String priceInput = scanner.nextLine();
                if (!priceInput.isEmpty()) {
                    price = Double.parseDouble(priceInput);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid price input. Keeping the current price.");
            }

            System.out.print("Enter new availability (true/false or press Enter to keep current): ");
            boolean availability = rs.getBoolean("availability");
            String availabilityInput = scanner.nextLine();
            if (!availabilityInput.isEmpty()) {
                availability = Boolean.parseBoolean(availabilityInput);
            }

            // Update query
            String updateQuery = "UPDATE Cars SET make = ?, model = ?, year = ?, price = ?, availability = ? WHERE car_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setString(1, make);
                pstmt.setString(2, model);
                pstmt.setInt(3, year);
                pstmt.setDouble(4, price);
                pstmt.setBoolean(5, availability);
                pstmt.setInt(6, carId);

                int rowsAffected = pstmt.executeUpdate();
                System.out.println(rowsAffected > 0 ? "Car updated successfully!" : "Failed to update car.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteCar(Connection conn, Scanner scanner) {
        System.out.print("Enter the ID of the car you want to delete: ");
        int carId = scanner.nextInt();

        // Checking if the car exists
        String checkQuery = "SELECT * FROM Cars WHERE car_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, carId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("Car with the given ID not found.");
                return;
            }

            // Confirm deletion
            System.out.print("Are you sure you want to delete this car? (yes/no): ");
            String confirmation = scanner.next();
            if ("yes".equalsIgnoreCase(confirmation)) {
                String deleteQuery = "DELETE FROM Cars WHERE car_id = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                    deleteStmt.setInt(1, carId);
                    int rowsAffected = deleteStmt.executeUpdate();
                    System.out.println(rowsAffected > 0 ? "Car deleted successfully!" : "Failed to delete car.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Car deletion canceled.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}