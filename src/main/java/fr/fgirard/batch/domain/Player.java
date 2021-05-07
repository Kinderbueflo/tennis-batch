package fr.fgirard.batch.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tennis_player")
public class Player implements Serializable {


	private static final long serialVersionUID = 234768889083675016L;

	@Id
	@Column(name = "player_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int playerId;
	
	@Column(name = "last_name", nullable = true, length = 250)
	private String lastName;

	@Column(name = "first_name", nullable = true, length = 250)
	private String firstName;

	@Column(name = "name", nullable = false, length = 250)
	private String name;

	@Column(name = "sexe", nullable = false, length = 10)
	private String sexe;

	public Player() {
		super();
	}

	public Player(final String name, final String sexe) {
		super();
		this.name = name;
		this.sexe = sexe;
	}

	public int getPlayerId() {
		return this.playerId;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getName() {
		return this.name;
	}

	public String getSexe() {
		return this.sexe;
	}

	public void setPlayerId(final int playerId) {
		this.playerId = playerId;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setSexe(final String sexe) {
		this.sexe = sexe;
	}

	@Override
	public String toString() {
		return "Player [playerId=" + this.playerId 
				+ ", lastName=" + this.lastName 
				+ ", firstName=" + this.firstName 
				+ ", name=" + this.name
				+ ", sexe=" + this.sexe + "]";
	}

	
}
