package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

		public Collection<Meeting> get(String sortBy, String sortOrder, String key) {
		String hql = "FROM Meeting WHERE " + sortBy + " LIKE :key ORDER by " + sortBy + " " + sortOrder;
		Query query = connector.getSession().createQuery(hql);
		query.setParameter("key", "%" + key + "%");
		return query.list();
	}

	public Meeting findById(long id) {
		return connector.getSession().get(Meeting.class, id);
	}

	public void add(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void delete(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
	}

	public void update(Meeting meeting, Meeting updatedMeeting){
		meeting.setTitle(updatedMeeting.getTitle());
		meeting.setDescription(updatedMeeting.getDescription());
		meeting.setDate(updatedMeeting.getDate());
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public Collection<Participant> getParticipants(Meeting meeting){
		return meeting.getParticipants();
	}

	public Collection<Participant> addParticipants(Meeting meeting, Collection<Participant> participants) {
		for (Participant participant : participants) {
			meeting.addParticipant(participant);
		}
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
		return meeting.getParticipants();
	}

	public void deleteParticipant(Meeting meeting, Participant participant) {
		meeting.removeParticipant(participant);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}
}
