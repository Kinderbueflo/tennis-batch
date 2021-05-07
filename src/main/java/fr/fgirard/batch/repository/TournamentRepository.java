package fr.fgirard.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.fgirard.batch.domain.Level;
import fr.fgirard.batch.domain.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

	Tournament findByNameAndYearAndLevel(String name, String year, Level level);
}
