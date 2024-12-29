package com.client;

import com.shared.CrudOperations;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException, SQLException {
        try {

            Registry registry = LocateRegistry.getRegistry("localhost");
            CrudOperations stub = (CrudOperations) registry.lookup("GestionInventaire");


            boolean running = true;
            Scanner scanner = new Scanner(System.in);

            while (running) {
                showMenu();
                int choice = -1;
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Clear newline character
                } else {
                    System.out.println("Entrée invalide. Veuillez entrer un numéro.");
                    scanner.nextLine(); // Clear invalid input
                }

                switch (choice) {
                    case 1:
                        System.out.println("List des produit");
                        System.out.println(stub.SearchAllProducts());
                        break;
                    case 2:
                        ajouterProduit(stub,scanner);
                        break;
                    case 3:
                        modifierProduit(stub,scanner);
                        break;
                    case 4:
                        supprimerProduit(stub,scanner);
                        break;
                    case 5:
                        rechercherProduit(stub,scanner);
                        break;
                    case 6:
                        System.out.println("Merci d'avoir utilisé notre système, Au revoir!");
                        running = false; // Exit the loop
                        break;
                    default:
                        System.out.println("Choix invalide, veuillez réessayer.");
                }
            }
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }


    private static void showMenu(){
        System.out.println("\n===== Menu Gestion de Inventaires =====");
        System.out.println("1. Afficher tous les inventaires disponible");
        System.out.println("2. Ajouter un inventaire");
        System.out.println("3. Mettre à jour un inventaire");
        System.out.println("4. Supprimer un inventaire");
        System.out.println("5. Rechercher des inventaires");
        System.out.println("6. Quitter");
        System.out.print("Choisissez une option : ");
    }

    private static void rechercherProduit(CrudOperations stub, Scanner scanner) throws SQLException, NotBoundException, RemoteException {
        System.out.println("Voulez-Vous rechercher Par le \n 1-ID \n 2-Nom \n 3-Categorie : \n");

        int choice = -1;
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline character
        } else {
            System.out.println("Entrée invalide. Veuillez entrer un numéro.");
            scanner.nextLine(); // Clear invalid input
        }

        switch (choice) {
            case 1:
                rechercherById(stub,scanner);
                break;
            case 2:
                rechercherByName(stub,scanner);
                break;
            case 3:
                rechercherByCategorie(stub,scanner);
                break;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
        }
    }

    private static void supprimerProduit(CrudOperations stub, Scanner scanner) throws RemoteException {
        System.out.println("\nEntrez l'id de l'inventaire que vous voulez supprimer :");
        int idNumber = scanner.nextInt();
        scanner.nextLine();

        stub.DeleteProduct(idNumber);
        System.out.println("Produit avec l'id " + idNumber +" est supprimé");

    }

    private static void modifierProduit(CrudOperations stub,Scanner scanner) throws RemoteException, NotBoundException, SQLException {

            System.out.println("\nEntrez l'id de l'inventaire que vous voulez modifier :");
            int idNumber = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Modifier le nom du produit :");
            String name = scanner.nextLine();

            System.out.println("Modifier la catégorie :");
            String category = scanner.nextLine();

            System.out.println("Modifier la quantité :");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Modifier le prix :");
            double price = scanner.nextDouble();

            stub.UpdateProduct(idNumber, name, category, quantity, price);
            System.out.println("Le produit a été modifié avec succès !");
    }

    private static void ajouterProduit(CrudOperations stub, Scanner scanner) throws RemoteException, SQLException {
        System.out.print("Entrez le nom du produit : ");
        String name = scanner.nextLine();

        System.out.print("Entrez la catégorie : ");
        String category = scanner.nextLine();

        System.out.print("Entrez la quantité : ");
        int quantity = -1;
        if (scanner.hasNextInt()) {
            quantity = scanner.nextInt();
            scanner.nextLine(); // Clear newline character
        } else {
            System.out.println("Quantité invalide.");
            scanner.nextLine();
            return;
        }

        System.out.print("Entrez le prix : ");
        double price = -1.0;
        if (scanner.hasNextDouble()) {
            price = scanner.nextDouble();
            scanner.nextLine(); // Clear newline character
        } else {
            System.out.println("Prix invalide.");
            scanner.nextLine();
            return;
        }

        stub.AddProduct(name, category, quantity, price);
        System.out.println("Produit ajouté avec succès !");
    }

    private static void rechercherById(CrudOperations stub, Scanner scanner) throws RemoteException, NotBoundException, SQLException {
        System.out.println("Tapez le Id de l'inventaire que vous voulez rechercher");
        int idNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.println("##############################################");

        System.out.println(stub.SearchProductByID(idNumber));

    }

    private static void rechercherByName(CrudOperations stub, Scanner scanner) throws RemoteException, NotBoundException, SQLException {
        System.out.println("Tapez le nom de l'inventaire que vous voulez rechercher");
        String name = scanner.nextLine();;


        System.out.println("##############################################");

        System.out.println(stub.SearchProductByName(name));
    }

    private static void rechercherByCategorie(CrudOperations stub, Scanner scanner) throws RemoteException, NotBoundException, SQLException {
        System.out.println("Tapez la catégorie de l'inventaire que vous voulez rechercher");
        String categorie = scanner.nextLine();;


        System.out.println("##############################################");

        System.out.println(stub.SearchProductByCategory(categorie));

    }
}
