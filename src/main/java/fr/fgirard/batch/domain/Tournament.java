package fr.fgirard.batch.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tennis_tournament")
public class Tournament implements Serializable {


	private static final long serialVersionUID = 2064029363686084757L;

	@Id
	@Column(name = "tournament_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tournamentId;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surface", nullable = false)
    private Surface surface;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level", nullable = false)
    private Level level;

    @Column(name = "year", nullable = false, length = 5)
    private String year;

    public Tournament() {
    	super();
    }

	public Tournament(final String name, final Surface surface, final Level level, final String year) {
		super();
		this.name = name;
		this.surface = surface;
		this.level = level;
		this.year = year;
	}

	public int getTournamentId() {
		return this.tournamentId;
	}

	public String getName() {
		return this.name;
	}

	public Surface getSurface() {
		return this.surface;
	}

	public Level getLevel() {
		return this.level;
	}

	public String getYear() {
		return this.year;
	}

	public void setTournamentId(final int tournamentId) {
		this.tournamentId = tournamentId;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setSurface(final Surface surface) {
		this.surface = surface;
	}

	public void setLevel(final Level level) {
		this.level = level;
	}

	public void setYear(final String year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "Tournament [tournamentId=" + this.tournamentId + ", name=" + this.name + ", surface=" + this.surface + ", level="
				+ this.level + ", year=" + this.year + "]";
	}
    
    
}
