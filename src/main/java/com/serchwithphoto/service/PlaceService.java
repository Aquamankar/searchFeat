package com.serchwithphoto.service;


import com.serchwithphoto.payload.PlaceDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PlaceService {
    PlaceDto addPlace(PlaceDto placeDto);
    List<PlaceDto> searchPlaces(String placeName, String city, double distanceFromCity, String bestTimeToVisit);
    byte[] getPhotoById(long placeId);

    void uploadPhoto(long placeId, MultipartFile photo) throws IOException;

}
