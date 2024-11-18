/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HotelPackage;
import java.sql.*;
import java.util.*;
import java.io.*;



public class Billing {
    private Connection connection;
    public Billing() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","@Khushi743");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int printBill(int cid, String name) {
    int amountPaid = -1; // Default value if details are not found
    try {
        // Check if the cid and name exist in the customer_information table
        String checkQuery = "SELECT * FROM customer_information WHERE cid = ? AND customer_name = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
        checkStatement.setInt(1, cid);
        checkStatement.setString(2, name);
        ResultSet checkResultSet = checkStatement.executeQuery();
        if (checkResultSet.next()) {
            // Retrieve the food price from the customer_information table
            double foodPrice = checkResultSet.getDouble("food_price");

            // Check if the cid exists in the checkout table
            String checkoutQuery = "SELECT * FROM checkout WHERE cid = ?";
            PreparedStatement checkoutStatement = connection.prepareStatement(checkoutQuery);
            checkoutStatement.setInt(1, cid);
            ResultSet checkoutResultSet = checkoutStatement.executeQuery();

            if (checkoutResultSet.next()) {
                // Retrieve the room price from the checkout table
                double roomPrice = checkoutResultSet.getDouble("total_room_price");

                // Calculate the total amount paid (room price + food price)
                amountPaid = (int) (roomPrice + foodPrice);

                // Update the amount paid in the customer_information table
                String updateQuery = "UPDATE customer_information SET amount_paid = ? WHERE cid = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, amountPaid);
                updateStatement.setInt(2, cid);
                updateStatement.executeUpdate();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return amountPaid;
}
}
    

   

