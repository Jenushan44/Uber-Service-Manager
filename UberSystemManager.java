import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;

public class UberSystemManager
{
  private HashMap<String, User> users;
  private ArrayList<Driver> drivers;
  private Queue<UberService>[] serviceQueues;
  private ArrayList<UberService> serviceRequests; 
  private List<Driver> allDrivers; 
  
  public double totalRevenue; 
  
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  
  private static final double PAYRATE = 0.1;
  private static final int NUM_ZONES = 4;

  int userAccountId = 900;
  int driverId = 700;

  public UberSystemManager()
  {
    users = new HashMap<>();
    drivers = new ArrayList<Driver>();
    serviceRequests = new ArrayList<UberService>(); 
    allDrivers = new ArrayList<>();
    serviceQueues = new Queue[NUM_ZONES];
        for (int i = 0; i < NUM_ZONES; i++) {
            serviceQueues[i] = new LinkedList<>();
        }
        
        totalRevenue = 0;
  }

public ArrayList<User> loadPreregisteredUsers(String filename) throws IOException {
  ArrayList<User> users = new ArrayList<>();
  try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String name, address;
      double wallet;
      while ((name = reader.readLine()) != null) {
          address = reader.readLine();
          wallet = Double.parseDouble(reader.readLine());
          users.add(new User(generateUserAccountId(), name, address, wallet));
      }
  }
  return users;
}

public ArrayList<Driver> loadPreregisteredDrivers(String filename) throws IOException {
  ArrayList<Driver> drivers = new ArrayList<>();
  try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String name, carModel, carLicense, address;
      while ((name = reader.readLine()) != null) {
          carModel = reader.readLine();
          carLicense = reader.readLine();
          address = reader.readLine(); 
          Driver driver = new Driver(generateDriverId(), name, carModel, carLicense, Driver.Status.AVAILABLE, address);
          drivers.add(driver);
      }
  }
  return drivers;
}

public void listAllRequests() {
  boolean requestsExist = false; 

  for (int i = 0; i < NUM_ZONES; i++) {
      System.out.println("\nZONE " + i);
      System.out.println("======");
      Queue<UberService> serviceQueue = serviceQueues[i];

      if (!serviceQueue.isEmpty()) {
          requestsExist = true; 
          int index = 1;
          for (UberService service : serviceQueue) {
              System.out.print(index + ". ");
              for (int j = 0; j < 60; j++)
                  System.out.print("-");
              service.printInfo();
              System.out.println();
              index++;
          }
      }
  }

}

  public void reqdlvy(String accountId, String from, String to, String restaurant, String foodOrderId) {
    
    User user = getUser(accountId);
    if (user == null) {
        System.out.println("User does not exist.");
        return;
    }

    int pickupZone = determineZone(from);

    if (requestDelivery(accountId, from, to, restaurant, foodOrderId)) {
        System.out.println("Delivery request added successfully.");
    } else {
        System.out.println(getErrorMessage());
    }
}
  String errMsg = null;

  public String getErrorMessage()
  {
    return errMsg;
  }

  private String generateUserAccountId()
  {
    return "" + userAccountId + users.size();
  }

  public void registerNewUser(String name, String address, double wallet) {
    String accountId = generateUserAccountId();
    User user = new User(accountId, name, address, wallet);
    if (users.containsKey(accountId)) {
        throw new DuplicateUserException("User Already Exists in System");
    }
    users.put(accountId, user); 
}

public User getUser(String accountId) {
  return users.get(accountId);
}

public ArrayList<User> getUsers() {
  return new ArrayList<>(users.values());
}

