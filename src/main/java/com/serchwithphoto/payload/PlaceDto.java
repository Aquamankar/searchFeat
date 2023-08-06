package com.serchwithphoto.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {
    private long id;
    private String city;
    private String placeName;
    private String overview;
    private double appDistanceFromCity;
    private String bestTimeToVisit;
    private byte[] photo;
}
