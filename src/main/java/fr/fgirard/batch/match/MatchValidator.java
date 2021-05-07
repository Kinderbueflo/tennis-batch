package fr.fgirard.batch.match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import fr.fgirard.batch.match.bo.ListMatchErrorBO;
import fr.fgirard.batch.match.bo.MatchBO;

public class MatchValidator implements Validator<MatchBO> {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(MatchValidator.class);
	
	@Autowired
	private ListMatchErrorBO listMatchError;
	
	@Override
	public void validate(final MatchBO match) throws ValidationException {

		LOGGER.debug("Validator MatchBO : {}", match);
		if (match == null 
				|| match.getDate() == null 
				|| match.getPlayerOne() == null || match.getPlayerOne().isEmpty() 
				|| match.getPlayerTwo() == null || match.getPlayerTwo().isEmpty()
				|| match.getSetPlayerOne() == null || match.getSetPlayerOne().isEmpty()
				|| match.getSetPlayerTwo() == null || match.getSetPlayerTwo().isEmpty()
				|| match.getOddsOne() == null || match.getOddsOne().isEmpty()
				|| match.getOddsTwo() == null || match.getOddsTwo().isEmpty()) {
			
			this.listMatchError.getListMatchError().add(match);
			throw new ValidationException("A parameters is missing, probably due to a match cancelled.");
		}
	}

}
