package com.techpixe.serviceImpl;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.json.JSONArray;
import org.json.JSONObject;
import com.techpixe.entity.Location;

@Service
public class GeocodingService {

    private static final String API_HOST = "india-pincode-with-latitude-and-longitude.p.rapidapi.com";
    private static final String API_KEY = "92dd61a3abmsh30aff286f1de18bp19ce46jsnd13651517f4e";

    public Location getLatLongFromPincode(Long pincode) {
        String url = "https://" + API_HOST + "/api/v1/pincode/" + pincode;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Host", API_HOST);
        headers.set("X-RapidAPI-Key", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return parseLocationResponse(response.getBody());
        } else {
            System.out.println("Failed to fetch location details: " + response.getStatusCode());
            return null;
        }
    }

    private Location parseLocationResponse(String response) {
        JSONArray jsonArray = new JSONArray(response);
        if (jsonArray.length() > 0) {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            Location location = new Location();
            location.setLatitude(jsonObject.getDouble("lat"));
            location.setLongitude(jsonObject.getDouble("lng"));
            return location;
        } else {
            System.out.println("No location details found for the provided pincode.");
            return null;
        }
    }
}
