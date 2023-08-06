package com.serchwithphoto.service.impl;

import com.serchwithphoto.entity.Place;
import com.serchwithphoto.payload.PlaceDto;
import com.serchwithphoto.repository.PlaceRepository;
import com.serchwithphoto.service.PlaceService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class PlaceServiceImpl  implements PlaceService {
    @Autowired
    private final PlaceRepository placeRepository;


    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public PlaceDto addPlace(PlaceDto placeDto) {
        Place newplace = mapToEntity(placeDto);
        placeRepository.save(newplace);

        return mapToDto(newplace);
    }

    private PlaceDto mapToDto(Place newplace) {

        PlaceDto dto=new PlaceDto();
        dto.setId(newplace.getId());
        dto.setCity(newplace.getCity());
        dto.setPlaceName(newplace.getPlaceName());
        dto.setOverview(newplace.getOverview());
        dto.setPhoto(newplace.getPhoto());
        dto.setBestTimeToVisit(newplace.getBestTimeToVisit());
        dto.setAppDistanceFromCity(newplace.getAppDistanceFromCity());
        return dto;
    }

    private Place mapToEntity(PlaceDto placeDto) {
        Place place=new Place();
        place.setId(placeDto.getId());
        place.setPlaceName(placeDto.getPlaceName());
        place.setCity(placeDto.getCity());
        place.setOverview(placeDto.getOverview());
        place.setBestTimeToVisit(placeDto.getBestTimeToVisit());
        place.setPhoto(placeDto.getPhoto());
        place.setAppDistanceFromCity(placeDto.getAppDistanceFromCity());
        return place;
    }


    @Override
    public List<PlaceDto> searchPlaces(String placeName, String city, double distanceFromCity, String bestTimeToVisit) {
        List<Place> places = placeRepository.findByPlaceNameContainingIgnoreCaseAndCityContainingIgnoreCaseAndAppDistanceFromCityAndBestTimeToVisitContainingIgnoreCase(
                placeName, city, distanceFromCity, bestTimeToVisit
        );

        // Convert entities to DTOs
        List<PlaceDto> placeDtos = places.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return placeDtos;
    }



    @Override
    public byte[] getPhotoById(long placeId) {
        Optional<Place> optionalPlace = placeRepository.findById(placeId);
        if (optionalPlace.isPresent()) {
            Place place = optionalPlace.get();
            return place.getPhoto();
        }
        return null; // Return null or handle photo not found scenario as needed
    }

    @Override
    public void uploadPhoto(long placeId, MultipartFile photo) throws IOException {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID"));

        // Compress the image
        byte[] compressedPhoto = compressImage(photo.getBytes());

        // Set the compressed image to the place entity
        place.setPhoto(compressedPhoto);

        placeRepository.save(place);
    }


    private byte[] compressImage(byte[] photoData) throws IOException {
        int maxSizeKB = 1024; // Set your desired maximum size in KB
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(photoData));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Compress the image to the desired size and quality
        Thumbnails.of(image)
                .size(800, 600) // You can also resize the image if needed
                .outputQuality(0.9) // Set the desired output quality (0.0 to 1.0)
                .outputFormat("jpg") // You can set the output format (e.g., "jpg", "png")
                .toOutputStream(outputStream);

        byte[] compressedPhoto = outputStream.toByteArray();
        outputStream.close();

        // Check if the compressed photo is still within the maximum size limit
        if (compressedPhoto.length > maxSizeKB * 1024) {
            throw new IOException("Compressed image size exceeds the maximum limit.");
        }

        return compressedPhoto;
    }
}
