package com.quartzbatchmanager.batch.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/batch")
@RequiredArgsConstructor
public class BatchController {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @RequestMapping("/start")
    public void start(@RequestParam String jobName, @RequestParam String reqDt) throws Exception {
        Job processJob = jobRegistry.getJob(jobName);
        JobParameters jobParameters = new JobParametersBuilder().addString("reqDt",reqDt).toJobParameters();

        jobLauncher.run(processJob, jobParameters);
    }
}
