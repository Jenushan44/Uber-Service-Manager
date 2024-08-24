 import java.util.List;
 import java.util.ArrayList;

public class Driver
{
  private String id;
  private String name;
  private String carModel;
  private String carLicensePlate;
  private double wallet;
  private String type;
  private UberService service; 
  private String address;
  private int zone;
  private static List<Driver> drivers = new ArrayList<>();
  public static enum Status {AVAILABLE, DRIVING};
  private Status status;
    
  
public Driver(String id, String name, String carModel, String carLicensePlate, Status status, String address) {
  this.id = id;
  this.name = name;
  this.carModel = carModel;
  this.carLicensePlate = carLicensePlate;
  this.status = status;
  this.wallet = 0;
  this.type = "";
  this.address = address;
  this.zone = CityMap.getCityZone(address);
}

public class DriverNotFoundException extends RuntimeException {
  public DriverNotFoundException(String message) {
      super(message);
  }
}

  public void printInfo()
  {
    System.out.printf("Id: %-3s Name: %-15s Car Model: %-15s License Plate: %-10s Wallet: %2.2f", 
        id, name, carModel, carLicensePlate, wallet);
  }

  public String toString() {
    return String.format("Id: %s   Name: %s   Car Model: %s   License Plate: %s   Status: %s   Wallet: %.2f   Address: %s   Zone: %d",
            id, name, carModel, carLicensePlate, status, wallet, address, zone);
}

  public void pickup(String driverId) {
    for (Driver driver : drivers) {
        if (driver.getId().equals(driverId)) {
            driver.pickup(driverId); 
            return;
        }
    }
    throw new DriverNotFoundException("Driver not found with ID: " + driverId);
}

public void driveTo(String driverId, String address) {
    for (Driver driver : drivers) {
        if (driver.getId().equals(driverId)) {
            driver.driveTo(driverId, address);
            System.out.println("Driver " + driverId + " Dropping Off");
            return;
        }
    }
    throw new DriverNotFoundException("Driver not found with ID: " + driverId);
}

  public String getType()
  {
    return type;
  }
  public void setType(String type)
  {
    this.type = type;
  }
  public String getId()
  {
    return id;
  }
  public void setId(String id)
  {
    this.id = id;
  }
  public String getName()
  {
    return name;
  }
  public void setName(String name)
  {
    this.name = name;
  }
  public String getCarModel()
  {
    return carModel;
  }
  public void setCarModel(String carModel)
  {
    this.carModel = carModel;
  }
  public String getLicensePlate()
  {
    return carLicensePlate;
  }
  public void setLicensePlate(String licensePlate)
  {
    this.carLicensePlate = licensePlate;
  }
  public Status getStatus()
  {
    return status;
  }
  public void setStatus(Status status)
  {
    this.status = status;
  }
  

public UberService getService() {
    return service;
}

public void setService(UberService service) {
    this.service = service;
}

public String getAddress() {
    return address;
}

public void setAddress(String address) {
    this.address = address;
    this.zone = CityMap.getCityZone(address);
}

public int getZone() {
    return zone;
}

public void setZone(int zone) {
    this.zone = zone;
}


public static List<Driver> getAllDrivers() {
  return drivers;
}


public static void addDriver(Driver driver) {
  drivers.add(driver);
}

  public double getWallet()
  {
    return wallet;
  }
  public void setWallet(double wallet)
  {
    this.wallet = wallet;
  }
  
  public boolean equals(Object other)
  {
    Driver otherDriver = (Driver) other;
    return this.name.equals(otherDriver.name) && 
           this.carLicensePlate.equals(otherDriver.carLicensePlate);
  }
  
  public void pay(double fee)
  {
    wallet += fee;
  }

  public String getCarLicensePlate() {
    return carLicensePlate;
}

}
