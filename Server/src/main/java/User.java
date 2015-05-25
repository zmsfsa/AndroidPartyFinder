package main.java;

import javax.persistence.*;

@Entity
@Table(name = "partyfinderdb.users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idUser")
	private int idUser;
	
	@Column(name = "login",unique = true)
	private String login;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "fName")
	private String fName;
	
	@Column(name = "lName")
	private String lName;

	public User() {

	}
	
	public User(String login, String password, String fName, String lName) {
		this.login = login;
		this.password = password;
		this.fName = fName;
		this.lName = lName;
	}

	
	public int getIdUser() {
		return idUser;
	}

	public String getLogin() {
		return this.login;
	}

	public String getPassword() {
		return this.password;
	}

	public String getFName() {
		return this.fName;
	}

	public String getLName() {
		return this.lName;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public void setFName(String fname) {
		this.fName = fname;
	}

	public void setLName(String lname) {
		this.lName = lname;
	}

	public void setIdUser(int id) {
		this.idUser = id;
	}
}