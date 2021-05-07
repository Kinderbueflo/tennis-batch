package fr.fgirard.batch.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tennis_level")
public class Level implements Serializable {

	
	private static final long serialVersionUID = 8342968217426344045L;

	@Id
	@Column(name = "level_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int levelId;
	
	@Column(name = "level", nullable = false, length = 15)
	private String level;

	public Level() {
		super();
	}
	
	public Level(final String level) {
		super();
		this.level = level;
	}

	public int getLevelId() {
		return this.levelId;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevelId(final int levelId) {
		this.levelId = levelId;
	}

	public void setLevel(final String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "Level [levelId=" + this.levelId + ", level=" + this.level + "]";
	}
	
}
