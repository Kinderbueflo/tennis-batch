package fr.fgirard.batch.tournaments;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;

import fr.fgirard.batch.domain.Level;
import fr.fgirard.batch.domain.Surface;
import fr.fgirard.batch.domain.Tournament;
import fr.fgirard.batch.repository.LevelRepository;
import fr.fgirard.batch.repository.SurfaceRepository;

public class TournamentMapper implements FieldSetMapper<Tournament> {

	
	@Autowired
	public SurfaceRepository surfaceRepository;
	
	@Autowired
	public LevelRepository levelRepository;
		
	@Override
	public Tournament mapFieldSet(final FieldSet fieldSet) throws BindException {
		
		final String name = fieldSet.readString(0);
		final String codeLevel = fieldSet.readString(1);
		final String codeSurface = fieldSet.readString(2);
		final String year = fieldSet.readString(3);
		
		final Level level = this.levelRepository.findByLevel(codeLevel);
		final Surface surface = this.surfaceRepository.findBySurface(codeSurface);
		
		return new Tournament(name, surface, level,	year);
	}

}
