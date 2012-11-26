package edu.vanderbilt.cqs.form;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import edu.vanderbilt.cqs.RegistrationType;

public class ScheduleUserForm implements Serializable {
	private static final long serialVersionUID = -8851661085048203556L;

	private Long dayId;
	
	private String day;

	private Long id;
	
	@NotEmpty(message = "Purpose is a required field. It will not be seen except VANGARD user.")
	private String purpose = "";
	
	@NotEmpty(message = "Firstname is a required field")
	@Pattern(regexp = "[a-zA-Z ]*", message = "First name should contain a-z, A-Z and blank space only")
	private String firstname;

	@NotEmpty(message = "Lastname is a required field")
	@Pattern(regexp = "[a-zA-Z ]*", message = "Last name should contain a-z, A-Z and blank space only")
	private String lastname;
	
	@NotEmpty(message = "Department is a required field")
	@Pattern(regexp = "[a-zA-Z ]*", message = "Department should contain a-z, A-Z and blank space only")
	private String department;

	@NotEmpty(message = "Study PI is a required field")
	@Pattern(regexp = "[a-zA-Z ]*", message = "Study PI should contain a-z, A-Z and blank space only")
	private String studyPI;
	
	public String getStudyPI() {
		return studyPI;
	}

	public void setStudyPI(String studyPI) {
		this.studyPI = studyPI;
	}

	public String getDepartment() {
		return this.department;
	}
	
	

	public void setDepartment(String department) {
		this.department = department;
	}

	private RegistrationType regType;
	
	public RegistrationType getRegType() {
		return regType;
	}

	public void setRegType(RegistrationType regType) {
		this.regType = regType;
	}

	@Email
	@NotEmpty(message = "Email is a required field")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		if (this.getFirstname() != null && this.getLastname() != null) {
			return this.getFirstname() + " " + this.getLastname();
		} else if (this.getFirstname() != null) {
			return this.getFirstname();
		} else if (this.getLastname() != null) {
			return this.getLastname();
		} else {
			return "";
		}
	}

	public Long getDayId() {
		return dayId;
	}

	public void setDayId(Long dayId) {
		this.dayId = dayId;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
}
