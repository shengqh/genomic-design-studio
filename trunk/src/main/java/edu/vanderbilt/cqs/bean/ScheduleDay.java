package edu.vanderbilt.cqs.bean;

import java.io.Serializable;
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

import edu.vanderbilt.cqs.Config;
import edu.vanderbilt.cqs.Utils;

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

	public String getDate() {
		return Utils.getDate(this.getScheduleDate());
	}
	
	public boolean getPassed(){
		return Config.hasPassed(this.getScheduleDate());
	}
	
	private boolean canRegister;
	public boolean getCanRegister() {
		return this.canRegister;
	}
	
	public void setCanRegister(boolean value){
		this.canRegister = value;
	}
}
