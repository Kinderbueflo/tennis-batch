package fr.fgirard.batch.match;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import fr.fgirard.batch.domain.Player;
import fr.fgirard.batch.domain.Score;
import fr.fgirard.batch.domain.Tournament;
import fr.fgirard.batch.match.bo.ListTournamentsBO;
import fr.fgirard.batch.match.bo.MatchBO;
import fr.fgirard.batch.match.utils.MatchUtils;
import fr.fgirard.batch.repository.PlayerRepository;
import fr.fgirard.batch.repository.ScoreRepository;

public class MatchWriter implements ItemWriter<MatchBO>, InitializingBean {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(MatchWriter.class);

	private static final String PLAYER_ONE = "playerOne";

	private static final String PLAYER_TWO = "playerTwo";
	
	private static final String WINNER = "winner";

	private String filename;
	
	@Value(value = "${tennis.match.sexe}")
	private String sexe;

	@Autowired
	private ListTournamentsBO listTournaments;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private ScoreRepository scoreRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.listTournaments, "The tournament list is required");
	}
	
	@BeforeStep
    public void retrieveFilename(final StepExecution stepExecution) throws MalformedURLException {
		final String file = (String) stepExecution.getExecutionContext().get("fileName");
//		Resource url = new UrlResource(file); url.getFilename();
		final String [] fileSplit = file.split("/");
		this.filename = fileSplit[fileSplit.length - 1];
    }
		
	@Override
	public void write(final List<? extends MatchBO> matchBOs) throws Exception {

		LOGGER.debug("Enter in the writer");
		final String tournamentName = this.filename.split("_")[0].replaceAll("-", " ");
		final String tournamentYear = this.filename.split("_")[1].substring(0, 4);
		final Tournament tournament = this.retrieveTournament(tournamentName, tournamentYear);
		LOGGER.debug("The tournament recovered : {}", tournament);
		
		try {
			matchBOs.stream().forEach(matchBO -> {
				LOGGER.debug("Insert the BO values : {}", matchBO);
				final Map<String, Player> players = this.insertPlayers(matchBO);
				this.insertScores(matchBO, players, tournament);
			});
		} catch (final Exception exception) {
			LOGGER.error("Error during score save with the exception : ", exception);
			throw new Exception("Error during score save for the file : " + this.filename);
		}

		LOGGER.debug("Ends of the writer");
	}
	
	private Tournament retrieveTournament(final String tournamentName, final String tournamentYear) throws Exception {
		final List<Tournament> tournaments = this.listTournaments.getListTournaments().stream()
				.filter(tournament -> 
						tournamentName.equalsIgnoreCase(tournament.getName())
						&& tournamentYear.equalsIgnoreCase(tournament.getYear()))
				.collect(Collectors.toList());
		
		if (tournaments == null || tournaments.size() != 1) {
			throw new Exception("We need to retrieve only one tournament for the filename : " + this.filename);
		}
		return tournaments.get(0);
	}

	private Map<String, Player> insertPlayers(final MatchBO matchBO) {
		final String playerOneName = matchBO.getPlayerOne();
		final String playerTwoName = matchBO.getPlayerTwo();
		final Player playerOne = this.insertPlayer(playerOneName);
		final Player playerTwo = this.insertPlayer(playerTwoName);
		final Map<String, Player> players = new HashMap<>();
		players.put(PLAYER_ONE, playerOne);
		players.put(PLAYER_TWO, playerTwo);
		return players;
	}

	private Player insertPlayer(final String name) {
		Player player = this.playerRepository.findByName(name);
		if (player == null) {
			player = this.playerRepository.save(new Player(name, this.sexe));
		}
		return player;
	}
	
	private void insertScores(final MatchBO matchBO, final Map<String, Player> players, final Tournament tournament) {
		final Integer setPlayerOne = Integer.valueOf(matchBO.getSetPlayerOne());
		final Integer setPlayerTwo = Integer.valueOf(matchBO.getSetPlayerTwo());
		if (setPlayerOne > setPlayerTwo) {
			players.put(WINNER, players.get(PLAYER_ONE));
		} else {
			players.put(WINNER, players.get(PLAYER_TWO));
		}
		final Score scoreOne = this.createScore(matchBO, players, true, tournament);
		final Score scoreTwo = this.createScore(matchBO, players, false, tournament);
		final List<Score> scores = new ArrayList<>();
		scores.add(scoreOne);
		scores.add(scoreTwo);
		this.scoreRepository.saveAll(scores);
	}
	
	private Score createScore(final MatchBO matchBO, final Map<String, Player> players, final boolean playerOne,
			final Tournament tournament) {
		final Score score = new Score();
		if (playerOne) {
			score.setPlayer(players.get(PLAYER_ONE));
			score.setOpponent(players.get(PLAYER_TWO));
			score.setOdds(new BigDecimal(matchBO.getOddsOne()));
			this.isVictory(players.get(PLAYER_ONE), players.get(WINNER), score);
			score.setSetWon(Integer.valueOf(matchBO.getSetPlayerOne()));
		} else {
			score.setPlayer(players.get(PLAYER_TWO));
			score.setOpponent(players.get(PLAYER_ONE));
			score.setOdds(new BigDecimal(matchBO.getOddsTwo()));
			this.isVictory(players.get(PLAYER_TWO), players.get(WINNER), score);
			score.setSetWon(Integer.valueOf(matchBO.getSetPlayerTwo()));
		}
		score.setTournament(tournament);
		score.setMatchDate(MatchUtils.convertDateToLocalDate(matchBO.getDate()));
		return score;
	}
	
	private void isVictory(final Player player, final Player winner, final Score score) {
		if (player.equals(winner)) {
			score.setVictory(true);
		} else {
			score.setVictory(false);
		}
	}

}
