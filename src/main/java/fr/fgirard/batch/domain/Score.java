package fr.fgirard.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tennis_score")
public class Score implements Serializable {


	private static final long serialVersionUID = 4931999926220238189L;

	@Id
	@Column(name = "score_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int scoreId;

	@ManyToOne
	@JoinColumn(name = "player", nullable = false)
	private Player player;
	
	@ManyToOne
	@JoinColumn(name = "opponent", nullable = false)
	private Player opponent;
	
	@Column(name = "odds", nullable = false)
	private BigDecimal odds;
	
	@Column(name = "victory", nullable = false)
	private boolean victory;
	
	@Column(name = "set_won", nullable = false)
    private Integer setWon;
	
	@ManyToOne
	@JoinColumn(name = "tournament", nullable = false)
	private Tournament tournament;
	
	@Column(name = "match_date", nullable = false)
	private LocalDate matchDate;

	public Score() {
		super();
	}

	public int getScoreId() {
		return this.scoreId;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Player getOpponent() {
		return this.opponent;
	}

	public BigDecimal getOdds() {
		return this.odds;
	}

	public boolean isVictory() {
		return this.victory;
	}

	public Integer getSetWon() {
		return this.setWon;
	}

	public Tournament getTournament() {
		return this.tournament;
	}

	public LocalDate getMatchDate() {
		return this.matchDate;
	}

	public void setScoreId(final int scoreId) {
		this.scoreId = scoreId;
	}

	public void setPlayer(final Player player) {
		this.player = player;
	}

	public void setOpponent(final Player opponent) {
		this.opponent = opponent;
	}

	public void setOdds(final BigDecimal odds) {
		this.odds = odds;
	}

	public void setVictory(final boolean victory) {
		this.victory = victory;
	}

	public void setSetWon(final Integer setWon) {
		this.setWon = setWon;
	}

	public void setTournament(final Tournament tournament) {
		this.tournament = tournament;
	}

	public void setMatchDate(final LocalDate matchDate) {
		this.matchDate = matchDate;
	}

	@Override
	public String toString() {
		return "Score ["
				+ "scoreId=" + this.scoreId 
				+ ", player=" + this.player 
				+ ", opponent=" + this.opponent 
				+ ", odds=" + this.odds
				+ ", victory=" + this.victory 
				+ ", setWon=" + this.setWon 
				+ ", tournament=" + this.tournament 
				+ ", matchDate=" + this.matchDate + "]";
	}

}
