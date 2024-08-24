import java.util.Objects;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

abstract public class UberService implements Comparable<UberService>
{
  private Driver driver;   
  private String from;
  private String to;
  private User user;
  private String type;  
  private int distance; 
  private double cost;  
  private List<Driver> drivers;

  public UberService(Driver driver, String from, String to, User user, int distance, double cost, String type)
  {
    this.driver = driver;
    this.from = from;
    this.to = to;
    this.user = user;
    this.distance = distance;
    this.cost = cost;
    this.type = type;
    this.distance = 0;
    this.drivers = new ArrayList<>();
  }

  abstract public String getServiceType();

  public Driver getDriver()
  {
    return driver;
  }
  public void setDriver(Driver driver)
  {
    this.driver = driver;
  }
  public String getFrom()
  {
    return from;
  }
  public void setFrom(String from)
  {
    this.from = from;
  }
  public String getTo()
  {
    return to;
  }
  public void setTo(String to)
  {
    this.to = to;
  }
  public User getUser()
  {
    return user;
  }
  public void setUser(User user)
  {
    this.user = user;
  }
  public int getDistance()
  {
    return distance;
  }
  public void setDistance(int distance)
  {
    this.distance = distance;
  }
  public double getCost()
  {
    return cost;
  }
  public void setCost(double cost)
  {
    this.cost = cost;
  }

  public int compareTo(UberService other)
  {
    return this.distance - other.distance;
  }
  
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    UberService that = (UberService) other;
    return distance == that.distance &&
            Double.compare(that.cost, cost) == 0 &&
            Objects.equals(from, that.from) &&
            Objects.equals(to, that.to) &&
            Objects.equals(user, that.user) &&
            Objects.equals(type, that.type);
}
  
  public void printInfo() {
    System.out.printf("\nType: %-9s From: %-15s To: %-15s", type, from, to);
    System.out.print("\nUser: ");
    user.printInfo();
  }
}
