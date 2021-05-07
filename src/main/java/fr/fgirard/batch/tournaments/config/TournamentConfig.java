package fr.fgirard.batch.tournaments.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import fr.fgirard.batch.domain.Tournament;
import fr.fgirard.batch.tournaments.TournamentMapper;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:application.properties")
public class TournamentConfig {

	@Value("file:${tennis.tournaments.resources}")
	private Resource[] resources;
	
	@Autowired
    @Qualifier(value="entityManagerFactory")
    private EntityManagerFactory entityManagerFactory;
		
	@Bean
	public Job tournaments(final JobBuilderFactory job, final Step tournamentStep) {
		
		return job.get("tournaments")
				.incrementer(new RunIdIncrementer())
				.flow(tournamentStep)
				.end()
				.build();
	}
	
	@Bean
	public Step tournamentStep(
			final StepBuilderFactory step,
			final ItemReader<Tournament> tournamentReader,
			final ItemWriter<Tournament> tournementWriter) {
		
		return step.get("tournamentStep")
				.<Tournament, Tournament>chunk(50)
				.reader(tournamentReader)
				.writer(tournementWriter)
				.build();
	}
	
	@Bean
	public ItemReader<Tournament> tournamentReader(final TournamentMapper tournamentMapper) {
		
		final DefaultLineMapper<Tournament> lineMapper = new DefaultLineMapper<>();
		lineMapper.setFieldSetMapper(tournamentMapper);
		lineMapper.setLineTokenizer(new DelimitedLineTokenizer(";"));
		
		final FlatFileItemReader<Tournament> delegate = new FlatFileItemReader<>();
		delegate.setLineMapper(lineMapper);
		
		final MultiResourceItemReader<Tournament> reader = new MultiResourceItemReader<>();
		reader.setResources(this.resources);
		reader.setDelegate(delegate);
		reader.setStrict(true);
		
		return reader;
	}
	
	@Bean
	public TournamentMapper tournamentMapper() {
		return new TournamentMapper();
	}
	
	@Bean
	public ItemWriter<Tournament> tournementWriter() {
		
		final JpaItemWriter<Tournament> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(this.entityManagerFactory);

		return writer;
	}
	
}
