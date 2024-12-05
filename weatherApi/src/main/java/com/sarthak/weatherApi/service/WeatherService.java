package com.sarthak.weatherApi.service;

import com.sarthak.weatherApi.dao.WeatherDao;
import com.sarthak.weatherApi.model.Location;
import com.sarthak.weatherApi.model.LocationInfo;
import com.sarthak.weatherApi.model.WeatherResponse; // Make sure to import your WeatherResponse model
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class WeatherService {

    private final GeolocationService geolocationService;
    private final RestTemplate restTemplate;

    @Autowired
    WeatherDao weatherDao;


    @Autowired
    public WeatherService(GeolocationService geolocationService, RestTemplate restTemplate) {
        this.geolocationService = geolocationService;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?
            > getWeatherByPincode(String code, LocalDate date) {
        ResponseEntity<Location> coordinatesResponse = geolocationService.getCoordinatesFromPincode(code);
        double lat = 0.0;
        double lng = 0.0;
        final String apiKey = "1d3fc911c48c389ca08edb3cc720ca74";
        if (coordinatesResponse.getStatusCode().is2xxSuccessful() && coordinatesResponse.getBody() != null) {
            Location location = coordinatesResponse.getBody();
            lat = location.getLat();
            lng = location.getLon();
            Integer pinCode = Integer.valueOf(code);
            LocationInfo locationInfo = new LocationInfo(pinCode, lat, lng);

            if (weatherDao.existsById(pinCode)) {
                return ResponseEntity.status(200).body(weatherDao.findByLocationInfoPincodeAndDate(pinCode, date));
            }

            String url = "https://api.openweathermap.org/data/3.0/onecall/day_summary?lat=" + lat + "&lon=" + lng + "&date=" + date + "&appid=" + apiKey;

            ResponseEntity<WeatherResponse> weatherResponse = restTemplate.getForEntity(url, WeatherResponse.class);
            if (weatherResponse.getStatusCode().is2xxSuccessful() && weatherResponse.getBody() != null) {
                weatherDao.save(locationInfo);
                return weatherResponse;
            } else {
                return ResponseEntity.status(weatherResponse.getStatusCode()).body(null); // Handle error response from weather API
            }

        } else {
            return ResponseEntity.status(coordinatesResponse.getStatusCode()).body(null); // Handle error response from geolocation service
        }
    }
}
