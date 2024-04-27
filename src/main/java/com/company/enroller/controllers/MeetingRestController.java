package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting){
        if (meetingService.findById(meeting.getId()) != null){
            return new ResponseEntity("Unable to create. A meeting with Id " + meeting.getId()
                    + " already exist.", HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

//    @RequestMapping(value = "/{meetingId}/participants/{participantId}", method =RequestMethod.POST)

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", params = "title", method =  RequestMethod.PATCH)
    public ResponseEntity<?> updateMeetingTitle(@PathVariable long id, @RequestParam String title) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.updateTitle(meeting, title);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", params = "description", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateMeetingDescription(@PathVariable long id, @RequestParam String description){
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.updateDescription(meeting, description);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", params = "date", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateMeetingDate(@PathVariable long id, @RequestParam String date){
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.updateDate(meeting, date);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", params = "sortBy", method = RequestMethod.GET)
    public ResponseEntity<?> sortMeetings(@RequestParam (value = "sortBy") String sortMethod
    , @RequestParam (value = "sortOrder", defaultValue = "ASC") String sortOrder){
        Collection<Meeting> meetings = null;
        if (!sortMethod.equals("title") && !sortMethod.equals("description") && !sortMethod.equals("date")
                || !sortOrder.equals("ASC") && !sortOrder.equals("DESC")) {
            return new ResponseEntity("Wrong input parameters", HttpStatus.CONFLICT);
        } else if (sortMethod.equals("title")){
            meetings = meetingService.sortByTitle(sortOrder);
        } else if (sortMethod.equals("description")){
            meetings = meetingService.sortByDescription(sortOrder);
        } else if (sortMethod.equals("date")){
            meetings = meetingService.sortByDate(sortOrder);
        }
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "", params = "keyTitle", method = RequestMethod.GET)
    public ResponseEntity<?> findMeetingsByKeyTitle(@RequestParam("keyTitle") String key){
        Collection<Meeting> meetings = meetingService.findByKeyTitle(key);
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "", params = "keyDescription", method = RequestMethod.GET)
    public ResponseEntity<?> findMeetingsByKeyDescription(@RequestParam("keyDescription") String key){
        Collection<Meeting> meetings = meetingService.findByKeyDescription(key);
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "", params = "keyDate", method = RequestMethod.GET)
    public ResponseEntity<?> findMeetingsByKeyDate(@RequestParam("keyDate") String key){
        Collection<Meeting> meetings = meetingService.findByKeyDate(key);
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

}
