package fr.fgirard.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.fgirard.batch.domain.Surface;

@Repository
public interface SurfaceRepository extends JpaRepository<Surface, Long> {

	public Surface findBySurface(final String surface);
}
