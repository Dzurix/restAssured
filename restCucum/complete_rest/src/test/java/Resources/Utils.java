package Resources;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

public class Utils {

  RequestSpecification req;

  public RequestSpecification requestSpecification() throws IOException {
    PrintStream log = new PrintStream(new FileOutputStream("logging.txt")); //kreiranje fajla gde ce se sve logovati

    // RestAssured.baseURI = "https://rahulshettyacademy.com";

    // kreiranje REQUEST
    req =
      new RequestSpecBuilder()
        .setBaseUri(getGlobalValue("baseUrl"))
        .addQueryParam("key", "qaclick123")
        .addFilter(RequestLoggingFilter.logRequestTo(log)) // OVO je umesto log().all() pomocu kojeg se LOGUJE REQUEST
        .addFilter(ResponseLoggingFilter.logResponseTo(log)) // ovo je logovanje RESPONSA
        .setContentType(ContentType.JSON)
        .build();

    return req;
  }

  // OVAJ METOD KORISTIM DA KAZEM GDE SE NALAZI global.properties FILE SA podacima
  public static String getGlobalValue(String key) throws IOException {
    Properties prop = new Properties();
    //ovde govorim gde se nalazi inputFIle

    FileInputStream fis = new FileInputStream(
      "D:/cy/restAssured/restCucum/complete_rest/src/test/java/Resources/global.properties"
    );

    prop.load(fis); // povezujem ih

    return prop.getProperty(key);
  }
}
