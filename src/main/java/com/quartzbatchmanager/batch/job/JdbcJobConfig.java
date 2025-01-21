package com.quartzbatchmanager.batch.job;

import com.quartzbatchmanager.batch.dto.BatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JdbcJobConfig {
    private final DataSource dataSource;

    private static final int chunkSize = 10;

    @Bean
    public Job jdbcJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, Step jdbcStep) {
        log.info("JOB START ==========>");
        return new JobBuilder("jdbcJob", jobRepository)
                .start(jdbcStep)
                .build();
    }

    @Bean
    @JobScope
    public Step jdbcStep(@Value("#{jobParameters[reqDt]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("jdbcStep START **************>");
        return new StepBuilder("jdbcStep", jobRepository)
                .<BatchDto,BatchDto>chunk(chunkSize,transactionManager)
                .reader(jdbcItemReader())
                .processor(loggingProcessor()) // 로그를 출력하는 Processor 추가
                .writer(jdbcItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<BatchDto> jdbcItemReader() {
        log.info("jdbcItemReader START ------------->");
        return new JdbcCursorItemReaderBuilder<BatchDto>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(BatchDto.class))
                .sql("SELECT id, name FROM BATCH_TEST")
                .name("jdbcItemReader")
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<BatchDto> jdbcItemWriter() {
        log.info("jdbcItemWriter START +++++++++++++++++>");
        String sql = "UPDATE BATCH_TEST set name = ? where id = ?";

        return new JdbcBatchItemWriterBuilder<BatchDto>().dataSource(dataSource)
                .sql(sql)
                .itemPreparedStatementSetter((item, ps) -> {
                    log.info(item.getId().toString());
                    ps.setString(1, "SYSTEM");
                    ps.setLong(2, item.getId());
                })
                .assertUpdates(true)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<BatchDto, BatchDto> loggingProcessor() {
        return item -> {
            log.info("Processing item: {}", item);
            return item; // 그대로 반환
        };
    }

//    @Bean
//    public JdbcBatchItemWriter<BatchDto> jdbcItemWriter() {
//        String sql = "UPDATE BATCH_TEST set name = 'SYSTEM' where id = :id";
//        return new JdbcBatchItemWriterBuilder<BatchDto>().dataSource(dataSource)
//                .sql(sql)
//                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//                .assertUpdates(true)
//                .build();
//    }

}
