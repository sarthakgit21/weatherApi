package com.sarthak.weatherApi.service;

import com.sarthak.weatherApi.dao.WeatherDao;
import com.sarthak.weatherApi.model.Location;
import com.sarthak.weatherApi.model.LocationInfo;
import com.sarthak.weatherApi.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private GeolocationService geolocationService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WeatherDao weatherDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWeatherByPincode_WhenWeatherDataExistsInDb() {
        String pincode = "12345";
        LocalDate date = LocalDate.now();

        // Mocking geolocation response
        Location location = new Location("New York", 40.7128, -74.0060, "US");
        when(geolocationService.getCoordinatesFromPincode(pincode))
                .thenReturn(ResponseEntity.ok(location));

        location.setLat(10.0);
        location.setLon(20.0);

        when(geolocationService.getCoordinatesFromPincode(pincode))
                .thenReturn(ResponseEntity.ok(location));
        WeatherResponse weatherResponse=new WeatherResponse();
        LocationInfo locationInfo = new LocationInfo(Integer.parseInt(pincode), 10.0, 20.0);
        when(weatherDao.existsById(Integer.parseInt(pincode))).thenReturn(true);
        when(weatherDao.findByLocationInfoPincodeAndDate(Integer.parseInt(pincode), date)).thenReturn(weatherResponse);


        // Call the method
        ResponseEntity<?> response = weatherService.getWeatherByPincode(pincode, date);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locationInfo, response.getBody());

        // Verifications
        verify(geolocationService, times(1)).getCoordinatesFromPincode(pincode);
        verify(weatherDao, times(1)).existsById(Integer.parseInt(pincode));
        verify(weatherDao, times(1)).findByLocationInfoPincodeAndDate(Integer.parseInt(pincode), date);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void testGetWeatherByPincode_WhenWeatherDataDoesNotExistInDb() {
        String pincode = "12345";
        LocalDate date = LocalDate.now();
        String apiResponse = """
    {
        "lat": 33,
        "lon": 35,
        "tz": "+02:00",
        "date": "2020-03-04",
        "units": "standard",
        "cloud_cover": {
            "afternoon": 0
        },
        "humidity": {
            "afternoon": 33
        },
        "precipitation": {
            "total": 0
        },
        "temperature": {
            "min": 286.48,
            "max": 299.24,
            "afternoon": 296.15,
            "night": 289.56,
            "evening": 295.93,
            "morning": 287.59
        },
        "pressure": {
            "afternoon": 1015
        },
        "wind": {
            "max": {
                "speed": 8.7,
                "direction": 120
            }
        }
    }
    """;

        // Mocking geolocation response
        Location location = new Location("New York", 40.7128, -74.0060, "US");
        when(geolocationService.getCoordinatesFromPincode(pincode))
                .thenReturn(ResponseEntity.ok(location));

        // Mocking database response
        when(weatherDao.existsById(Integer.parseInt(pincode))).thenReturn(false);

        // Mocking REST API response
        String url = "https://api.openweathermap.org/data/3.0/onecall/day_summary?lat=10.0&lon=20.0&date=" + date + "&appid=1d3fc911c48c389ca08edb3cc720ca74";
        when(restTemplate.getForObject(url, String.class)).thenReturn(apiResponse);

        // Call the method
        ResponseEntity<?> response = weatherService.getWeatherByPincode(pincode, date);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());

        // Verifications
        verify(geolocationService, times(1)).getCoordinatesFromPincode(pincode);
        verify(weatherDao, times(1)).existsById(Integer.parseInt(pincode));
        verify(restTemplate, times(1)).getForObject(url, String.class);
    }

    @Test
    void testGetWeatherByPincode_WhenGeolocationServiceFails() {
        String pincode = "12345";
        LocalDate date = LocalDate.now();

        // Mocking geolocation response failure
        when(geolocationService.getCoordinatesFromPincode(pincode))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));

        // Call the method
        ResponseEntity<?> response = weatherService.getWeatherByPincode(pincode, date);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());

        // Verifications
        verify(geolocationService, times(1)).getCoordinatesFromPincode(pincode);
        verifyNoInteractions(weatherDao);
        verifyNoInteractions(restTemplate);
    }
}
