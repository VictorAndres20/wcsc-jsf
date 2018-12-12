package com.unbosque.wcsc.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the missingsheets database table.
 * 
 */
@Entity
@Table(name="missingsheets")
public class Missingsheet implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	private int countSheets;

	private int numberSheets;

	private int userId;

	public Missingsheet() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "countSheets",length=2)
	public int getCountSheets() {
		return this.countSheets;
	}

	public void setCountSheets(int countSheets) {
		this.countSheets = countSheets;
	}

	@Column(name = "numberSheets",length=3)
	public int getNumberSheets() {
		return this.numberSheets;
	}

	public void setNumberSheets(int numberSheets) {
		this.numberSheets = numberSheets;
	}

	@Column(name = "userId")
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}