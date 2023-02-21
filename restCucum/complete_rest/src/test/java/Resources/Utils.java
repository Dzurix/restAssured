package Resources;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

public class Utils {

public static RequestSpecification req; //sada kada smo rekli da je ova varijabla STATIC
// to znaci NE KREIRAJ NOVU INSTANCU vec ovu varijablu koristi kod svih TEST CASE, nemoj je vracati stalno na NULL prilikom sledeceg testa

  public RequestSpecification requestSpecification() throws IOException {
	  
	  if(req==null) {
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
  
  //Ovaj metod koristim da izvucem vrednost iz JSONpath, da ne pisem stalno js.get (JSONpath)
  
  public String getJsonPath(Response response, String key) {
	  
	  String resp = response.asString();
	   JsonPath js = new JsonPath(resp);
	  return js.get(key).toString();
  }

}
