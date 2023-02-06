package com.winksys.renaserv.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="credential")
public class Credential implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String nome;
	private String url;
	@Column(name="_system") private int system;
	@Column(name="_password") private String password;
	@Column(name="_user") private String user;	
	@Column(name="_key") private String key;
	@Column(name="status") private EnStatus status;
	@Column(name="email") private String email;
	@Column(name="lastcheck") private Date lastCheck; // is the last time when system check account
	@Column(name="lastsentmail") private Date lastSentmail; // is the last time when system sents notification mail about broken
	@Column(name="image") private String image;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getSystem() {
		return system;
	}

	public void setSystem(int system) {
		this.system = system;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public EnStatus getStatus() {
		return status;
	}

	public void setStatus(EnStatus status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(Date lastCheck) {
		this.lastCheck = lastCheck;
	}

	public Date getLastSentmail() {
		return lastSentmail;
	}

	public void setLastSentmail(Date lastSentmail) {
		this.lastSentmail = lastSentmail;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
