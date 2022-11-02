package com.example.mySpringProject.service;

import com.example.mySpringProject.models.DepartureModel;
import com.example.mySpringProject.strategy.DepartureFilteringStrategy;
import com.example.mySpringProject.strategy.UriCreationStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DepartureFetchingService {

    public static final String ALVIK_STATION_ID = "740020755";
    public static final String STORA_MOSSEN_STATION_ID = "740021690";
    public static final String KRISTINEBERG_STATION_ID = "740021663";

    @Autowired
    UriCreationStrategy uriCreationStrategy;

    @Autowired
    DepartureFilteringStrategy departureFilteringStrategy;

    public String getCatFactFromApi() {
        String uri ="https://catfact.ninja/fact";
        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }


    /**
     *
     get the ID of the T-bana
     https://api.resrobot.se/v2.1/location.name?input=Alvik&format=json&accessId=ce7b1956-d486-4372-b06e-548fd918ac7d
     extId":"740020755","name":"Alvik T-bana
     "extId":"740021690","name":"Stora Mossen T-bana
     "extId":"740021663","name":"Kristineberg T-bana

     Here is a URI of the trips FROM Alvik TO Kristineberg -->
     https://api.resrobot.se/v2.1/trip?format=json&originId=740020755&destId=740021663&passlist=false&showPassingPoints=false&accessId=ce7b1956-d486-4372-b06e-548fd918ac7d

     The JSON returntype includes an Array Called "Trip"
     Each entry in the "Trip" includes an object that has an "Origin" Object.
     The Origin Object has an attribute "time"
     "Time" is the departure from the station. This is what we are interested in.
     */
    public List<DepartureModel> getTheNextEmptyTrain() {

        List<DepartureModel> alvikTrains = this.getDepartures(ALVIK_STATION_ID, KRISTINEBERG_STATION_ID);
        List<DepartureModel> storaMossenTrains = this.getDepartures(STORA_MOSSEN_STATION_ID, KRISTINEBERG_STATION_ID);

        int i = 0;
        // I can only get 6 results at a time. This loops let me get 6 * X results by making multiple calls - then concatenating the lists.
        while(i < 2){
            // ALVIK
            DepartureModel lastAlvikDeparture = alvikTrains.get(alvikTrains.size()-1);
            alvikTrains = Stream.concat(alvikTrains.stream(), this.getDepartures(ALVIK_STATION_ID, KRISTINEBERG_STATION_ID, lastAlvikDeparture.getDepartureTime().plusMinutes(1)).stream()).collect(Collectors.toList());

            //STORA MOSSEN
            DepartureModel lastStoraMossenDeparture = storaMossenTrains.get(storaMossenTrains.size()-1);
            storaMossenTrains = Stream.concat(storaMossenTrains.stream(), this.getDepartures(STORA_MOSSEN_STATION_ID, KRISTINEBERG_STATION_ID, lastStoraMossenDeparture.getDepartureTime().plusMinutes(1)).stream()).collect(Collectors.toList());

            i++;
        }
        System.out.println("Last departure Alvik - " + alvikTrains.get(alvikTrains.size()-1).getDepartureTime());
        System.out.println("Last departure Stora Mossen - " + storaMossenTrains.get(storaMossenTrains.size()-1).getDepartureTime());
        System.out.println("Alvik trains size - " + alvikTrains.size());
        System.out.println("Stora Mossen trains size - " + storaMossenTrains.size());
        return departureFilteringStrategy.filterDepartures(alvikTrains, storaMossenTrains);
     //   return Stream.concat(alvikTrains.stream(), storaMossenTrains.stream()).collect(Collectors.toList());
    }

    private List<DepartureModel> getDepartures(final String _from, String _to){
        final String alvikUri = uriCreationStrategy.createUri(_from, _to);
        RestTemplate restTemplate = new RestTemplate();
        JsonNode result = restTemplate.getForObject(alvikUri, JsonNode.class);
        return extractJsonDepartureList(result);
    }

    private List<DepartureModel> getDepartures(String _from, String _to, LocalTime localTime){
        final String alvikUri = uriCreationStrategy.createUri(_from, _to, localTime);
        RestTemplate restTemplate = new RestTemplate();
        JsonNode result = restTemplate.getForObject(alvikUri, JsonNode.class);
        return extractJsonDepartureList(result);
    }

    private List<DepartureModel> extractJsonDepartureList(JsonNode _result){
        List<DepartureModel> departures = new ArrayList<DepartureModel>();
        ArrayNode tripArray = (ArrayNode) _result.get("Trip");
        for(JsonNode trip : tripArray){
            departures.add(new DepartureModel(trip.get("Origin").get("time").asText(), trip.get("Origin").get("name").asText()));
        }
        return departures;

    }


    /*
    example call : https://api.resrobot.se/v2.1/trip?
    format=json&
    originId=740000001&
    destId=740000003&
    passlist=true&
    showPassingPoints=true&
    accessId=API_KEY

https://api.resrobot.se/v2.1/trip?format=json&originId=740000001&destId=740000003&passlist=true&showPassingPoints=true&accessId=ce7b1956-d486-4372-b06e-548fd918ac7d
     */
    public void getQueryParameters(){

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("accessId", "ce7b1956-d486-4372-b06e-548fd918ac7d");
        queryParams.put("format", "json");
        queryParams.put("originId", "740000001");
        queryParams.put("destId", "740000003");
        queryParams.put("passlist", "true");
        queryParams.put("showPassingPoints", "true");






    }
}
