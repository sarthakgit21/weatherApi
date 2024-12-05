package com.sarthak.weatherApi.contollers;

import com.sarthak.weatherApi.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
public class WeatherApi {
    public WeatherApi(WeatherService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    @Autowired
    WeatherService weatherApiService;
    @GetMapping("/weather/{code}")
    public ResponseEntity<?> getWeatherByCity(@PathVariable("code") String code,@RequestParam("date") String dateStr) {
        try {
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Parse the date
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return weatherApiService.getWeatherByPincode(code,date);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd.");
        }

    }
}