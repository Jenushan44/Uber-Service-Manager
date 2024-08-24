public class UberRide extends UberService
{
  private int numPassengers;
  private boolean requestedXL;
  
  public static final String TYPENAME = "RIDE";
  
  public UberRide(Driver driver, String from, String to, User user, int distance, double cost)
  {
    super(driver, from, to, user, distance, cost, UberRide.TYPENAME);
    requestedXL = false;
    numPassengers = 1;
  }
  
  public String getServiceType()
  {
    return TYPENAME;
  }

  public int getNumPassengers()
  {
    return numPassengers;
  }

  public void setNumPassengers(int numPassengers)
  {
    this.numPassengers = numPassengers;
  }

  public boolean isRequestedXL()
  {
    return requestedXL;
  }

  public void setRequestedXL(boolean requestedXL)
  {
    this.requestedXL = requestedXL;
  }
}
