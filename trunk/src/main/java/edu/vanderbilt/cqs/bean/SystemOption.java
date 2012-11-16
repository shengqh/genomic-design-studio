package edu.vanderbilt.cqs.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYSTEMOPTION")
public class SystemOption implements Serializable {
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOptionKey() {
		return optionKey;
	}

	public void setOptionKey(String optionKey) {
		this.optionKey = optionKey;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	private static final long serialVersionUID = 7626047846824302126L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;

	@Column(unique=true, name="OPTION_KEY")
	private String optionKey = "";

	@Column(name="OPTION_VALUE")
	private String optionValue = "";
}
