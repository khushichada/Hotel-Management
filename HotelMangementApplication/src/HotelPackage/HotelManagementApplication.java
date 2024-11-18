/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HotelPackage;
import java.sql.Date;
import java.util.Scanner;
public class HotelManagementApplication 
{
    public static void main(String[] args) throws Exception
    {
        Hotel h=new Hotel();
        Food f=new Food();
        Billing bl=new Billing();
        System.out.println("GRAND WELCOME TO THE HOTEL OMG!");
        System.out.println("please select you  necessities");
        Scanner scanner=new Scanner(System.in);
        while (true)
        {
            System.out.println("1. Check-in guest");
            System.out.println("2. Check-out guest");
            System.out.println("3. Print guest list");
            System.out.println("4. Place food order");
            System.out.println("5. Billing");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter your name: ");
                    String cname = scanner.next();
                    System.out.println("we have 2 types of rooms ");
                    System.out.println("1.ordinary(single bed)");
                    System.out.println("2.luxurious(double bed with balcony");
                    System.out.println("enter the type of room you want(ordinary/luxurious)");
                    String roomtype=scanner.next();
                    System.out.println("Enter address: ");
                    String address = scanner.next();
                    System.out.print("Enter phone number: ");
                    String phoneNumber = scanner.next();
                    System.out.print("Enter check-in date (yyyy-MM-dd): ");
                    String checkInDateString = scanner.next();
                    Date checkInDate = Date.valueOf(checkInDateString);
                    int roomno=h.checkInGuest(cname, address, phoneNumber,roomtype,checkInDate );
                    if(roomno==-1)
                    {
                        System.out.println("sorry! the type of room you want is not available.");
                    }
                    else
                    {
                        System.out.println("YOU ARE ALLOTED ROOM "+roomno);
                        System.out.println("wish you a happy stay");
                    }
                    break;
                case 2:
                   System.out.print("Enter your customer id :");
                   int cid = scanner.nextInt();
                   System.out.print("Enter your room number :");
                   int rn=scanner.nextInt();
                   Date checkOutDate = new Date(System.currentTimeMillis()); // Current date as check-out date
                   h.checkOutGuest(cid,rn,checkOutDate);
                   break;
                case 3:                    
                    h.printGuestList();
                    break;
                case 4:
                    System.out.println("welcome to the food Section");
                    System.out.print("Enter yor name:");
                    String name=scanner.next();
                    System.out.print("Enter yor phone number:");
                    String pno=scanner.next();
                    
                    System.out.println("Please Order food from provided menu");
                    
                    f.printMenu();
                    String foodItems[]=new String[30];
                    System.out.println("how many did you select");
                    int l;
                    try {
                        l = scanner.nextInt();
                        }
                    catch (Exception e)
                    {
                        System.out.println("Invalid input. Please enter a valid integer.");
                        scanner.nextLine(); // Clear the invalid input from the scanner buffer
                        continue; // Restart the loop to prompt the user again
                     }
                    
                    System.out.print("Enter food items name: ");
                    for(int i=0;i<l;i++)
                    {
                        foodItems[i]=scanner.next();
                    }
                    System.out.println("ordered items:");
                    f.placeFoodOrder(name,pno,foodItems);
                    System.out.println("you can collect your food after 30minutes ");
                    System.out.println("enjoy your food");
                    System.out.println("Thank you");
                    break;
                case 5:
                    System.out.println("kindly enter these details for billing");
                    System.out.println("enter cid:");
                    int c = scanner.nextInt();
                    System.out.print("Enter your name:");
                    String n=scanner.next();
                    int b=bl.printBill(c,n);
                    if(b==-1)
                    {
                        System.out.println("Please enter valid details");
                    }
                    else
                    {
                        System.out.println("Your total bill is:"+b);
                        System.out.println("please pay by cash or by using upi");
                        System.out.println("Thank you for Visiting.Please visit again..");
                    }
            }
        }           
    }  
}
