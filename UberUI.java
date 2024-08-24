import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class UberUI
{
  public static void main(String[] args)
  {
    UberSystemManager uber = new UberSystemManager();
    Scanner scanner = new Scanner(System.in);
    System.out.print(">");
    boolean usersLoaded = false;
    boolean driversLoaded = false;
    while (scanner.hasNextLine())
    {
      String action = scanner.nextLine();

      if (action == null || action.equals("")) 
      {
        System.out.print("\n>");
        continue;
      }
      if (!usersLoaded) {
            usersLoaded = true;
        }
      else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
        return;
      else if (action.equalsIgnoreCase("REQUESTS"))  
      {
        uber.listAllRequests(); 
      }
      else if (action.equalsIgnoreCase("REGDRIVER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String carModel = "";
        System.out.print("Car Model: ");
        if (scanner.hasNextLine())
        {
          carModel = scanner.nextLine();
        }
        String license = "";
        System.out.print("Car License: ");
        if (scanner.hasNextLine())
        {
          license = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");

        if (scanner.hasNextLine()) {
          address = scanner.nextLine();
        }

        try {
          uber.registerNewDriver(name, carModel, license, address);
          System.out.printf("Driver: %-15s Car Model: %-15s License Plate: %-10s Address: %-20s\n", name, carModel, license, address);
        } catch (RuntimeException e) {
          System.out.println(e.getMessage());
      }
      
      }
      else if (action.equalsIgnoreCase("REGUSER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        double wallet = 0.0;
        System.out.print("Wallet: ");
        if (scanner.hasNextDouble())
        {
          wallet = scanner.nextDouble();
          scanner.nextLine(); 
        }
         try {
          uber.registerNewUser(name, address, wallet);
          System.out.printf("User: %-15s Address: %-15s Wallet: %2.2f\n", name, address, wallet);
      } catch (RuntimeException e) {
          System.out.println(e.getMessage());
      }
      
      }
      else if (action.equalsIgnoreCase("REQRIDE")) 
      {
        String account = "";
        System.out.print("User Account Id: ");
        if (scanner.hasNextLine())
        {
          account = scanner.nextLine();
        }
        String from = "";
        System.out.print("From Address: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To Address: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        if (uber.requestRide(account, from, to))
        {
          User user = uber.getUser(account);
          System.out.printf("\nRIDE for: %-15s From: %-15s To: %-15s", user.getName(), from, to);
        }
        else
        {
          System.out.println(uber.getErrorMessage());           
        }
      }
      else if (action.equalsIgnoreCase("REQDLVY")) 
      {
        String account = "";
        System.out.print("User Account Id: ");
        if (scanner.hasNextLine())
        {
          account = scanner.nextLine();
        }
        String from = "";
        System.out.print("From Address: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To Address: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        String restaurant = "";
        System.out.print("Restaurant: ");
        if (scanner.hasNextLine())
        {
          restaurant = scanner.nextLine();
        }
        String foodOrder = "";
        System.out.print("Food Order #: ");
        if (scanner.hasNextLine())
        {
          foodOrder = scanner.nextLine();
        }
        if (uber.requestDelivery(account, from, to, restaurant, foodOrder))
        {
          User user = uber.getUser(account);
          System.out.printf("\nDELIVERY for: %-15s From: %-15s To: %-15s", user.getName(), from, to);  
        }
        else
        {
          System.out.println(uber.getErrorMessage()); 
        }
      }
      else if (action.equalsIgnoreCase("SORTBYNAME")) 
      {
        uber.sortByUserName();
      }
      else if (action.equalsIgnoreCase("SORTBYWALLET")) 
      {
        uber.sortByWallet();
      }
      else if (action.equalsIgnoreCase("SORTBYDIST")) 
      {
        uber.sortByDistance();
      }
      else if (action.equalsIgnoreCase("CANCELREQ")) {
        int zone = -1;
        System.out.print("Zone Number: ");
        if (scanner.hasNextInt()) {
            zone = scanner.nextInt();
            scanner.nextLine(); 
        }
        int request = -1;
        System.out.print("Request Number: ");
        if (scanner.hasNextInt()) {
            request = scanner.nextInt();
            scanner.nextLine(); 
        }
        if (uber.cancelServiceRequest(zone, request)) {
            System.out.println("Service request #" + request + " in zone " + zone + " cancelled");
        } else {
            System.out.println(uber.getErrorMessage());
        }
    }
      else if (action.equalsIgnoreCase("DROPOFF")) {
        String driverId = "";
        System.out.print("Driver ID: ");
        if (scanner.hasNextLine()) {
            driverId = scanner.nextLine();
        }
        try {
          uber.dropOff(driverId);
          System.out.println("Driver " + driverId + " Dropping Off");
      } catch (RuntimeException e) {
          System.out.println(e.getMessage());
      }
    }
      else if (action.equalsIgnoreCase("REVENUES")) 
      {
        System.out.println("Total Revenue: " + uber.totalRevenue);
      }  else if (action.equalsIgnoreCase("PICKUP")) {
        System.out.print("Driver Id: ");
        String driverId = scanner.nextLine();
        uber.pickup(driverId);
      }else if (action.equalsIgnoreCase("LOADUSERS")) {
        System.out.print("User file: ");
        String filename = scanner.nextLine();
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String name = fileScanner.nextLine().trim();
                String address = fileScanner.nextLine().trim(); 
                double wallet = Double.parseDouble(fileScanner.nextLine().trim()); 

                boolean isDuplicate = false;
                for (User existingUser : uber.getUsers()) {
                    if (existingUser.getName().equals(name)) {
                        System.out.println("Skipping duplicate user: " + name);
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    uber.registerNewUser(name, address, wallet);
                }
            }
            usersLoaded = true;
            System.out.println("Users Loaded");
            System.out.print("\n>");
        } catch (FileNotFoundException e) {
            System.out.println("Users File: " + filename + " Not Found");
            System.out.print("\n>");
        }
        continue;
    
    }else if (action.equalsIgnoreCase("LOADDRIVERS")) {
      System.out.print("Drivers file: ");
      String filename = scanner.nextLine();
      try (Scanner fileScanner = new Scanner(new File(filename))) {
          while (fileScanner.hasNextLine()) {
              String name = fileScanner.nextLine().trim();
              String carModel = fileScanner.nextLine().trim(); 
              String license = fileScanner.nextLine().trim(); 
              String address = fileScanner.nextLine().trim(); 
  
              if (name.isEmpty() || carModel.isEmpty() || license.isEmpty() || address.isEmpty()) {
                  System.out.println("Invalid format for driver entry. Skipping...");
                  continue;
              }
              uber.registerNewDriver(name, carModel, license, address);
          }
          driversLoaded = true;
          System.out.println("Drivers Loaded");
          System.out.print("\n>");
      } catch (FileNotFoundException e) {
          System.out.println("File not found: " + filename);
      }
      continue;
  }
    else if (action.equalsIgnoreCase("USERS")) {
      if (usersLoaded) {
          uber.listAllUsers();
      } else {
          System.out.println("");
      }
  }
  else if (action.equalsIgnoreCase("DRIVERS")) {
    if (driversLoaded) {
        uber.listAllDrivers();
    } else {
        System.out.println("");
    }
}   
  else if (action.equalsIgnoreCase("DRIVETO")) {
      System.out.print("Driver Id: ");
      String driverId = scanner.nextLine();
      System.out.print("Address: ");
      String address = scanner.nextLine();
      uber.driveTo(driverId, address);
      int zoneNumber = uber.determineZoneForDriver(address);

      System.out.println("Driver " + driverId + " is Now in Zone " + zoneNumber);
    }
      else if (action.equalsIgnoreCase("ADDR")) 
      {
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        System.out.print(address);
        if (CityMap.validAddress(address))
          System.out.println("\nValid Address"); 
        else
          System.out.println("\nBad Address"); 
      }
      else if (action.equalsIgnoreCase("DIST")) 
      {
        String from = "";
        System.out.print("From: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        System.out.print("\nFrom: " + from + " To: " + to);
        System.out.println("\nDistance: " + CityMap.getDistance(from, to) + " City Blocks");
      }
      System.out.print("\n>");
    }
  }
}

