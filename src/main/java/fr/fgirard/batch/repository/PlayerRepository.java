package fr.fgirard.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.fgirard.batch.domain.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

	
	Player findByName(String name);
}
