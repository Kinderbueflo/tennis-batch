package fr.fgirard.batch.match.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class MatchUtils {

	
	public static final LocalDate convertDateToLocalDate(final Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
