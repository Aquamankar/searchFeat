package com.serchwithphoto.repository;


import com.serchwithphoto.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    // Add custom query methods for searching here if needed

    List<Place> findByPlaceNameContainingIgnoreCaseAndCityContainingIgnoreCaseAndAppDistanceFromCityAndBestTimeToVisitContainingIgnoreCase(
            String placeName, String city, double distanceFromCity, String bestTimeToVisit
    );

}
