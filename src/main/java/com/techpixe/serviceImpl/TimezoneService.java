
package com.techpixe.serviceImpl;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;

@Service
public class TimezoneService {

    private static final String API_HOST = "weatherapi-com.p.rapidapi.com";
    private static final String API_KEY = "92dd61a3abmsh30aff286f1de18bp19ce46jsnd13651517f4e";

    public String getTimeZoneFromLatLong(Double latitude, Double longitude) {
        String url = "https://" + API_HOST + "/timezone.json?q=" + latitude + "," + longitude;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Host", API_HOST);
        headers.set("X-RapidAPI-Key", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            return jsonObject.getJSONObject("location").getString("tz_id");
        } else {
            			//System.out.println("Failed to fetch timezone details: " + response.getStatusCode());
            return null;
        }
    }
}

