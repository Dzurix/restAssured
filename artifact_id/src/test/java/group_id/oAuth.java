package group_id;

import static io.restassured.RestAssured.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.GetCourse;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class oAuth {

  public static void main(String[] args) {
    // automatizujem u browseru dobijanje Authorization koda

    // ALI OVO SAD NE RADI ZBOG GOOGLE UPDATE SECURITY I NE MOZE SE AUTOMATIZOVATI LOGOVANJE

    // // WebDriverManager.chromedriver().setup();
    // // WebDriver driver = new ChromeDriver();

    // // driver.get(
    // //   "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php"
    // // );
    // // driver
    // //   .findElement(By.cssSelector("input[type='email']"))
    // //   .sendKeys("srinath19830@gmail.com");

    // // driver
    // //   .findElement(By.cssSelector("input[type='email']"))
    // //   .sendKeys(Keys.ENTER);
    // // Thread.sleep(3000);

    // // driver
    // //   .findElement(By.cssSelector("input[type='password']"))
    // //   .sendKeys("123456");
    // // driver
    // //   .findElement(By.cssSelector("input[type='password']"))
    // //   .sendKeys(Keys.ENTER);

    // // Thread.sleep(4000);

    // // String url = driver.getCurrentUrl();

    // A)
    //POSTO SE NE MOZE AUTOMATIZOVATI URL DOBIJAM RUCNO U BROWSERU KADA SE PRIJAVIM NA GMAIL ACCOUNT
    // I ONDA IZVLACIM AUTHORIZATION CODE

    String url =
      "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AWgavdcZgcE5sMy7Bxxig48pjsvXY2F5TNEjWNjeeLF0Vq56gTqJuvfR537QNhpmEqVewA&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=3&prompt=consent";
    String partialCode = url.split("code=")[1];
    String code = partialCode.split("&scope")[0];

    System.out.println(code); // Get Authorization code

    // B) Dobijanje access_tokena

    String accessTokenResponse = given()
      .urlEncodingEnabled(false) //OVO OBAVEZNO posto REST ASSURED ne prepoznaju specijalne karaktere iz AUTHORIZATION CODE
      .queryParams("code", code)
      .queryParams(
        "client_id",
        "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com"
      )
      .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
      .queryParams(
        "redirect_uri",
        "https://rahulshettyacademy.com/getCourse.php"
      )
      .queryParams("grant_type", "authorization_code")
      .when()
      .log()
      .all()
      .post("https://www.googleapis.com/oauth2/v4/token")
      .asString();

    JsonPath js = new JsonPath(accessTokenResponse);

    String accessToken = js.getString("access_token");

    // C) ubacivanje access_tokena

//    String response = given()
//      .queryParam("access_token", accessToken)
//      .when()
//      .get("https://rahulshettyacademy.com/getCourse.php")
//      .asString();
//
//    System.out.println(response); //posto ne mozemo printati RAW response, uvek ga konvertujemo u String
    
    //OVO JE DRUGI NACIN PREKO POJO CLASE
    
    GetCourse gc = given().
    		queryParam("access_token", accessToken).
    		expect().defaultParser(Parser.JSON).// ovo ubacujemo posto je CONTENT TYPE za ovaj primer text/html, 
    		//pa zato moramo naglasiti da se salje JSON
    		when().
    		get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
    
   System.out.println(gc.getLinkedin());
   System.out.println(gc.getInstructor());
  
    
    
  }
}
