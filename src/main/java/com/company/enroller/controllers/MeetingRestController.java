package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;
    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings(@RequestParam (value = "sortBy", defaultValue = "") String sortMethod,
                                         @RequestParam (value = "sortOrder", defaultValue = "ASC") String sortOrder,
                                         @RequestParam (value = "value", defaultValue = "") String key) {
        Collection<Meeting> meetings;
        if (sortMethod.isEmpty()) {
            meetings = meetingService.getAll();
        } else {
            if (!sortMethod.equals("title") && !sortMethod.equals("description") && !sortMethod.equals("date")
                    || !sortOrder.equals("ASC") && !sortOrder.equals("DESC")) {
                return new ResponseEntity("Wrong input parameters", HttpStatus.CONFLICT);
            }
            meetings = meetingService.get(sortMethod, sortOrder, key);
        }

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

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);
        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method =  RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Meeting updatedMeeting) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.update(meeting, updatedMeeting);
        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Collection<Participant> meetingParticipants = meetingService.getParticipants(meeting);
        return new ResponseEntity<Collection<Participant>>(meetingParticipants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addMeetingParticipants(@PathVariable long id, @RequestParam List<String> login) {
        Meeting meeting = meetingService.findById(id);
        Collection<Participant> newMeetingParticipants = new HashSet<>();
        Collection<Participant> participantsAlreadyExists = meetingService.getParticipants(meeting);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        for (String participantLogin : login) {
            Participant participant = participantService.findByLogin(participantLogin);
            if (participant == null) {
                return new ResponseEntity<String>(
                        "A participant with login " + participantLogin + " doesn't exist.",
                        HttpStatus.NOT_FOUND);
            } else if (participantsAlreadyExists.contains(participant)) {
                return new ResponseEntity<String>(
                        "Unable to add. A participant with login " + participant.getLogin() + " is already on the meeting.",
                        HttpStatus.CONFLICT);
            }
            newMeetingParticipants.add(participant);
        }
        Collection<Participant> meetingParticipants = meetingService.addParticipants(meeting, newMeetingParticipants);
        return new ResponseEntity<Collection<Participant>>(meetingParticipants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeetingParticipants(@PathVariable long id, @PathVariable String login) {
        Meeting meeting = meetingService.findById(id);
        Participant participant = participantService.findByLogin(login);
        Collection<Participant> participantsAlreadyExists = meetingService.getParticipants(meeting);
        if (meeting == null || participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else if (!participantsAlreadyExists.contains(participant)) {
            return new ResponseEntity<String>(
                    "Unable to remove. There is no participant with login " + participant.getLogin() + " on the meeting.",
                    HttpStatus.CONFLICT);
        }
        meetingService.deleteParticipant(meeting, participant);
        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

}
