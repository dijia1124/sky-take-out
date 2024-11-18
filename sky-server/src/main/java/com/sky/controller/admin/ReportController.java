package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Report controller
 */

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "Admin end: statistics report interface")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * sales (turnover) statistics
     * @param begin
     * @param end
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("Turnover statistics")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("Turnover statistics: begin={}, end={}", begin, end);
        TurnoverReportVO reportVO = reportService.getTurnover(begin, end);
        log.info("Turnover statistics result: {}", reportVO);
        return Result.success(reportVO);
    }

    /**
     * statistics of new users and total new users
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("User statistics")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        UserReportVO reportVO = reportService.getUserStatistics(begin, end);
        return Result.success(reportVO);
    }

}
