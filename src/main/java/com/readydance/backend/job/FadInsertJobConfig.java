package com.readydance.backend.job;

import com.readydance.backend.entity.Fad;
import com.readydance.backend.entity.repository.FadRepository;
import com.readydance.backend.validator.FilePathParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
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
public class FadInsertJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final FadRepository fadRepository;

    @Bean
    public Job fadInsertJob(Step fadInsertStep) {
        return jobBuilderFactory.get("fadInsertJob")
                .incrementer(new RunIdIncrementer())
                .validator(new FilePathParameterValidator())
                .start(fadInsertStep)
                .build();
    }

    @JobScope
    @Bean
    public Step fadInsertStep(
            StaxEventItemReader<Fad> fadEntityStaxEventItemReader,
            ItemWriter<Fad> fadEntityItemWriter
    ){
        return stepBuilderFactory.get("fadInsertStep")
                .<Fad, Fad>chunk(10)
                .reader(fadEntityStaxEventItemReader)
                .writer(fadEntityItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public StaxEventItemReader<Fad> fadEntityStaxEventItemReader(
//            @Value("#{jobParameters['filePath']}") String filePath,
            Jaxb2Marshaller fadDtoMarshaller
    ){
        return new StaxEventItemReaderBuilder<Fad>()
                .name("fadEntityStaxEventItemReader")
                .resource(new ClassPathResource("fad-api-response.xml")) //읽을 xml 파일 설정
                .addFragmentRootElements("item") //내가 읽어낼 root element 설정
                .unmarshaller(fadDtoMarshaller) //파일을 객체에 매핑할때 쓴다.
                .build();
    }

    @Bean
    @StepScope
    public Jaxb2Marshaller fadDtoMarshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(Fad.class);
        return jaxb2Marshaller;
    }

    @StepScope
    @Bean
    public ItemWriter<Fad> fadEntityItemWriter(){
        return items -> {
            items.forEach(System.out::println);

            ArrayList<Fad> fadList = new ArrayList<Fad>(items);

                fadRepository.deleteAllInBatch();

            for (Fad fadData : fadList) {
                fadRepository.save(fadData);
            }
        };
    }
}
