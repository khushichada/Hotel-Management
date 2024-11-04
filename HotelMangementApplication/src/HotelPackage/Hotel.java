/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HotelPackage;
import java.util.Date;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class Hotel 
{
        private Connection connection;

    public Hotel() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","@Khushi743");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int checkInGuest(String name, String address, String phoneNumber, String roomtype, Date checkInDate) {
        int roomNumber = -1; // Default value if no room is available

       // Connect to the database and execute the query to find an available room
        try
        {// Query to find an available room of the requested type
            String query = "SELECT roomno FROM rooms WHERE roomtype = ? AND isavailable = true";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, roomtype);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                roomNumber = resultSet.getInt("roomno");
                // Update the room availability to false after assigning it to the guest
                String updateQuery = "UPDATE rooms SET isavailable = false WHERE roomno = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, roomNumber);
                updateStatement.executeUpdate();
                if(roomNumber!=-1)
                {
                String insertQuery = "INSERT INTO customer_information(customer_name,address,phone_number,room_type,room_number,check_in_date) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, name);
                insertStatement.setString(2, address);
                insertStatement.setString(3, phoneNumber);
                insertStatement.setString(4, roomtype);
                insertStatement.setInt(5, roomNumber);
                insertStatement.setDate(6, new java.sql.Date(checkInDate.getTime()));
                insertStatement.executeUpdate();  
                return roomNumber;
                }
          
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return roomNumber;
    }
 public void checkOutGuest(int cid, int roomNumber, java.util.Date checkOutDate) {
    try {
        // Update the is_available field in the rooms table to true after check-out
        String updateRoomQuery = "UPDATE rooms SET isavailable = true WHERE roomno = ?";
        PreparedStatement updateRoomStatement = connection.prepareStatement(updateRoomQuery);
        updateRoomStatement.setInt(1, roomNumber);
        updateRoomStatement.executeUpdate();
// Convert java.util.Date to java.sql.Date
        java.sql.Date sqlCheckOutDate = new java.sql.Date(checkOutDate.getTime());
// Query to get check-in date, room type, and room price for the specified cid
        String query = "SELECT check_in_date, room_type FROM customer_information WHERE cid = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, cid);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            java.sql.Date checkInDate = resultSet.getDate("check_in_date"); // Correct column name
            String roomType = resultSet.getString("room_type");
            int daysStayed = calculateDays(checkInDate, sqlCheckOutDate);
            double roomPricePerDay = getRoomPrice(roomType);
            double totalRoomPrice = daysStayed * roomPricePerDay;     // Update the total_room_price in the customer_information table
            String updateQuery = "UPDATE customer_information SET total_room_price = ?,check_out_date=? WHERE cid = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, totalRoomPrice);
            updateStatement.setDate(2,sqlCheckOutDate );
            updateStatement.setInt(3, cid);
            updateStatement.executeUpdate();          // Insert the check-out details into the checkout table
            String insertQuery = "INSERT INTO checkout (cid, room_number, check_in_date, check_out_date, days_stayed, total_room_price) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, cid);
            insertStatement.setInt(2, roomNumber);
            insertStatement.setDate(3, checkInDate);
            insertStatement.setDate(4, sqlCheckOutDate);
            insertStatement.setInt(5, daysStayed);
            insertStatement.setDouble(6, totalRoomPrice);
            insertStatement.executeUpdate();
            System.out.println("Check-out successful. Thank you for staying with us!");
        } else {
            System.out.println("No guest found with the specified CID.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
} 
 private int calculateDays(Date checkInDate, Date checkOutDate) {
        // Implementation to calculate the number of days between two dates
        // You can use libraries like java.time.LocalDate or Joda-Time for a more accurate and convenient date calculation
        // This is just a basic example
        long difference = checkOutDate.getTime() - checkInDate.getTime();
         int daysStayed = (int) (difference / (24 * 60 * 60 * 1000)); // Convert milliseconds to days

         // If check-out is on the same day, count it as one day
       return daysStayed == 0 ? 1 : daysStayed;
       
         
    }

private double getRoomPrice(String roomType) {
    try {
        // Query to get the room price for the specified room type
        String query = "SELECT price FROM rooms WHERE roomtype = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, roomType);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getDouble("price");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0; // Default room price if the room type is not found
}    
public void printGuestList() {
    try {
        // Query to fetch customer information and room price from customerinformation and checkout tables
        String query = "SELECT customer_information.cid, customer_information.customer_name, customer_information.address, customer_information.phone_number, customer_information.food_price, "
            + "checkout.total_room_price, customer_information.amount_paid "
            + "FROM customer_information "
            + "LEFT JOIN checkout ON customer_information.cid = checkout.cid";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        // Display the customer information
        System.out.println("========================== CUSTOMER INFORMATION ==========================");
        System.out.println("CID\t| Customer Name\t| Address\t\t| Phone Number\t| Food Price\t| Room Price\t| Amount Paid");
        System.out.println("-----------------------------------------------------------------------");
        while (resultSet.next()) {
            int cid = resultSet.getInt("cid");
            String customerName = resultSet.getString("customer_name");
            String address = resultSet.getString("address");
            String phoneNumber = resultSet.getString("phone_number");
            double foodPrice = resultSet.getDouble("food_price");
            double roomPrice = resultSet.getDouble("total_room_price");
            double amountPaid = resultSet.getDouble("amount_paid");

            System.out.printf("%d\t| %s\t\t| %s\t| %s\t| %.2f\t\t| %.2f\t\t| %.2f%n", cid, customerName, address, phoneNumber, foodPrice, roomPrice, amountPaid);
        }
        System.out.println("=======================================================================");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

   }

    

