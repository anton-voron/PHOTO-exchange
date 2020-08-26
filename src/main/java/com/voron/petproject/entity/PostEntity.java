package com.voron.petproject.entity;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "posts")
public class PostEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity author;

	@Column(name = "description")
	private String description;

	@Column(name = "post_path")
	private String postPath;
	
	@Column(name = "file_name")
	private String fileName;

	@Column(name = "geolocation")
	private String geolocation;
	
	public PostEntity() {
	}

	public PostEntity(String description, String geolocation) {
		this.description = description;
		this.geolocation = geolocation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserEntity getAuthor() {
		return author;
	}

	public void setAuthor(UserEntity author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPostPath() {
		return postPath;
	}

	public void setPostPath(String postPath) {
		this.postPath = postPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

	
}