public ArrayList<Driver> getAllDrivers() {
  return drivers;
}
public Driver getDriver(String name) {
  for (Driver driver : allDrivers) {
      if (driver.getName().equals(name)) {
          return driver;
      }
  }
  return null; 
}
public void registerNewDriver(Driver driver) {
  this.drivers.add(driver);
}

  private String generateDriverId()
  {
    return "" + driverId + drivers.size();
  }

  private boolean userExists(User user)
  {
    for (int i = 0; i < users.size(); i++)
    {
      if (users.get(i).equals(user))
        return true;
    }
    return false;
  }
  
 private boolean driverExists(Driver driver)
 {
   for (int i = 0; i < drivers.size(); i++)
   {
     if (drivers.get(i).equals(driver))
       return true;
   }
   return false;
 }
  
 private boolean existingRequest(UberService req)
 {
   
   for (int i = 0; i < serviceRequests.size(); i++)
   {
     if (serviceRequests.get(i).equals(req))
       return true;
   }
   return false;
 } 
 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }

  private Driver getAvailableDriver()
  {
    for (int i = 0; i < drivers.size(); i++)
    {
      Driver driver = drivers.get(i);
      if (driver.getStatus() == Driver.Status.AVAILABLE)
        return driver;
    }
    return null;
  }

  public void listAllUsers() {
    List<User> userList = getUsers();
    
    Collections.sort(userList, new Comparator<User>() {
        @Override
        public int compare(User user1, User user2) {
            return user1.getAccountId().compareTo(user2.getAccountId());
        }
    });

    for (int i = 0; i < userList.size(); i++) {
        User user = userList.get(i);
        int userNumber = i + 1;
        System.out.printf("%d. Id: %s  Name: %-15s Address: %-20s Wallet: %.2f\n", userNumber, user.getAccountId(), user.getName(), user.getAddress(), user.getWallet());
    }
}
  
  public void listAllDrivers() {
    if (drivers.isEmpty()) {
        System.out.println("No drivers loaded.");
    } else {
        int driverNumber = 1;
        for (Driver driver : drivers) {
            System.out.printf("%d. Id: %-6s Name: %-15s Car Model: %-15s License Plate: %-10s   Status: %s   Wallet: %2.2f   Address: %-20s Zone: %d\n",
                    driverNumber++, driver.getId(), driver.getName(), driver.getCarModel(), driver.getLicensePlate(),driver.getStatus() ,driver.getWallet(), driver.getAddress(), driver.getZone());
        }
    }
}
public void addRequestToZone(UberService service, int zone) {
  serviceQueues[zone].add(service);
}

  public boolean requestRide(String accountId, String from, String to)
  {
      User user = getUser(accountId);
      if (user == null)
      {
          errMsg = "User Account Not Found " + accountId;
          return false;
      }
      
      if (!CityMap.validAddress(from))
      {
          errMsg = "Invalid Address " + from;
          return false;
      }
      if (!CityMap.validAddress(to))
      {
          errMsg = "Invalid Address " + to;
          return false;
      }
      
      int distance = CityMap.getDistance(from, to); 
      
      if (!(distance > 1))
      {
          errMsg = "Insufficient Travel Distance";
          return false;
      }
      
      double cost = getRideCost(distance);
      if (user.getWallet() < cost)
      {
          errMsg = "Insufficient Funds";
          return false;
      }
      
      Driver driver = getAvailableDriver();
      if (driver == null) 
      {
          errMsg = "No Drivers Available";
          return false;
      }
      
      int pickupZone = CityMap.getCityZone(from);
      
      UberRide req = new UberRide(driver, from, to, user, distance, cost);
      
      if (existingRequest(req))
      {
          errMsg = "User Already Has Ride Request";
          return false;
      }
      
      addRequestToZone(req, pickupZone);
      
      user.addRide();
      return true;
  }

  public boolean requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    User user = getUser(accountId);
    if (user == null)
    {
        errMsg = "User Account Not Found " + accountId;
        return false;
    }
    
    if (!CityMap.validAddress(from))
    {
        errMsg = "Invalid Address " + from;
        return false;
    }
    if (!CityMap.validAddress(to))
    {
        errMsg = "Invalid Address " + to;
        return false;
    }
    
    int distance = CityMap.getDistance(from, to); 
    
    if (distance == 0)
    {
        errMsg = "Insufficient Travel Distance";
        return false;
    }
    
    double cost = getDeliveryCost(distance);
    if (user.getWallet() < cost)
    {
        errMsg = "Insufficient Funds";
        return false;
    }
    
    Driver driver = getAvailableDriver();
    if (driver == null) 
    {
        errMsg = "No Drivers Available";
        return false;
    }
    int pickupZone = determineZone(from);

    UberDelivery delivery = new UberDelivery(driver, from, to, user, distance, cost, restaurant, foodOrderId); 
    
    if (existingRequest(delivery))
{
    errMsg = "User Already Has Delivery Request at Restaurant with this Food Order";
    return false;
}

