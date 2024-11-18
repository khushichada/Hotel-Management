/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HotelPackage;
import java.sql.*;
import java.util.*;
import java.io.*;
public class Food {
    private Connection connection;
    public Food() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","@Khushi743");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }     
     public void printMenu()
     {
          double totalFoodPrice = 0;

        try {
            // Query to fetch all the menu items from the menu table
            String query = "SELECT fid, name, fprice FROM menu";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Display the menu items
            System.out.println("========== MENU ==========");
            System.out.println("ID\t| Name\t\t| Price");
            System.out.println("--------------------------");
            while (resultSet.next()) {
                int fid = resultSet.getInt("fid");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("fprice");

                System.out.printf("%d\t| %s\t\t| %.2f%n", fid, name, price);
            }
            System.out.println("==========================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
public void placeFoodOrder(String customerName, String phoneNumber, String[] foodItems) {
    double totalFoodPrice = 0;
    try {
        // Iterate through each food item and calculate the total food price
        
        for (String foodItem : foodItems)
        {
            // Query to get the price of the food item from the menu table
            String query = "SELECT fprice FROM menu WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, foodItem);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double foodPrice = resultSet.getDouble("fprice");
                totalFoodPrice += foodPrice;
               
                
                System.out.println(foodItem+"\t"+foodPrice);
               
                
            }
            
        }

        // Check if the customer already has an existing record in the customer_information table
        String checkQuery = "SELECT food_price FROM customer_information WHERE customer_name = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
        checkStatement.setString(1, customerName);
        ResultSet checkResultSet = checkStatement.executeQuery();

        if (checkResultSet.next()) {
            // If the customer exists, update the food price by adding the current food price to the existing value
            double existingFoodPrice = checkResultSet.getDouble("food_price");
            totalFoodPrice += existingFoodPrice;

            // Update the food price in the customer_information table
            String updateQuery = "UPDATE customer_information SET food_price = ? WHERE customer_name = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, totalFoodPrice);
            updateStatement.setString(2, customerName);
            updateStatement.executeUpdate();
        } else {
            //If the customer does not exist, insert a new record with the customer_name and food_price in the customer_information table
            String insertQuery = "INSERT INTO customer_information (customer_name, phone_number, food_price) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, customerName);
            insertStatement.setString(2, phoneNumber);
            insertStatement.setDouble(3, totalFoodPrice);
            insertStatement.executeUpdate();
            
        }

        System.out.println("Food order placed successfully.");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
