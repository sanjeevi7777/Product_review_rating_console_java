package application;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProductReviewSystem implements ProductReviewInterface {
    static Scanner scanner = new Scanner(System.in);
    static Map<String, ProductInfo> productReviews = new HashMap<>();
    static Map<String, ProductInfo> products = new HashMap<>();
    static boolean isLoggedIn = false;
    static boolean isAdmin = false;

    static Connection connection = null;

    private void connectToDatabase() {
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

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing the database connection: " + e.getMessage());
            }
        }
    }

    public void getAllProducts() {
        connectToDatabase();

        String query = "SELECT * FROM products";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String productName = rs.getString("product_name");
                String productDescription = rs.getString("description");
                ProductInfo productInfo = new ProductInfo(productDescription);
                products.put(productName, productInfo);
            }

        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @SuppressWarnings("finally")
	public void getAllProducts(String mess) {
        connectToDatabase();

        String query = "SELECT * FROM products";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String productName = rs.getString("product_name");
                String productDescription = rs.getString("description");
                ProductInfo productInfo = new ProductInfo(productDescription);
                products.put(productName, productInfo);
                System.out.println("Product: " + productName);
                System.out.println("Description: " + productInfo.getDescription());
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void getReviews() {
        getAllProducts();
        connectToDatabase();

        String query = "SELECT * FROM review";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String productName = rs.getString("product_name");
                String review = rs.getString("review");
                float rating = rs.getFloat("ratings");

                if (products.containsKey(productName)) {
                    ProductInfo productInfo = products.get(productName);
                    System.out.println("---------------reviews----------------");
                    System.out.println("Name : " + productName);
                    System.out.println("Description : " + productInfo.getDescription());
                    productInfo.addReview(review, rating);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void addProducts() {
        connectToDatabase();

        System.out.print("Enter the product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter the product description: ");
        String productDescription = scanner.nextLine();

        String query = "INSERT INTO products (product_name,description) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, productName);
            ps.setString(2, productDescription);

            int count = ps.executeUpdate();
            System.out.println("Number of rows affected by this query: " + count);

        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void addReviews() {
        connectToDatabase();

        System.out.print("Enter the product name: ");
        String productName = scanner.nextLine();

        if (!products.containsKey(productName)) {
            System.out.println("Product not found!");
            closeConnection();
            return;
        }

        System.out.print("Enter your review: ");
        String review = scanner.nextLine();
        float rating;
        try {
            System.out.print("Enter your rating (out of 5): ");
            rating = Float.parseFloat(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rating format. Please enter a valid number.");
            closeConnection();
            return;
        }

        String query = "INSERT INTO review (product_name, review, ratings) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, productName);
            ps.setString(2, review);
            ps.setFloat(3, rating);

            int count = ps.executeUpdate();
            System.out.println("Number of rows affected by this query: " + count);

            // Update the ProductInfo object with the new review
            ProductInfo productInfo = products.get(productName);
            productInfo.addReview(review, rating);

        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }
}
