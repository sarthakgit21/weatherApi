package com.sarthak.weatherApi.service;

import com.sarthak.weatherApi.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GeolocationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeolocationService geolocationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCoordinatesFromPincode_Success() {
        // Arrange
        String pincode = "123456";
        String apiKey = "1d3fc911c48c389ca08edb3cc720ca74";
        String url = "http://api.openweathermap.org/geo/1.0/zip?zip=" + pincode + ",IN&appid=" + apiKey;

        // Mock response
        Location mockLocation = new Location();
        mockLocation.setName("Test City");
        mockLocation.setLat(10.0);
        mockLocation.setLon(20.0);
        mockLocation.setCountry("IN");

        ResponseEntity<Location> mockResponse = new ResponseEntity<>(mockLocation, HttpStatus.OK);
        when(restTemplate.getForEntity(url, Location.class)).thenReturn(mockResponse);

        // Act
        ResponseEntity<Location> response = geolocationService.getCoordinatesFromPincode(pincode);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockLocation, response.getBody());
        assertEquals("Test City", response.getBody().getName());
        assertEquals(10.0, response.getBody().getLat());
        assertEquals(20.0, response.getBody().getLon());

        // Verify interactions
        verify(restTemplate, times(1)).getForEntity(url, Location.class);
    }

    @Test
    void testGetCoordinatesFromPincode_NotFound() {
        // Arrange
        String pincode = "123456";
        String apiKey = "1d3fc911c48c389ca08edb3cc720ca74";
        String url = "http://api.openweathermap.org/geo/1.0/zip?zip=" + pincode + ",IN&appid=" + apiKey;

        // Mock response for not found
        ResponseEntity<Location> mockResponse = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        when(restTemplate.getForEntity(url, Location.class)).thenReturn(mockResponse);

        // Act
        ResponseEntity<Location> response = geolocationService.getCoordinatesFromPincode(pincode);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

        // Verify interactions
        verify(restTemplate, times(1)).getForEntity(url, Location.class);
    }
}
