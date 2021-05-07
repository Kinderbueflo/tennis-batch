package fr.fgirard.batch.match.bo;

import java.util.Date;

public class MatchBO {

	
	private Date date;
	
	private String playerOne;
	
	private String playerTwo;
	
	private String setPlayerOne;
	
	private String setPlayerTwo;
	
	private String oddsOne;
	
	private String oddsTwo;

	public Date getDate() {
		return this.date;
	}

	public String getPlayerOne() {
		return this.playerOne;
	}

	public String getPlayerTwo() {
		return this.playerTwo;
	}

	public String getSetPlayerOne() {
		return this.setPlayerOne;
	}

	public String getSetPlayerTwo() {
		return this.setPlayerTwo;
	}

	public String getOddsOne() {
		return this.oddsOne;
	}

	public String getOddsTwo() {
		return this.oddsTwo;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public void setPlayerOne(final String playerOne) {
		this.playerOne = playerOne;
	}

	public void setPlayerTwo(final String playerTwo) {
		this.playerTwo = playerTwo;
	}

	public void setSetPlayerOne(final String setPlayerOne) {
		this.setPlayerOne = setPlayerOne;
	}

	public void setSetPlayerTwo(final String setPlayerTwo) {
		this.setPlayerTwo = setPlayerTwo;
	}

	public void setOddsOne(final String oddsOne) {
		this.oddsOne = oddsOne;
	}

	public void setOddsTwo(final String oddsTwo) {
		this.oddsTwo = oddsTwo;
	}

	@Override
	public String toString() {
		return "MatchBO [date=" + this.date 
				+ ", playerOne=" + this.playerOne 
				+ ", playerTwo=" + this.playerTwo 
				+ ", setPlayerOne="	+ this.setPlayerOne 
				+ ", setPlayerTwo=" + this.setPlayerTwo 
				+ ", oddsOne=" + this.oddsOne 
				+ ", oddsTwo=" + this.oddsTwo
				+ "]";
	}
	
	
}