serviceRequests.add(delivery);

user.addDelivery();

addRequestToZone(delivery, pickupZone);

return true;
}

  public boolean cancelServiceRequest(int zoneNumber, int requestNumber) {
    if (requestNumber < 1 || requestNumber > serviceRequests.size()) {
        errMsg = "Invalid Request # " + requestNumber;
        return false;
    }
    serviceRequests.remove(requestNumber - 1); 
    return true;
}
public void dropOff(String driverId) {
  for (Driver driver : drivers) {
      if (driver.getId().equals(driverId)) {
          if (driver.getStatus() != Driver.Status.DRIVING) {
              throw new IllegalStateException("Driver is not currently driving");
          }

          UberService service = driver.getService();

          totalRevenue += service.getCost();
          double driverPayment = service.getCost() * PAYRATE;
          driver.pay(driverPayment);
          User user = service.getUser();
          user.payForService(service.getCost());

          driver.setStatus(Driver.Status.AVAILABLE);
          driver.setService(null);
          driver.setAddress(service.getTo());
          int newZone = determineZone(driver.getAddress());
          driver.setZone(newZone);

          serviceRequests.remove(service);
          return;
      }
  }
  throw new DriverNotFoundException("Driver not found with ID: " + driverId);
}

public void setUsers(ArrayList<User> userList) {
  users.clear();

  for (User user : userList) {
      String accountId = user.getAccountId();
      if (users.containsKey(accountId)) {
          System.out.println("Skipping duplicate user with Account ID: " + accountId);
          continue;
      }
      users.put(accountId, user); 
  }
}
public void loadDrivers(String filename) {
  try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      int count = 0;
      while ((line = reader.readLine()) != null) {
          count++;
          System.out.println(line);
      }
      if (count > 0) {
          System.out.println("Drivers loaded.");
      }
  } catch (FileNotFoundException e) {
      System.out.println("File not found. Please try again.");
  } catch (IOException e) {
      System.out.println("Error occurred while loading drivers. Exiting program.");
      System.exit(1);
  }
}

public void drivers() {
  if (drivers.isEmpty()) {
      System.out.println("No drivers loaded.");
  } else {
      listAllDrivers(); 
  }
}

public void setDrivers(ArrayList<Driver> driverList) {
  drivers.clear();

  for (Driver driver : driverList) {
      if (!drivers.contains(driver)) {
          drivers.add(driver);
      } else {
          System.out.println("Skipping duplicate driver: " + driver.getId());
      }
  }
}
public void pickup(String driverId) {
  Driver driver = findDriverById(driverId);

  boolean requestPickedUp = false;

  int driverZone = determineZone(driver.getAddress());

  if (driverZone < 0 || driverZone >= NUM_ZONES) {
      System.out.println("Invalid zone for driver: " + driverId);
      return;
  }

  if (!serviceQueues[driverZone].isEmpty()) {
      UberService service = serviceQueues[driverZone].poll();
      driver.setService(service);
      driver.setStatus(Driver.Status.DRIVING);
      driver.setAddress(service.getFrom());
      System.out.println("Driver " + driverId + " Picking Up in Zone " + driverZone);
      requestPickedUp = true;
  }

  if (!requestPickedUp) {
      System.out.println("No Service Request in Zone " + driverZone);
  }
}

