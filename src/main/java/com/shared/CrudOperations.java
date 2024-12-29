package com.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface CrudOperations extends Remote {
    String SearchAllProducts() throws RemoteException, SQLException;
    void AddProduct(String name, String category, int quantity, double price) throws RemoteException, SQLException;
    void UpdateProduct(int id, String name, String category, Integer quantity, Double price) throws RemoteException, SQLException;
    void DeleteProduct(int id) throws RemoteException;
    String SearchProductByID(int id) throws RemoteException;
    String SearchProductByName(String name) throws RemoteException;
    String SearchProductByCategory(String category) throws RemoteException;

}
