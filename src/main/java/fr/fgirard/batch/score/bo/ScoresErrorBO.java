package fr.fgirard.batch.score.bo;

import java.util.List;

public class ScoresErrorBO {

	private List<ScoreBO> scoresError;

	public ScoresErrorBO() {
		super();
	}

	public List<ScoreBO> getScoresError() {
		return this.scoresError;
	}

	public void setScoresError(final List<ScoreBO> scoresError) {
		this.scoresError = scoresError;
	}

	@Override
	public String toString() {
		return "ScoresErrorBO [scoresError=" + this.scoresError + "]";
	}
	
}
