package fr.fgirard.batch.score.bo;

import java.util.Date;

public class ScoreBO {

	private Date date;
	private String location;
	private String level;
	private String surface;
	private String winner;
	private String loser;
	private String setWinner;
	private String setLoser;
	private String oddsWinner;
	private String oddsLoser;
	private String comment;
	
	public ScoreBO() {
		super();
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(final String level) {
		this.level = level;
	}

	public String getSurface() {
		return this.surface;
	}

	public void setSurface(final String surface) {
		this.surface = surface;
	}

	public String getWinner() {
		return this.winner;
	}

	public void setWinner(final String winner) {
		this.winner = winner;
	}

	public String getLoser() {
		return this.loser;
	}

	public void setLoser(final String loser) {
		this.loser = loser;
	}

	public String getSetWinner() {
		return this.setWinner;
	}

	public void setSetWinner(final String setWinner) {
		this.setWinner = setWinner;
	}

	public String getSetLoser() {
		return this.setLoser;
	}

	public void setSetLoser(final String setLoser) {
		this.setLoser = setLoser;
	}

	public String getOddsWinner() {
		return this.oddsWinner;
	}

	public void setOddsWinner(final String oddsWinner) {
		this.oddsWinner = oddsWinner;
	}

	public String getOddsLoser() {
		return this.oddsLoser;
	}

	public void setOddsLoser(final String oddsLoser) {
		this.oddsLoser = oddsLoser;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "ScoreBO [date=" + this.date + ", location=" + this.location + ", level=" + this.level + ", surface=" + this.surface
				+ ", winner=" + this.winner + ", loser=" + this.loser + ", setWinner=" + this.setWinner + ", setLoser=" + this.setLoser
				+ ", oddsWinner=" + this.oddsWinner + ", oddsLoser=" + this.oddsLoser + ", comment=" + this.comment + "]";
	}
	
	
}
