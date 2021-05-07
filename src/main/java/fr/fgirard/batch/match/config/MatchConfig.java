package fr.fgirard.batch.match.config;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import fr.fgirard.batch.match.MatchBOMapper;
import fr.fgirard.batch.match.MatchStepListener;
import fr.fgirard.batch.match.MatchValidator;
import fr.fgirard.batch.match.MatchWriter;
import fr.fgirard.batch.match.bo.ListMatchErrorBO;
import fr.fgirard.batch.match.bo.ListTournamentsBO;
import fr.fgirard.batch.match.bo.MatchBO;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:application.properties")
public class MatchConfig {

	
	@Value("file:${tennis.match.resources}")
	private Resource[] resources;
		
	@Bean
	public Job match (final JobBuilderFactory job, final Step partitionerStep) {
		
		return job.get("match")
				.incrementer(new RunIdIncrementer())
				.flow(partitionerStep)
				.end()
				.build();
	}
	
	@Bean
	public Step partitionerStep(
			final StepBuilderFactory step,
			final Step matchStep,
			final Partitioner partitioner,
			final TaskExecutor taskExecutor,
			final StepExecutionListener listener) {
		
		return step.get("partitionerStep")
				.listener(listener)
				.partitioner("matchStep", partitioner)
				.taskExecutor(taskExecutor)
				.step(matchStep)
				.build();
	}
	
	@Bean
	public Partitioner partitioner() {
		final MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		partitioner.setResources(this.resources);
		return partitioner;
	}
	
	@Bean 
	public TaskExecutor taskExecutor() {
		final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setCorePoolSize(1);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
	}
	
	@Bean
	public StepExecutionListener listener() {
		return new MatchStepListener();
	}
	
	@Bean
	public Step matchStep(
			final StepBuilderFactory step,
			final FlatFileItemReader<MatchBO> matchReader,
			final ItemProcessor<MatchBO, MatchBO> matchProcessor,
			final ItemWriter<MatchBO> matchWriter) {
		
		return step.get("matchStep")
				.<MatchBO, MatchBO>chunk(50)
				.reader(matchReader)
				.processor(matchProcessor)
				.writer(matchWriter)
				.build();
	}
	
	@Bean
	@StepScope
	public FlatFileItemReader<MatchBO> matchReader(@Value("#{stepExecutionContext['fileName']}") final String filename) throws MalformedURLException {
		
		final DefaultLineMapper<MatchBO> lineMapper = new DefaultLineMapper<>();
		lineMapper.setFieldSetMapper(new MatchBOMapper());
		lineMapper.setLineTokenizer(new DelimitedLineTokenizer(";"));
		
		final FlatFileItemReader<MatchBO> reader = new FlatFileItemReader<>();
		reader.setLineMapper(lineMapper);
		reader.setResource(new UrlResource(filename));
		
//		final MultiResourceItemReader<MatchBO> reader = new MultiResourceItemReader<>();
//		reader.setResources(this.resources);
//		reader.setDelegate(delegate);
//		reader.setStrict(true);
		
		return reader;
	}
	
	@Bean
	public ItemProcessor<MatchBO, MatchBO> matchProcessor(final Validator<MatchBO> validator) {
		final ValidatingItemProcessor<MatchBO> processor = new ValidatingItemProcessor<>();
		processor.setValidator(validator);
		processor.setFilter(true);
		return processor;
	}
	
	@Bean
	public Validator<MatchBO> validator() {
		return new MatchValidator();
	}
	
	@Bean
	public ItemWriter<MatchBO> matchWriter() {
		return new MatchWriter();
	}
		
	@Bean
	public ListTournamentsBO listTournaments() {
		return new ListTournamentsBO();
	}
	
	@Bean
	public ListMatchErrorBO listMatchError() {
		final ListMatchErrorBO listMatchError =  new ListMatchErrorBO();
		listMatchError.setListMatchError(Collections.synchronizedList(new ArrayList<>()));
		return listMatchError;
	}
}
