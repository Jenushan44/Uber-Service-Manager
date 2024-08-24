import java.util.Arrays;
import java.util.Scanner;

public class CityMap
{

  private static boolean allDigits(String s)
  {
    for (int i = 0; i < s.length(); i++)
      if (!Character.isDigit(s.charAt(i)))
        return false;
    return  true;
  }

  private static String[] getParts(String address)
  {
    String parts[] = new String[3];
    
    if (address == null || address.length() == 0)
    {
      parts = new String[0];
      return parts;
    }
         
    int numParts = 0;
    Scanner sc = new Scanner(address);
    while (sc.hasNext())
    {
      if (numParts >= 3)
        parts = Arrays.copyOf(parts, parts.length+1);

      parts[numParts] = sc.next();
      numParts++;
    }
    if (numParts == 1)
      parts = Arrays.copyOf(parts, 1);
    else if (numParts == 2)
      parts = Arrays.copyOf(parts, 2);
    return parts;
  }

  public static boolean validAddress(String address)
  {
    int[] block = {-1, -1};

    String[] parts = getParts(address);
    
    if (parts.length != 3)
      return false;
    
    boolean streetType = false; 
    
    if (!parts[2].equalsIgnoreCase("street") && !parts[2].equalsIgnoreCase("avenue"))
      return false;

      if (parts[2].equalsIgnoreCase("street"))
      streetType = true;

    if (!allDigits(parts[0]) || parts[0].length() != 2)
      return false;

    int num1 = Integer.parseInt(parts[0])/10;
    if (num1 == 0) return false;
   
    String suffix = parts[1].substring(1);
    if (parts[1].length() != 3) 
      return false;
   
    if (!suffix.equals("th") && !parts[1].equals("1st") &&
        !parts[1].equals("2nd") && !parts[1].equals("3rd"))
      return false;

    String digitStr = parts[1].substring(0, 1);
    if (!allDigits(digitStr))
      return false;
    int num2 = Integer.parseInt(digitStr);
    if (num2 == 0)
      return false;
    if (streetType)
    {
      block[0] = num2;
      block[1] = num1;
    }
    else
    {
      block[0] = num1;
      block[1] = num2;
    }
    return true;
  }

  public static int[] getCityBlock(String address)
  {
    int[] block = {-1, -1};
    boolean streetType = false; 

    String[] parts = getParts(address);

    if (parts[2].equalsIgnoreCase("street"))
      streetType = true;

    int num1 = Integer.parseInt(parts[0])/10;
    int num2 = Integer.parseInt(parts[1].substring(0, 1));
    if (streetType)
    {
      block[0] = num2;
      block[1] = num1;
    }
    else
    {
      block[0] = num1;
      block[1] = num2;
    }
    return block;
  }
  
  public static int getDistance(String from, String to)
  {
    int[] fromblock = {0, 0};
    int[] toblock   = {0, 0};

    if (validAddress(from) && validAddress(to))
    {
      fromblock = getCityBlock(from);
      toblock   = getCityBlock(to);
    }
    return Math.abs(toblock[0] - fromblock[0]) + Math.abs(toblock[1] - fromblock[1]) ;
  }
  public int determineZone(String address) {
    return CityMap.getCityZone(address);
  }

  public static int getCityZone(String address) {
    if (!validAddress(address)) {
      return -1;
    }

    int[] block = getCityBlock(address);
    int avenue = block[1];
    int street = block[0];

    if (avenue >=1 && avenue <= 5 && street >=6 && street <= 9) {
      return 0;
    } else if (avenue >= 6 && avenue <= 9 && street >= 6 && street <=9) {
      return 1;
    } else if (avenue >= 6 && avenue <= 9 && street >= 1 && street <= 5) {
      return 2; 
    } else if (avenue >= 1 && avenue <= 5 && street >= 1 && street <= 5) {
      return 3; 
    } else {
      return -1; 
    }
  }
}
