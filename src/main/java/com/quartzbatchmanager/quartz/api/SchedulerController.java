package com.quartzbatchmanager.quartz.api;

import com.quartzbatchmanager.quartz.application.SchedulerService;
import com.quartzbatchmanager.quartz.dto.SchedulerJobDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/quartz")
@RequiredArgsConstructor
public class SchedulerController {

    private final SchedulerService schedulerService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addScheduleJob(@RequestBody SchedulerJobDTO schedulerJobDTO){
        if(!schedulerService.isJobExists(schedulerJobDTO)){
            schedulerJobDTO.setScheduleStat("ADD");
            schedulerService.addScheduleJob(schedulerJobDTO);
        }else{
            return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
