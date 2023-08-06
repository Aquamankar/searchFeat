package com.serchwithphoto.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Change the data type to long
    private String city;
    private String placeName;
    private String overview;
    private double appDistanceFromCity;
    private String bestTimeToVisit;

    @Lob
    private byte[] photo;

    // Constructors, getters, and setters
}
