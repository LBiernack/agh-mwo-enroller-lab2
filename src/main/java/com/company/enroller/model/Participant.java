package com.company.enroller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "participant")
public class Participant {

	@Id
	private String login;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column
	private String password;

//	@JsonIgnore
//	@ManyToMany(mappedBy = "participants", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
//
//	Set<Meeting> meetings = new HashSet<>();

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public Collection<Meeting> getMeetings() {
//		return meetings;
//	}
//
//	public void addMeeting(Meeting meeting) {
//		this.meetings.add(meeting);
//	}
//
//	public void removeMeeting(Meeting meeting) {
//		this.meetings.remove(meeting);
//	}
}
