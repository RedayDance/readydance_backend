package com.readydance.backend.job;

import com.readydance.backend.entity.MainPageRec;
import com.readydance.backend.entity.repository.MainRepository;
import com.readydance.backend.validator.FilePathParameterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor //필요한 Bean 와이어링
@Slf4j
public class MainInsertJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MainRepository mainRepository;

    @Bean
    public Job mainInsertJob(Step mainInsertStep) {
        return jobBuilderFactory.get("mainInsertJob")
                .incrementer(new RunIdIncrementer())
                .validator(new FilePathParameterValidator())
                .start(mainInsertStep)
                .build();
    }

    @JobScope
    @Bean
    public Step mainInsertStep(
            StaxEventItemReader<MainPageRec> mainResourceReader,
            ItemWriter<MainPageRec> mainPageRecDataDtoItemWriter
    ){
        return stepBuilderFactory.get("mainInsertStep")
                .<MainPageRec, MainPageRec>chunk(10)
                .reader(mainResourceReader)
                .writer(mainPageRecDataDtoItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public StaxEventItemReader<MainPageRec> mainResourceReader(
         //   @Value("#{jobParameters['filePath']}") String filePath,
            Jaxb2Marshaller mainDtoMarshaller
    ){
        return new StaxEventItemReaderBuilder<MainPageRec>()
                .name("mainResourceReader")
                .resource(new ClassPathResource("rec-api-response.xml")) //읽을 xml 파일 설정
                .addFragmentRootElements("item") //내가 읽어낼 root element 설정
                .unmarshaller(mainDtoMarshaller) //파일을 객체에 매핑할때 쓴다.
                .build();
    }

    @Bean
    @StepScope
    public Jaxb2Marshaller mainDtoMarshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(MainPageRec.class);
        return jaxb2Marshaller;
    }

    @StepScope
    @Bean
    public ItemWriter<MainPageRec> mainPageRecDataDtoItemWriter(){
        return items -> {
            items.forEach(System.out::println);

            ArrayList<MainPageRec> mainList = new ArrayList<MainPageRec>(items);

                mainRepository.deleteAllInBatch();

            for (MainPageRec mainPageRecData : mainList) {
                mainRepository.save(mainPageRecData);
            }
        };
    }
}
