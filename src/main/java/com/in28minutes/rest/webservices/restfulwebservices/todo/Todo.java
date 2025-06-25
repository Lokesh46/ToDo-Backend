package com.in28minutes.rest.webservices.restfulwebservices.todo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Todo {

	public Todo() {
		
	}
	
	

	public Todo(Integer id, String description, LocalDate targetDate, boolean done, LocalDate completeDate, User user) {
		super();
		this.id = id;
		this.description = description;
		this.targetDate = targetDate;
		this.done = done;
		this.completeDate = completeDate;
		this.user = user;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	
	private String description;
	private LocalDate targetDate;
	private boolean done;
	private LocalDate completeDate;
	
    public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	@JsonBackReference
    private User user;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(LocalDate targetDate) {
		this.targetDate = targetDate;
	}

	public boolean getDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	

	public LocalDate getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(LocalDate completeDate) {
		this.completeDate = completeDate;
	}

}