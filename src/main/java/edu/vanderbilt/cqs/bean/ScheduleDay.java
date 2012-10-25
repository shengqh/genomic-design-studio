package edu.vanderbilt.cqs.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class ScheduleDay implements Serializable {
	private static final long serialVersionUID = -4930836800655207470L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "SCHEDULEDATE")
	private Date scheduleDate;

	@OneToMany(mappedBy = "day", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@OrderBy("registerTime")
	private List<ScheduleUser> users;

	@Column(name = "REGISTEREDNUMBER")
	private int registeredNumber = 0;

	@Column(name = "ATTENDEENUMBER")
	private int attendeeNumber = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public List<ScheduleUser> getUsers() {
		return users;
	}

	public void setUsers(List<ScheduleUser> users) {
		this.users = users;
	}

	public int getRegisteredNumber() {
		return registeredNumber;
	}

	public void setRegisteredNumber(int registeredNumber) {
		this.registeredNumber = registeredNumber;
	}

	public int getAttendeeNumber() {
		return attendeeNumber;
	}

	public void setAttendeeNumber(int attendeeNumber) {
		this.attendeeNumber = attendeeNumber;
	}

	public boolean getPassed() {
		Calendar toDate = Calendar.getInstance();
		Calendar schDate = Calendar.getInstance();
		schDate.setTime(this.getScheduleDate());
		return schDate.before(toDate);
	}

	public String getDate() {
		return new SimpleDateFormat("EEE, MMM d, yyyy").format(this
				.getScheduleDate());
	}
}
