package com.sarthak.weatherApi.service;

import com.sarthak.weatherApi.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeolocationService {

    private final RestTemplate restTemplate;
    private final String apiKey = "1d3fc911c48c389ca08edb3cc720ca74"; // Replace with your actual API key

    @Autowired
    public GeolocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Location> getCoordinatesFromPincode(String pincode) {
        String url = "http://api.openweathermap.org/geo/1.0/zip?zip=" + pincode + ",IN&appid=" + apiKey;
System.out.println(url);
        // Call the API
        ResponseEntity<Location> response = restTemplate.getForEntity(url, Location.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Location location = response.getBody();
            return ResponseEntity.ok(location);
        } else {
            return ResponseEntity.notFound().build(); // Not found
        }
    }
}
