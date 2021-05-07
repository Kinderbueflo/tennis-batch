package fr.fgirard.batch.score.config;

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

import fr.fgirard.batch.score.ScoreBOMapper;
import fr.fgirard.batch.score.ScoreStepListener;
import fr.fgirard.batch.score.ScoreValidator;
import fr.fgirard.batch.score.ScoreWriter;
import fr.fgirard.batch.score.bo.ScoreBO;
import fr.fgirard.batch.score.bo.ScoresErrorBO;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:application.properties")
public class ScoreConfig {

	@Value("file:${tennis.score.resources}")
	private Resource[] resources;
	
	@Bean
	public Job score (final JobBuilderFactory job, final Step step) {
		return job.get("score")
				.incrementer(new RunIdIncrementer())
				.flow(step)
				.end()
				.build();
	}
	
	@Bean
	public Step step(
			final StepBuilderFactory step,
			final Step scoreStep,
			final Partitioner scorePartitioner,
			final TaskExecutor scoreTaskExecutor,
			final StepExecutionListener scoreListener) {
		
		return step.get("partitionerStep")
				.listener(scoreListener)
				.partitioner("scoreStep", scorePartitioner)
				.taskExecutor(scoreTaskExecutor)
				.step(scoreStep)
				.build();
	}
	
	@Bean
	public Partitioner scorePartitioner() {
		final MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		partitioner.setResources(this.resources);
		return partitioner;
	}
	
	@Bean 
	public TaskExecutor scoreTaskExecutor() {
		final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setCorePoolSize(1);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
	}
	
	@Bean
	public StepExecutionListener scoreListener() {
		return new ScoreStepListener();
	}
	
	@Bean
	public Step scoreStep(
			final StepBuilderFactory step,
			final FlatFileItemReader<ScoreBO> scoreReader,
			final ItemProcessor<ScoreBO, ScoreBO> scoreProcessor,
			final ItemWriter<ScoreBO> scoreWriter) {
		
		return step.get("scoreStep")
				.<ScoreBO, ScoreBO>chunk(50)
				.reader(scoreReader)
				.processor(scoreProcessor)
				.writer(scoreWriter)
				.build();
	}
	
	@Bean
	@StepScope
	public FlatFileItemReader<ScoreBO> scoreReader(@Value("#{stepExecutionContext['fileName']}") final String filename) throws MalformedURLException {
		
		final DefaultLineMapper<ScoreBO> lineMapper = new DefaultLineMapper<>();
		lineMapper.setFieldSetMapper(new ScoreBOMapper());
		lineMapper.setLineTokenizer(new DelimitedLineTokenizer(";"));
		
		final FlatFileItemReader<ScoreBO> reader = new FlatFileItemReader<>();
		reader.setLineMapper(lineMapper);
		reader.setResource(new UrlResource(filename));
		
		return reader;
	}
	
	@Bean
	public ItemProcessor<ScoreBO, ScoreBO> scoreProcessor(final Validator<ScoreBO> scoreValidator) {
		final ValidatingItemProcessor<ScoreBO> processor = new ValidatingItemProcessor<>();
		processor.setValidator(scoreValidator);
		processor.setFilter(true);
		return processor;
	}
	
	@Bean
	public Validator<ScoreBO> scoreValidator() {
		return new ScoreValidator();
	}
	
	@Bean
	public ItemWriter<ScoreBO> scoreWriter() {
		return new ScoreWriter();
	}
	
	@Bean
	public ScoresErrorBO scoresError() {
		final ScoresErrorBO scoresError =  new ScoresErrorBO();
		scoresError.setScoresError(Collections.synchronizedList(new ArrayList<>()));
		return scoresError;
	}
}
