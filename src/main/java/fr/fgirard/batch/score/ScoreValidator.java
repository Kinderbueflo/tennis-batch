package fr.fgirard.batch.score;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import fr.fgirard.batch.score.bo.ScoreBO;
import fr.fgirard.batch.score.bo.ScoresErrorBO;

public class ScoreValidator implements Validator<ScoreBO> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreValidator.class);
	
	@Autowired
	private ScoresErrorBO scoresError;
	
	@Override
	public void validate(final ScoreBO score) throws ValidationException {
		LOGGER.debug("Validator ScoreBO : {}", score);
		if (score == null 
				|| score.getComment() == null 
				|| !score.getComment().equals("Completed")) {
			
			this.scoresError.getScoresError().add(score);
			throw new ValidationException("A parameters is missing, probably due to a match cancelled.");
		}
	}

}
