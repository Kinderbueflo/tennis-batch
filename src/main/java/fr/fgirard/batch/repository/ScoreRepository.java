package fr.fgirard.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.fgirard.batch.domain.Score;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

}
