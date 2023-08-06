package com.serchwithphoto.controller;


import com.serchwithphoto.entity.Place;
import com.serchwithphoto.payload.PlaceDto;
import com.serchwithphoto.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }



    @PostMapping
    public ResponseEntity<PlaceDto> addPlaceDetails(@RequestBody PlaceDto placeDto) {
        PlaceDto createdPlace = placeService.addPlace(placeDto);
        return ResponseEntity.ok(createdPlace);
    }
    @GetMapping("/search")
    public List<PlaceDto> searchPlaces(@RequestParam(required = false) String placeName,
                                    @RequestParam(required = false) String city,
                                    @RequestParam(required = false) Double distanceFromCity,
                                    @RequestParam(required = false) String bestTimeToVisit) {
        return placeService.searchPlaces(placeName, city, distanceFromCity, bestTimeToVisit);
    }
    //http://localhost:8080/places/3/upload
    @PostMapping("/{placeId}/upload")
    public ResponseEntity<String> uploadPhoto(@PathVariable long placeId, @RequestParam("photo") MultipartFile photo) {
        try {
            placeService.uploadPhoto(placeId, photo);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload photo: " + e.getMessage());
        }
    }



    //http://localhost:8080/places/3/photos
    @GetMapping("/{placeId}/photos")
    public ResponseEntity<byte[]> getPhoto(@PathVariable long placeId) {
        byte[] photoData = placeService.getPhotoById(placeId);
        if (photoData != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type as per your photo type
            return new ResponseEntity<>(photoData, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
