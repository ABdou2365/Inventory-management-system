package com.server;

import com.shared.CrudOperations;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) {
        try{
            CrudOperationsImpl obj = new CrudOperationsImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("GestionInventaire", obj);
            System.out.println("Server is ready to embrace your requests :)");

        } catch (RemoteException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
