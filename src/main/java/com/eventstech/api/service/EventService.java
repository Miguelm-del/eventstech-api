package com.eventstech.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.eventstech.api.domain.event.Event;
import com.eventstech.api.domain.event.EventRequestDTO;
import com.eventstech.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(EventRequestDTO eventData) {
        String imgUrl = null;

        if (eventData.image() != null) {
          imgUrl = this.uploadImg(eventData.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(eventData.title());
        newEvent.setDescription(eventData.description());
        newEvent.setEventUrl(eventData.eventUrl());
        newEvent.setDate(new Date(eventData.date()) );
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(eventData.remote());

        eventRepository.save(newEvent);

        return newEvent;
    }

    public Event getEventById(UUID eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }

    private String uploadImg(MultipartFile multipartFile) {
       String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

       try {
           File file = this.convertMultipartToFile(multipartFile);

           s3Client.putObject(bucketName, fileName, file);

           file.delete();
           return s3Client.getUrl(bucketName, fileName).toString();
       } catch (Exception e) {
           System.out.println("Error occured while uploading image " + fileName);

           return "";
       }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();

        return convFile;
    }
}
