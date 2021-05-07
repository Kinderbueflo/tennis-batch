package fr.fgirard.batch.score;

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

import fr.fgirard.batch.score.bo.ScoreBO;
import fr.fgirard.batch.score.bo.ScoresErrorBO;

public class ScoreStepListener implements StepExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreStepListener.class);
	
	@Autowired
	private ScoresErrorBO scoresError;
	
	@Value("${tennis.score.error}")
	private String pathToErroFile;
	
	@Override
	public void beforeStep(final StepExecution stepExecution) {
		LOGGER.debug("Before step.");
		}

	@Override
	public ExitStatus afterStep(final StepExecution stepExecution) {
		LOGGER.debug("Ends of step");
		try (BufferedWriter buffer = new BufferedWriter(new FileWriter(this.pathToErroFile + "errorsFile.csv"))) {
			this.scoresError.getScoresError().stream().forEach(score -> {
				this.writeError(buffer, score);
			});
		} 
		catch (final IOException exception) {
			LOGGER.error("Error while creating errorsFile");
		}
		return ExitStatus.COMPLETED;
	}

	private void writeError(final BufferedWriter buffer, final ScoreBO score) {
		try {
			buffer.write(score.toString());
			buffer.newLine();
		} catch (final IOException e) {
			LOGGER.error("Error to write the line in the error file for the score : {}", score);
		}
	}
}
