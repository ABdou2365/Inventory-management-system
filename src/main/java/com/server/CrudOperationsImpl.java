package com.server;

import com.shared.CrudOperations;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class CrudOperationsImpl extends UnicastRemoteObject implements CrudOperations {

    Connection connection = null;

    protected CrudOperationsImpl() throws RemoteException, SQLException {
        super();
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/GestionInventaire";
            String user = "root";
            String password = "abdo@2002";
            connection = java.sql.DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to connect to database", e);
        }
    }


    @Override
    public String SearchAllProducts() throws RemoteException, SQLException {
        
        String sql = "SELECT id, name, category, quantity, price FROM Product";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            StringBuilder results = new StringBuilder();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                results.append("Id: ").append(id)
                        .append(", Name: ").append(name)
                        .append(", Category: ").append(category)
                        .append(", Quantity: ").append(quantity)
                        .append(", Price: ").append(price)
                        .append("\n");
            }

            return results.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error retrieving products", e);
        }
        }

    @Override
    public void AddProduct(String name, String category, int quantity, double price) throws RemoteException, SQLException {
        String sql = "INSERT INTO Product (name, category, quantity, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Remplir les paramètres
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, category);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setDouble(4, price);

            // Exécuter la requête
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produit ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout du produit.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du produit : " + e.getMessage());
            throw new SQLException("Error adding product", e);
        }
    }

    @Override
    public void UpdateProduct(int id, String name, String category, Integer quantity, Double price) throws RemoteException, SQLException {
        String sql = "UPDATE Product SET name = ?, category = ?, quantity = ?, price = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Remplir les paramètres
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, category);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setDouble(4, price);
            preparedStatement.setInt(5, id);

            // Exécuter la requête
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produit avec le id = "+ id +" est bien modifié!");
            } else {
                System.out.println("Échec de la modification du produit. Aucun produit trouvé avec cet id.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du produit : " + e.getMessage());
            throw new SQLException("Error updating product", e);
        }
    }

    @Override
    public void DeleteProduct(int id) throws RemoteException {
        String sql = "DELETE FROM Product WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produit avec le id = "+ id +" est bien supprimé!");
            } else {
                System.out.println("Échec de la suppression du produit. Aucun produit trouvé avec cet id.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String SearchProductByID(int idValue) throws RemoteException {
        String sql = "SELECT id, name, category, quantity, price FROM Product WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idValue);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String category = resultSet.getString("category");
                    int quantity = resultSet.getInt("quantity");
                    double price = resultSet.getDouble("price");


                    System.out.println("Recherche en cours ....");



                    return String.format("Id: %d, Name: %s, Category: %s, Quantity: %d, Price: %.2f",
                            id, name, category, quantity, price);
                } else {
                    return "Product not found.";
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Error searching product by ID", e);
        }
    }


    @Override
    public String SearchProductByName(String nameValue) throws RemoteException {
        String sql = "SELECT id, name, category, quantity, price FROM Product WHERE name LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,nameValue );

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                StringBuilder results = new StringBuilder();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String category = resultSet.getString("category");
                    int quantity = resultSet.getInt("quantity");
                    double price = resultSet.getDouble("price");

                    results.append("Id: ").append(id)
                            .append(", Name: ").append(name)
                            .append(", Category: ").append(category)
                            .append(", Quantity: ").append(quantity)
                            .append(", Price: ").append(price)
                            .append("\n");
                }

                return results.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error searching product by name", e);
        }
    }

    @Override
    public String SearchProductByCategory(String categoryValue) throws RemoteException {
        String sql = "SELECT id, name, category, quantity, price FROM Product WHERE category = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, categoryValue);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                StringBuilder results = new StringBuilder();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String category = resultSet.getString("category");
                    int quantity = resultSet.getInt("quantity");
                    double price = resultSet.getDouble("price");

                    results.append("Id: ").append(id)
                            .append(", Name: ").append(name)
                            .append(", Category: ").append(category)
                            .append(", Quantity: ").append(quantity)
                            .append(", Price: ").append(price)
                            .append("\n");
                }

                return results.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error searching product by category", e);
        }
    }

}

