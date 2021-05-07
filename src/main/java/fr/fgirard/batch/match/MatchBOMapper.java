package fr.fgirard.batch.match;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import fr.fgirard.batch.match.bo.MatchBO;

public class MatchBOMapper implements FieldSetMapper<MatchBO> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MatchBOMapper.class);
	
	@Override
	public MatchBO mapFieldSet(final FieldSet fieldSet) throws BindException {

		final MatchBO match = new MatchBO();
		try {
	    match.setDate(new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).parse(fieldSet.readString(0)));
		} catch(final ParseException exception) {
			LOGGER.error("Error parsing date : ", exception);
		}
		match.setPlayerOne(fieldSet.readString(1));
		match.setPlayerTwo(fieldSet.readString(2));
		match.setSetPlayerOne(fieldSet.readString(3));
		match.setSetPlayerTwo(fieldSet.readString(4));
		match.setOddsOne(fieldSet.readString(5));
		match.setOddsTwo(fieldSet.readString(6));
		
		return match;
	}

}
