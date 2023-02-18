package Resources;

import java.util.ArrayList;
import java.util.List;
import pojo.Location;
import pojo.addPlace;

public class TestDataBuild {

  public addPlace addPlacePayload() {
    addPlace p = new addPlace();

    p.setAccuracy(50);
    p.setAddress("Moja ulica");
    p.setLanguage("jezik");
    p.setPhone_number("+381650650652");
    p.setWebsite("https://assess.rs");
    p.setName("MojeIme");

    //setovanje tipova
    List<String> myList = new ArrayList<String>();
    myList.add("ses12");
    myList.add("ses123");
    p.setTypes(myList);

    //setovanje lokacije
    Location loc = new Location();
    loc.setLat(-38.383494);
    loc.setLng(33.427362);
    p.setLocation(loc);

    return p;
  }
}
