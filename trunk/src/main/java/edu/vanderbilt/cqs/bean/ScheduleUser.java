package edu.vanderbilt.cqs.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import edu.vanderbilt.cqs.RegistrationType;

@Entity
public class ScheduleUser implements Serializable {
	private static final long serialVersionUID = 7401126221031716368L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "FIRSTNAME")
	private String firstname = "";

	@Column(name = "LASTNAME")
	private String lastname = "";

	@Column(name = "EMAIL")
	private String email = "";
	
	@Column(name = "IPADDRESS")
	private String ipaddress = "";

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	@Column(name = "DEPARTMENT")
	private String department = "";

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "REGISTERTIME")
	private Date registerTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DAY_ID")
	private ScheduleDay day;

	@Column(name = "REGISTRATIONTYPE")
	private RegistrationType regType = RegistrationType.rtOnsite;

	public RegistrationType getRegType() {
		return regType;
	}

	public void setRegType(RegistrationType regType) {
		this.regType = regType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public ScheduleDay getDay() {
		return day;
	}

	public void setDay(ScheduleDay day) {
		this.day = day;
	}
}
