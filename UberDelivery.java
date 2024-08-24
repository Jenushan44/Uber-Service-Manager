public class UberDelivery extends UberService
{
  public static final String TYPENAME = "DELIVERY";
 
  private String restaurant; 
  private String foodOrderId;
      
  public UberDelivery(Driver driver, String from, String to, User user, int distance, double cost,
                        String restaurant, String order)
  {
    super(driver, from, to, user, distance, cost, UberDelivery.TYPENAME);
    this.restaurant = restaurant;
    this.foodOrderId = order;
  }
 
  
  public String getServiceType()
  {
    return TYPENAME;
  }
  
  public String getRestaurant()
  {
    return restaurant;
  }

  public void setRestaurant(String restaurant)
  {
    this.restaurant = restaurant;
  }

  public String getFoodOrderId()
  {
    return foodOrderId;
  }

  public void setFoodOrderId(String foodOrderId)
  {
    this.foodOrderId = foodOrderId;
  }

  public boolean equals(Object other)
  {
    UberService req = (UberService)other;
    if (!req.getServiceType().equals(UberDelivery.TYPENAME))
      return false;
    
    UberDelivery delivery = (UberDelivery)other;
    return super.equals(other) && delivery.getRestaurant().equals(restaurant) && 
                                  delivery.getFoodOrderId().equals(foodOrderId);
  }

  public void printInfo()
  {
    super.printInfo();
    System.out.printf("\nRestaurant: %-9s Food Order #: %-3s", restaurant, foodOrderId); 
  }
}
