package group_id;

import Files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.AssertJUnit;

public class ComplexJsonParse {

  public static void main(String[] args) {
    JsonPath js = new JsonPath(payload.CoursePrice()); //mokovanje responsa

    // Print num of courses returned bi API

    int count = js.getInt("courses.size()");

    System.out.println(count);

    // Print purchase ammount
    int purchaseAmount = js.getInt("dashboard.purchaseAmount");

    System.out.println(purchaseAmount);

    //Print title of first course
    String firstTitle = js.getString("courses[0].title");

    System.out.println(firstTitle);
    //  4. Print All course titles and their respective prices

    for (int i = 0; i < count; i++) {
      String courseTitles = js.get("courses[" + i + "].title");
      Integer coursePrices = js.get("courses[" + i + "].price");

      System.out.println(courseTitles);
      System.out.println(coursePrices);
    }
    // 5. Print no of copies sold by RPA Course

    for (int i = 0; i < count; i++) {
      String courseTitles = js.get("courses[" + i + "].title");

      Integer numOfCopies = js.get("courses[" + i + "].copies");

      if (courseTitles.contains("RPA")) {
        System.out.println(numOfCopies);
        break; // prekidamo cim nadje
      }
    }
    // 6. Verify if Sum of all Course prices matches with Purchase Amount
    int sum = 0;
    for (int i = 0; i < count; i++) {
      int coursePrices = js.get("courses[" + i + "].price");

      int numOfCopies = js.get("courses[" + i + "].copies");

      int amount = coursePrices * numOfCopies;
      System.out.println(amount);

      sum = sum + amount;
    }

    System.out.println(sum);

    Assert.assertEquals(sum, purchaseAmount);
  }
}
