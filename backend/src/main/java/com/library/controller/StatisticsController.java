package com.library.controller;

import com.library.dto.Result;
import com.library.dto.StatisticsDTO;
import com.library.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public Result<StatisticsDTO> getStatistics() {
        return Result.success(statisticsService.getStatistics());
    }
}
