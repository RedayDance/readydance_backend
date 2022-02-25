package com.readydance.backend.job;

import com.readydance.backend.entity.Subway;
import com.readydance.backend.entity.repository.SubwayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.List;

import static com.readydance.backend.job.SubwayFieldSetMapper.*;

@Configuration
@RequiredArgsConstructor
public class SubwayInsertJobConfig {

    private final SubwayRepository subwayRepository;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job subwayInsertJob(Step subwayInsertStep) {
        return jobBuilderFactory.get("subwayInsertJob")
                .incrementer(new RunIdIncrementer())
                .start(subwayInsertStep)
                .build();
    }

    @JobScope
    @Bean
    public Step subwayInsertStep(
            FlatFileItemReader<Subway> flatFileItemReader,
            ItemWriter<Subway> subwayEntityItemWriter
    ){
        return stepBuilderFactory.get("subwayInsertStep")
                .<Subway, Subway>chunk(12)
                .reader(flatFileItemReader)
                .writer(subwayEntityItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Subway> subwayEntityFlatFileItemReader() {
        return new FlatFileItemReaderBuilder<Subway>()
                .name("subwayEntityFlatFileItemReader")
                .delimited()
                .delimiter("\t")
                .names(STATION_NAME,Y,X)
                .linesToSkip(1)
                .fieldSetMapper(new SubwayFieldSetMapper())
                .resource(new ClassPathResource("subway.txt"))
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<Subway> subwayEntityItemWriter(){
        return new ItemWriter<Subway>() {
            @Override
            public void write(List<? extends Subway> items) throws Exception {
               // items.forEach(System.out::println);

                ArrayList<Subway> subwayList = new ArrayList<Subway>(items);

                subwayRepository.deleteAllInBatch();

                for (Subway subwayEntity : subwayList) {
                    subwayRepository.save(subwayEntity);
                }
            }
        };
    }
 }
