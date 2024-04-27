package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

//	@RequestMapping(value = "", method = RequestMethod.GET)
//	public ResponseEntity<?> getParticipants() {
//		Collection<Participant> participants = participantService.getAll();
//		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
//	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@RequestParam (value = "sortBy", defaultValue = "") String sortMethod,
											 @RequestParam (value = "sortOrder", defaultValue = "ASC") String sortOrder,
											 @RequestParam (value = "key", defaultValue = "") String key) {
		if (!sortOrder.equals("ASC") && !sortOrder.equals("DESC")){
			return new ResponseEntity("Wrong input parameters", HttpStatus.CONFLICT);
		}

		Collection<Participant> participants = participantService.sortByLoginAndKey(sortMethod, sortOrder, key);

//		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}



	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(@RequestBody Participant participant) {
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity<String>(
					"Unable to create. A participant with login " + participant.getLogin() + " already exist.",
					HttpStatus.CONFLICT);
		}
		participantService.add(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participantService.delete(participant);
		return new ResponseEntity<Participant>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") String login, @RequestBody Participant updatedParticipant) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participant.setPassword(updatedParticipant.getPassword());
		participantService.update(participant);
		return new ResponseEntity<Participant>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<?> updateParticipantPassword(@PathVariable("id") String login, @RequestParam String oldPassword
			, @RequestParam String newPassword) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		} else if (!participant.getPassword().equals(oldPassword)){
			return new ResponseEntity("Unable to change the password. Enter correct actual password"
					+ " of participant with login " + participant.getLogin(), HttpStatus.CONFLICT);
		}
		participantService.updatePassword(participant, newPassword);
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

//	@RequestMapping(value = "", params = "sortBy", method = RequestMethod.GET)
//	public ResponseEntity<?> sortParticipants(@RequestParam ("sortBy") String sortMethod
//			, @RequestParam (value = "sortOrder", defaultValue = "ASC") String sortOrder) {
//		if (!sortMethod.equals("login") || !sortOrder.equals("ASC") && !sortOrder.equals("DESC")){
//			return new ResponseEntity("Wrong input parameters", HttpStatus.CONFLICT);
//		}
//		Collection<Participant> participants = participantService.sortByLogin(sortOrder);
//		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
//	}
//
//	@RequestMapping(value = "", params = "key", method = RequestMethod.GET)
//	public ResponseEntity<?> findParticipantsByKey(@RequestParam String key){
//		Collection<Participant> participants = participantService.findByLoginKey(key);
//		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
//	}
}
