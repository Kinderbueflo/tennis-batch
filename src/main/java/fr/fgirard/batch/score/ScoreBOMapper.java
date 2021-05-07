package fr.fgirard.batch.score;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import fr.fgirard.batch.score.bo.ScoreBO;

public class ScoreBOMapper implements FieldSetMapper<ScoreBO> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreBOMapper.class);
	
	@Override
	public ScoreBO mapFieldSet(final FieldSet fieldSet) throws BindException {
		final ScoreBO score = new ScoreBO();
		score.setLocation(fieldSet.readString(0));
		try {
			score.setDate(new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(fieldSet.readString(1)));
		} catch(final ParseException exception) {
			LOGGER.error("Error parsing date : ", exception);
		}
		score.setLevel(fieldSet.readString(2));
		score.setSurface(fieldSet.readString(3));
		score.setWinner(fieldSet.readString(4));
		score.setLoser(fieldSet.readString(5));
		score.setSetWinner(fieldSet.readString(6));
		score.setSetLoser(fieldSet.readString(7));
		score.setComment(fieldSet.readString(8));
		score.setOddsWinner(fieldSet.readString(9));
		score.setOddsLoser(fieldSet.readString(10));
		return score;
	}

}
