package Student;
import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\Sahil Avhad\\eclipse-workspace\\StudentMgt\\Student.db";

    public static void main(String[] args) {
        try {
            // Initialize database
            createDatabase();
            createTable();

            // Menu loop
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n--- Student Management System ---");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student Details");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> viewStudents();
                    case 3 -> updateStudent();
                    case 4 -> deleteStudent();
                    case 5 -> {
                        System.out.println("Exiting program...");
                        scanner.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Connected to the database successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    private static void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS students (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER NOT NULL,
                grade TEXT NOT NULL
            );
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Student table created or verified successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    private static void addStudent() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students (name, age, grade) VALUES (?, ?, ?)")) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter student name: ");
            String name = scanner.nextLine();

            System.out.print("Enter student age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter student grade: ");
            String grade = scanner.nextLine();

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, grade);
            pstmt.executeUpdate();

            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private static void viewStudents() {
        String sql = "SELECT * FROM students";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nID\tName\t\tAge\tGrade");
            System.out.println("-------------------------------------");

            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String grade = rs.getString("grade");

                System.out.printf("%d\t%-15s\t%d\t%s%n", id, name, age, grade);
            }

            if (!hasRecords) {
                System.out.println("No students found in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing students: " + e.getMessage());
        }
    }


    private static void updateStudent() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE students SET name = ?, age = ?, grade = ? WHERE id = ?")) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter student ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter new name: ");
            String name = scanner.nextLine();

            System.out.print("Enter new age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter new grade: ");
            String grade = scanner.nextLine();

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, grade);
            pstmt.setInt(4, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM students WHERE id = ?")) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter student ID to delete: ");
            int id = scanner.nextInt();

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }
}


