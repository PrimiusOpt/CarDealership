# Car Dealership Management System (GUI-Based)

This project is a **Graphical User Interface (GUI)** application for managing car dealership operations. The application provides functionalities to add, update, delete, view, and list all cars in the inventory, making it easier to manage dealership records through an interactive interface.

---

## Features

### 1. **Add a Car**
- Allows users to input car details (make, model, year, price, and availability) and add them to the database.

### 2. **View a Car**
- Fetches and displays details of a specific car based on its ID.

### 3. **Update Car Details**
- Updates the details of an existing car in the inventory.

### 4. **Delete a Car**
- Removes a car from the inventory based on its ID.

### 5. **List All Cars**
- Displays a table of all cars available in the inventory in a separate window.

---

## Technologies Used
- **Java**: Programming language for the application logic.
- **JDBC**: For database connectivity.
- **Swing**: For the GUI implementation.
- **MySQL**: Database for storing car records.

---

## Setup Instructions

### Prerequisites
- Install [Java JDK](https://www.oracle.com/java/technologies/javase-jdk-downloads.html).
- Install [MySQL Server](https://dev.mysql.com/downloads/installer/).
- Set up an IDE such as IntelliJ IDEA, Eclipse, or VS Code.

### Database Setup
1. Create a database named `car_dealership` in MySQL:
   ```sql
   CREATE DATABASE car_dealership;
   ```
2. Create a table named `Cars`:
   ```sql
   CREATE TABLE Cars (
       car_id INT AUTO_INCREMENT PRIMARY KEY,
       make VARCHAR(50) NOT NULL,
       model VARCHAR(50) NOT NULL,
       year INT NOT NULL,
       price DOUBLE NOT NULL,
       availability BOOLEAN NOT NULL
   );
   ```
3. (Optional) Insert sample data:
   ```sql
   INSERT INTO Cars (make, model, year, price, availability) VALUES
   ('Toyota', 'Camry', 2021, 24000.00, TRUE),
   ('Honda', 'Civic', 2020, 20000.00, FALSE);
   ```

### Application Configuration
1. Set up a database connection in the `DBConnection` class:
   ```java
   public class DBConnection {
       public static Connection getConnection() throws SQLException {
           String url = "jdbc:mysql://localhost:3306/car_dealership";
           String user = "your_username"; // Replace with your MySQL username
           String password = "your_password"; // Replace with your MySQL password
           return DriverManager.getConnection(url, user, password);
       }
   }
   ```

### Running the Application
1. Compile and run the `CarDealershipUI` class.
2. Use the buttons in the GUI to perform CRUD operations.

---

## Project Structure
```
CarDealershipManagementSystem/
├── src/
│   ├── CarDealershipUI.java        # Main GUI class
│   ├── DBConnection.java           # Handles database connectivity
├── resources/
│   └── README.md                   # Project documentation
└── pom.xml                         # If using Maven
```

---

## Screenshots

### Main GUI Window
*(Include a screenshot of the main application window)*

### Add Car
*(Include a screenshot of the add car functionality)*

### List All Cars
*(Include a screenshot of the table displaying all cars)*

---

## Contributing
1. Fork the repository.
2. Create a feature branch: `git checkout -b feature-name`.
3. Commit your changes: `git commit -m 'Add feature-name'`.
4. Push to the branch: `git push origin feature-name`.
5. Submit a pull request.

---

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Acknowledgments
- **MySQL Documentation** for database setup.
- **Oracle Swing Tutorials** for GUI development.

