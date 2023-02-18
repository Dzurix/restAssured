Feature: Validating Place API's

    Scenario: Verify if place is being successfully added using AddPlaceAPI

        Given Add Place Payload
        When user calls "AddPlaceAPI" with Post http request
        Then the API call got success with status code 200
        And "status" in response body is "OK"
        And "scope" in response body is "APP"

        Examples:
            | name    | language | address            |
            | AAhouse | English  | World cross center |