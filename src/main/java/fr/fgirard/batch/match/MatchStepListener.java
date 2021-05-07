package fr.fgirard.batch.match;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import fr.fgirard.batch.match.bo.ListMatchErrorBO;
import fr.fgirard.batch.match.bo.ListTournamentsBO;
import fr.fgirard.batch.match.bo.MatchBO;
import fr.fgirard.batch.repository.TournamentRepository;

public class MatchStepListener implements StepExecutionListener {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(MatchStepListener.class);
	
	@Autowired
	private TournamentRepository tournamentRepository;
	
	@Autowired
	private ListTournamentsBO listTournaments;
	
	@Autowired
	private ListMatchErrorBO listMatchError;
	
	@Value("${tennis.match.error}")
	private String pathToErroFile;
	
	@Override
	public void beforeStep(final StepExecution stepExecution) {
		LOGGER.debug("Gets the tournaments list.");
		this.listTournaments.setListTournaments(this.tournamentRepository.findAll());
	}

	@Override
	public ExitStatus afterStep(final StepExecution stepExecution) {
		
		LOGGER.debug("Ends of partitionerStep");
		
		try (BufferedWriter buffer = new BufferedWriter(new FileWriter(this.pathToErroFile + "errorsFile.csv"))) {
			this.listMatchError.getListMatchError().stream().forEach(match -> {
				this.writeError(buffer, match);
			});
		} 
		catch (final IOException exception) {
			LOGGER.error("Error while creating errorsFile");
		}
		
		return ExitStatus.COMPLETED;
	}
	
	private void writeError(final BufferedWriter buffer, final MatchBO match) {
		try {
			buffer.write(match.toString());
			buffer.newLine();
		} catch (final IOException e) {
			LOGGER.error("Error to write the line in the error file for the match : {}", match);
		}
	}

}
