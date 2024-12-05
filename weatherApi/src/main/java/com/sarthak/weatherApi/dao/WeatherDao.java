package com.sarthak.weatherApi.dao;


import com.sarthak.weatherApi.model.Location;
import com.sarthak.weatherApi.model.LocationInfo;
import com.sarthak.weatherApi.model.WeatherResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WeatherDao extends JpaRepository<LocationInfo, Integer> {
    @Query("SELECT li FROM LocationInfo li WHERE li.pincode = :pincode AND :date IS NOT NULL")
    WeatherResponse findByLocationInfoPincodeAndDate(Integer pincode, LocalDate date);

}