package fr.fgirard.batch.score;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import fr.fgirard.batch.domain.Level;
import fr.fgirard.batch.domain.Player;
import fr.fgirard.batch.domain.Score;
import fr.fgirard.batch.domain.Surface;
import fr.fgirard.batch.domain.Tournament;
import fr.fgirard.batch.match.utils.MatchUtils;
import fr.fgirard.batch.repository.LevelRepository;
import fr.fgirard.batch.repository.PlayerRepository;
import fr.fgirard.batch.repository.ScoreRepository;
import fr.fgirard.batch.repository.SurfaceRepository;
import fr.fgirard.batch.repository.TournamentRepository;
import fr.fgirard.batch.score.bo.ScoreBO;
import fr.fgirard.batch.score.constants.ConstantsLevel;

public class ScoreWriter implements ItemWriter<ScoreBO> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreWriter.class);
	private static final String PLAYER_ONE = "playerOne";
	private static final String PLAYER_TWO = "playerTwo";
	private static final String WINNER = "winner";
	
	@Autowired
	private SurfaceRepository surfaceRepository;
	@Autowired
	private LevelRepository levelRepository;
	@Autowired
	private TournamentRepository tournamentRepository;
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private ScoreRepository scoreRepository;
	
	@Value(value = "${tennis.score.sexe}")
	private String sexe;
	
	private String filename;
	
	
	@BeforeStep
    public void retrieveFilename(final StepExecution stepExecution) throws MalformedURLException {
		final String file = (String) stepExecution.getExecutionContext().get("fileName");
		final String [] fileSplit = file.split("/");
		this.filename = fileSplit[fileSplit.length - 1];
    }
	
	@Override
	public void write(final List<? extends ScoreBO> scores) throws Exception {
		LOGGER.debug("Enter in the writer");
		final String year = this.filename.split("_")[1].substring(0, 4);
		try {
			scores.stream().forEach(score -> {
				final Tournament tournament = this.insertTournament(score, year);
				final Map<String, Player> players = this.insertPlayers(score);
				this.insertScores(score, players, tournament);
			});
		} catch (final Exception exception) {
			LOGGER.error("Error during score save with the exception : ", exception);
			throw new Exception("Error during score save for the file : " + this.filename);
		}
		LOGGER.debug("Ends of the writer");
	}
	
	private Tournament insertTournament(final ScoreBO score, final String year) {
		final Level level = this.levelRepository.findByLevel(this.selectLevel(score.getLevel()));
		final Surface surface = this.surfaceRepository.findBySurface(score.getSurface());
		Tournament tournament = this.tournamentRepository.findByNameAndYearAndLevel(score.getLocation(), year, level);
		if (tournament == null) {
			tournament = new Tournament();
			tournament.setName(score.getLocation());
			tournament.setYear(year);
			tournament.setLevel(level);
			tournament.setSurface(surface);
			this.tournamentRepository.save(tournament);
		}
		return tournament;
	}
	
	private String selectLevel(final String level) {
		if (level.equals(ConstantsLevel.ATP250)) {
			return ConstantsLevel.ATP_250;
		}
		if (level.equals(ConstantsLevel.ATP500)) {
			return ConstantsLevel.ATP_500;
		}
		if (level.equals(ConstantsLevel.MASTERS_CUP)) {
			return ConstantsLevel.TOUR_FINALS;
		}
		if (level.equals(ConstantsLevel.MASTERS1000)) {
			return ConstantsLevel.MASTERS;
		}
		return level;
	}

	private Map<String, Player> insertPlayers(final ScoreBO score) {
		final String playerOneName = score.getWinner();
		final String playerTwoName = score.getLoser();
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
	
	private void insertScores(final ScoreBO score, final Map<String, Player> players, final Tournament tournament) {
		players.put(WINNER, players.get(PLAYER_ONE));
		final Score scoreOne = this.createScore(score, players, true, tournament);
		final Score scoreTwo = this.createScore(score, players, false, tournament);
		final List<Score> scores = new ArrayList<>();
		scores.add(scoreOne);
		scores.add(scoreTwo);
		this.scoreRepository.saveAll(scores);
	}
	
	private Score createScore(final ScoreBO scoreBO, final Map<String, Player> players, final boolean playerOne,
			final Tournament tournament) {
		final Score score = new Score();
		if (playerOne) {
			score.setPlayer(players.get(PLAYER_ONE));
			score.setOpponent(players.get(PLAYER_TWO));
			score.setOdds(new BigDecimal(scoreBO.getOddsWinner().replace(",", ".")));
			this.isVictory(players.get(PLAYER_ONE), players.get(WINNER), score);
			score.setSetWon(Integer.valueOf(scoreBO.getSetWinner()));
		} else {
			score.setPlayer(players.get(PLAYER_TWO));
			score.setOpponent(players.get(PLAYER_ONE));
			score.setOdds(new BigDecimal(scoreBO.getOddsLoser().replace(",", ".")));
			this.isVictory(players.get(PLAYER_TWO), players.get(WINNER), score);
			score.setSetWon(Integer.valueOf(scoreBO.getSetLoser()));
		}
		score.setTournament(tournament);
		score.setMatchDate(MatchUtils.convertDateToLocalDate(scoreBO.getDate()));
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
