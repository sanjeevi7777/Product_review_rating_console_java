package application;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static ProductInfo p = new ProductInfo("");
    static ProductReviewSystem pr = new ProductReviewSystem();
    static boolean isLoggedIn = false;
    static boolean isAdmin = false;
    static Scanner sc = new Scanner(System.in);
    static Connection connection = null;

    public static void main(String[] args) {
        int choice;

        while (true) {
            if (!isLoggedIn) {
                System.out.println("Login Menu:");
                System.out.println("1. Admin Login");
                System.out.println("2. User Login");
                System.out.println("3. Admin Signup");
                System.out.println("4. User Signup");
                System.out.println("5. Exit");
                System.out.print("Enter your choice (1/2/3/4/5): ");

                try {
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        loginAsAdmin();
                        break;
                    case 2:
                        loginAsUser();
                        break;
                    case 3:
                        signUpAsAdmin();
                        break;
                    case 4:
                        signUpAsUser();
                        break;
                    case 5:
                        System.out.println("Exiting the Product Review and Rating System.");
                        closeConnection(); // Close the database connection before exiting
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } else {
                if (isAdmin) {
                    System.out.println("Admin Menu:");
                    System.out.println("1. Add Product");
                    System.out.println("2. Display Products");
                    System.out.println("3. Logout");
                    System.out.print("Enter your choice (1/2/3): ");

                    int adminChoice = Integer.parseInt(sc.nextLine());

                    switch (adminChoice) {
                        case 1:
                            pr.addProducts();
                            break;
                        case 2:
                            pr.getAllProducts("products");
                            break;
                        case 3:
                            isLoggedIn = false;
                            isAdmin = false;
                            System.out.println("Logged out successfully!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                } else {
                    System.out.println("User Menu:");
                    System.out.println("1. Add Product Review");
                    System.out.println("2. Display Product Reviews");
                    System.out.println("3. Logout");
                    System.out.print("Enter your choice (1/2/3): ");

                    int userChoice = Integer.parseInt(sc.nextLine());

                    switch (userChoice) {
                        case 1:
                            pr.addReviews();
                            break;
                        case 2:
                            pr.getReviews();
                            break;
                        case 3:
                            isLoggedIn = false;
                            System.out.println("Logged out successfully!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                }
            }
        }
    }

    private static void connectToDatabase() {
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/Product_review";
        String username = "root";
        String password = "Dhon!777";

        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing the database connection: " + e.getMessage());
            }
        }
    }

    public static void loginAsAdmin() {
        connectToDatabase();

        System.out.print("Enter your admin username: ");
        String adminUsername = sc.nextLine();
        System.out.print("Enter your admin password: ");
        String adminPassword = sc.nextLine();

        String query = "SELECT * FROM admin_details WHERE admin_name = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            ps.setString(1, adminUsername);
            ps.setString(2, adminPassword);

            // Execute the query
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Admin login successful!");
                    isLoggedIn = true;
                    isAdmin = true;
                } else {
                    System.out.println("Invalid credentials. Admin login failed.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public static void loginAsUser() {
        connectToDatabase();

        System.out.print("Enter your user username: ");
        String userUsername = sc.nextLine();
        System.out.print("Enter your user password: ");
        String userPassword = sc.nextLine();

        String query = "SELECT * FROM user_details WHERE user_name = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            ps.setString(1, userUsername);
            ps.setString(2, userPassword);

            // Execute the query
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User login successful!");
                    isLoggedIn = true;
                    isAdmin = false;
                } else {
                    System.out.println("Invalid credentials. User login failed.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public static void signUpAsAdmin() {
        connectToDatabase();

        System.out.print("Enter your admin username: ");
        String adminUsername = sc.nextLine();
        System.out.print("Enter your admin password: ");
        String adminPassword = sc.nextLine();

        String query = "INSERT INTO admin_details(admin_name, password) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            ps.setString(1, adminUsername);
            ps.setString(2, adminPassword);

            // Execute the query
            int count = ps.executeUpdate();
            System.out.println("Number of rows affected by this query: " + count);

        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public static void signUpAsUser() {
        connectToDatabase();

        System.out.print("Enter your user username: ");
        String userUsername = sc.nextLine();
        System.out.print("Enter your user password: ");
        String userPassword = sc.nextLine();

        String query = "INSERT INTO user_details(user_name, password) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            ps.setString(1, userUsername);
            ps.setString(2, userPassword);

            // Execute the query
            int count = ps.executeUpdate();
            System.out.println("Number of rows affected by this query: " + count);

        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

}
