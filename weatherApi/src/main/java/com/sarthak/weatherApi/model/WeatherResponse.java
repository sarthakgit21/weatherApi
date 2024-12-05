package com.sarthak.weatherApi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "weather_response")
@Data
public class WeatherResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long id; // Primary Key

    private double lat;
    private double lon;
    private String tz;
    @Column(unique = true, nullable = false)
    private LocalDate date;
    private String units;


    @OneToOne
    @JoinColumn(name = "pincode", referencedColumnName = "pincode") // Foreign key referencing pincode
    private LocationInfo locationInfo;


    @Embedded
    private Temperature temperature;

    @Embedded
    private Wind wind;





    @Data
    @Embeddable
    public static class Temperature {
        private double min;
        private double max;
        private double afternoon;
        private double night;
        private double evening;
        private double morning;
    }

    @Data
    @Embeddable
    public static class Wind {
        private Max max;

        @Data
        @Embeddable
        public static class Max {
            private double speed;
            private int direction;
        }
    }
}