public int determineZoneForDriver(String address) {
  return CityMap.getCityZone(address);
}

private Driver findDriverById(String driverId) {
  for (Driver driver : drivers) {
      if (driver.getId().equals(driverId)) {
          return driver;
      }
  }
  throw new DriverNotFoundException("Driver not found with ID: " + driverId);
}

public int determineZone(String address) {
  return CityMap.getCityZone(address);
}

public void driveTo(String driverId, String address) {
  for (Driver driver : drivers) {
      if (driver.getId().equals(driverId)) {
          if (!CityMap.validAddress(address)) {
              throw new InvalidAddressException("Invalid address: " + address);
          }

          if (driver.getStatus() == Driver.Status.AVAILABLE) {
              driver.setAddress(address);
              int newZone = determineZone(address);
              if (newZone != driver.getZone()) {
                  driver.setZone(newZone);
              }
              return;
          } else {
              throw new IllegalStateException("Driver is not available");
          }
      }
  }
  throw new DriverNotFoundException("Driver not found with ID: " + driverId);
}

  public void sortByUserName() {
    List<Map.Entry<String, User>> userList = new ArrayList<>(users.entrySet());
    Collections.sort(userList, new Comparator<Map.Entry<String, User>>() {
        @Override
        public int compare(Map.Entry<String, User> entry1, Map.Entry<String, User> entry2) {
            return entry1.getValue().getName().compareTo(entry2.getValue().getName());
        }
    });
    for (Map.Entry<String, User> entry : userList) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
    }
}
  private class NameComparator implements Comparator<User>
  {
    public int compare(User a, User b)
    {
      return a.getName().compareTo(b.getName());
    }
  }

  public void sortByWallet() {
    List<User> userList = new ArrayList<User>(users.values());
    Collections.sort(userList, new UserWalletComparator());
    for (User user : userList) {
        user.printInfo();
        System.out.println();
    }
}

  private class UserWalletComparator implements Comparator<User>
  {
    public int compare(User a, User b)
    {
      if (a.getWallet() > b.getWallet()) return 1;
      if (a.getWallet() < b.getWallet()) return -1; 
      return 0;
    }
  }

  public void sortByDistance()
  {
    Collections.sort(serviceRequests);
    listAllRequests();
  }

  static class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(String message) {
        super(message);
    }
}

static class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

static class InvalidAddressException extends RuntimeException {
    public InvalidAddressException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class InsufficientTravelDistanceException extends RuntimeException {
    public InsufficientTravelDistanceException(String message) {
        super(message);
    }
}

class NoDriversAvailableException extends RuntimeException {
    public NoDriversAvailableException(String message) {
        super(message);
    }
}

class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}

class DuplicateDriverException extends RuntimeException {
    public DuplicateDriverException(String message) {
        super(message);
    }
}

class ServiceRequestNotFoundException extends RuntimeException {
    public ServiceRequestNotFoundException(String message) {
        super(message);
    }
}

public void registerNewDriver(String name, String carModel, String carLicencePlate, String address) {

  if (name == null || name.equals("")) {
      throw new IllegalArgumentException("Invalid Driver Name " + name);
  }
  if (carModel == null || carModel.equals("")) {
      throw new IllegalArgumentException("Invalid Car Model " + carModel);
  }
  if (carLicencePlate == null || carLicencePlate.equals("")) {
      throw new IllegalArgumentException("Invalid Car Licence Plate " + carLicencePlate);
  }
  if (address == null || address.equals("")) {
      throw new InvalidAddressException("Invalid Driver Address " + address);
  }
  Driver driver = new Driver(generateDriverId(), name, carModel, carLicencePlate, Driver.Status.AVAILABLE, address);
  if (driverExists(driver)) {
      throw new DuplicateDriverException("Driver Already Exists in System");
  }
  drivers.add(driver);
}
}

