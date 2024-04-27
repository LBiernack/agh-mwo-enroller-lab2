package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

//	Session session;
//
//	public MeetingService() {
//		session = DatabaseConnector.getInstance().getSession();
//	}

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
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

	public void updateTitle(Meeting meeting, String title){
		meeting.setTitle(title);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void updateDescription(Meeting meeting, String description){
		meeting.setDescription(description);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void updateDate(Meeting meeting, String date){
		meeting.setDate(date);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public Collection<Meeting> sortByTitle(String sortOrder) {
		String hql = "FROM Meeting m ORDER BY m.title " + sortOrder;
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Collection<Meeting> sortByDescription(String sortOrder) {
		String hql = "FROM Meeting m ORDER BY m.description " + sortOrder;
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Collection<Meeting> sortByDate(String sortOrder) {
		String hql = "FROM Meeting m ORDER BY m.date " + sortOrder;
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Collection<Meeting> findByKeyTitle(String key) {
		String hql = "SELECT m FROM Meeting m WHERE m.title like '%" + key + "%'";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Collection<Meeting> findByKeyDescription(String key) {
		String hql = "SELECT m FROM Meeting m WHERE m.description like '%" + key + "%'";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Collection<Meeting> findByKeyDate(String key) {
		String hql = "SELECT m FROM Meeting m WHERE m.date like '%" + key + "%'";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

}
