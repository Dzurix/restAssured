package stepDefinitions;

import io.cucumber.java.Before;

public class Hooks {
	
	
	
	@Before("@DeletePlace")
	
	public void beforeScenario() throws Throwable  {
		
		//execute this code only when place_id is null
		
		
		//write a code that will give you place_id
		
		stepDefinitions m = new stepDefinitions();
		
		if(stepDefinitions.place_id == null)
		
		//	if(m.place_id == null)  // MOZEMO I PREKO OBJEKTA Ali posto je place_id STATIC varijabla, onda najbolje pozivati preko KLASE
			//  I TO MOZEMO POZIVATI PREKO KLASE SAMO ako je varijabla STATIC
		{
		
		m.add_place_payload_with_something_something_something("Sheety", "French", "Asia");
		
		m.user_calls_with_http_request("AddPlaceAPI", "POST");
		
		m.verify_place_id_created_maps_to_ising("Sheety", "GetPlaceAPI");
		
		}
		
	}

}
