import java.sql.*;
import java.util.Scanner;

interface UserAction {
    void performAction(Connection connection, Scanner scanner) throws SQLException;
}

class CustomerAction implements UserAction {
    @Override
    public void performAction(Connection connection, Scanner scanner) throws SQLException {
        while(true){
            System.out.println("______________________________________________");
            System.out.println("1. View products");
            System.out.println("2. Search product");
            System.out.println("3. Log out");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            if(choice == 1){
                viewProducts(connection);
            } else if(choice == 2){
                searchProduct(connection, scanner);
            } else if(choice == 3){
                break;
            }
        }
    }

    private void viewProducts(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM clothes";
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            System.out.println("Type: " + resultSet.getString("type") +
                    ", Material: " + resultSet.getString("material") +
                    ", Size: " + resultSet.getString("size") +
                    ", Price: $" + resultSet.getInt("price") +
                    ", Brand: " + resultSet.getString("brand"));
        }
    }

    private void searchProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter product name to search: ");
        String productName = scanner.next();
        String query = "SELECT * FROM clothes WHERE type = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, productName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            System.out.println("Type: " + resultSet.getString("type") +
                    ", Material: " + resultSet.getString("material") +
                    ", Size: " + resultSet.getString("size") +
                    ", Price: $" + resultSet.getInt("price") +
                    ", Brand: " + resultSet.getString("brand"));
        } else {
            System.out.println("Product not found.");
        }
    }
}

class SellerAction implements UserAction {
    @Override
    public void performAction(Connection connection, Scanner scanner) throws SQLException {
        while(true){
            System.out.println("______________________________________________");
            System.out.println("1. View products");
            System.out.println("2. Add product");
            System.out.println("3. Update product");
            System.out.println("4. Delete product");
            System.out.println("5. Log out");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            if(choice == 1){
                viewProducts(connection);
            } else if(choice == 2){
                addProduct(connection, scanner);
            } else if(choice == 3){
                updateProduct(connection, scanner);
            } else if(choice == 4){
                deleteProduct(connection, scanner);
            } else if(choice == 5){
                break;
            }
        }
    }

    private void viewProducts(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM clothes";
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            System.out.println("Type: " + resultSet.getString("type") +
                    ", Material: " + resultSet.getString("material") +
                    ", Size: " + resultSet.getString("size") +
                    ", Price: $" + resultSet.getInt("price") +
                    ", Brand: " + resultSet.getString("brand"));
        }
    }

    private void addProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter type: ");
        String type = scanner.next();
        System.out.print("Enter material: ");
        String material = scanner.next();
        System.out.print("Enter size: ");
        String size = scanner.next();
        System.out.print("Enter price: ");
        int price = scanner.nextInt();
        System.out.print("Enter brand: ");
        String brand = scanner.next();

        String query = "INSERT INTO clothes (type, material, size, price, brand) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, type);
        preparedStatement.setString(2, material);
        preparedStatement.setString(3, size);
        preparedStatement.setInt(4, price);
        preparedStatement.setString(5, brand);
        preparedStatement.executeUpdate();
        System.out.println("Product added.");
    }

    private void updateProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter product name to update: ");
        String productName = scanner.next();
        String query = "SELECT * FROM clothes WHERE type = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, productName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            System.out.print("Enter new type: ");
            String newType = scanner.next();
            System.out.print("Enter new material: ");
            String newMaterial = scanner.next();
            System.out.print("Enter new size: ");
            String newSize = scanner.next();
            System.out.print("Enter new price: ");
            int newPrice = scanner.nextInt();
            System.out.print("Enter new brand: ");
            String newBrand = scanner.next();

            String updateQuery = "UPDATE clothes SET type = ?, material = ?, size = ?, price = ?, brand = ? WHERE type = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, newType);
            updateStatement.setString(2, newMaterial);
            updateStatement.setString(3, newSize);
            updateStatement.setInt(4, newPrice);
            updateStatement.setString(5, newBrand);
            updateStatement.setString(6, productName);
            updateStatement.executeUpdate();
            System.out.println("Product updated.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private void deleteProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter product name to delete: ");
        String productName = scanner.next();
        String query = "SELECT * FROM clothes WHERE type = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, productName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            String deleteQuery = "DELETE FROM clothes WHERE type = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, productName);
            deleteStatement.executeUpdate();
            System.out.println("Product deleted.");
        } else {
            System.out.println("Product not found.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","povar2009");
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.println("______________________________________________");
                System.out.println("1. Login");
                System.out.println("2. Exit");
                System.out.print("Enter your choice: ");
                int loginChoice = scanner.nextInt();
                if(loginChoice == 1){
                    if (login()) {
                        int roleChoice = chooseRole(scanner);
                        if(roleChoice == 1){
                            UserAction customerAction = new CustomerAction();
                            customerAction.performAction(connection, scanner);
                        } else if(roleChoice == 2){
                            UserAction sellerAction = new SellerAction();
                            sellerAction.performAction(connection, scanner);
                        } else {
                            System.out.println("Please try again.");
                        }
                    } else {
                        System.out.println("Login failed. Please try again.");
                    }
                } else if(loginChoice == 2){
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        return username.equals("me") && password.equals("12345");
    }

    public static int chooseRole(Scanner scanner){
        System.out.println("Choose your role:");
        System.out.println("1. Customer");
        System.out.println("2. Seller");
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }
}