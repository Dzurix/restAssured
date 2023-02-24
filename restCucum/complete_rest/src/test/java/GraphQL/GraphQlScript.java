package GraphQL;

import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import junit.framework.Assert;



public class GraphQlScript {
	
	
	public static void main(String args[]) {
		
		
//Query
		
		int characterId = 1155;
		int locationId = 1525;
		int episodeId = 886;
		
	String response = 	given().log().all().header("Content-Type","application/json")
		.body("{\"query\":\"query($characterId : Int!, $episodeId: Int!, $locationId: Int!){\\n  "
				+ "character(characterId: $characterId) {\\n    name\\n    gender\\n    status\\n    id\\n  }\\n  "
				+ "location(locationId: $locationId) {\\n    name\\n    dimension\\n  }\\n  episode(episodeId: $episodeId) "
				+ "{\\n    name\\n    air_date\\n    episode\\n  }\\n  characters(filters: {name: \\\"Rahul\\\"}) "
				+ "{\\n    info {\\n      count\\n    }\\n    result {\\n      name\\n      type\\n    }\\n  }\\n "
				+ " episodes(filters: {episode: \\\"hulu\\\"}) {\\n    result {\\n      id\\n      name\\n   "
				+ "   air_date\\n      episode\\n    }\\n  }\\n}\\n\","
				+ "\"variables\":{\"characterId\":" + characterId + ",\"locationId\":" + locationId + 
				",\"episodeId\":" + episodeId + "}}")
		.when().post("https://rahulshettyacademy.com/gq/graphql")
		.then().extract().response().asString();
	
	System.out.println(response);

	JsonPath js = new JsonPath(response);
	
String characterName=	js.getString("data.character.name");

Assert.assertEquals(characterName, "Dejan Hero");



// Mutations



String locationName = "YUGOSLAVIA";
String characterMutName = "Dejan is a HERO";
String episodeName = "Rise of the fallen";

String mutationResponse = 	given().log().all().header("Content-Type","application/json")
.body("{\"query\":\"mutation($locationName: String!, $characterName:String!, $episodeName: String!){\\n  \\n "
		+ " createLocation(location:{\\n    \\n    name: $locationName,\\n    type: \\\"Ses\\\", \\n    dimension:"
		+ " \\\"123\\\"\\n  })\\n  {id}\\n  \\n  createCharacter(character:{\\n    \\n    name: $characterName, \\n  "
		+ "  type: \\\"Lol\\\", \\n    status: \\\"Hero\\\", \\n    species: \\\"Ses\\\", \\n    gender: \\\"male\\\", \\n  "
		+ "  image: \\\"png\\\",\\n    originId: 1520,\\n    locationId: 1520\\n  })\\n  {id}\\n  \\n "
		+ " createEpisode(episode:{\\n    \\n    name: $episodeName ,\\n    air_date: \\\"01-01-1990\\\",\\n   "
		+ " episode: \\\"Ses\\\"\\n  })\\n  {id}\\n  \\n  deleteLocations(locationIds:[1518,1519]){\\n  "
		+ "  locationsDeleted\\n  }\\n}\",\"variables\":"
		+ "{\"locationName\":\"" + locationName + "\",\"characterName\":\""+characterMutName+"\""
		+ ",\"episodeName\":\""+episodeName+"\"}}")
.when().post("https://rahulshettyacademy.com/gq/graphql")
.then().extract().response().asString();

System.out.println(response);

JsonPath js1 = new JsonPath(mutationResponse);


	
	}

}
