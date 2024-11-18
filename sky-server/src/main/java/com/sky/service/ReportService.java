package com.sky.service;

import com.sky.entity.User;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * Get turnover report
     * @param begin
     * @param end
     */
    TurnoverReportVO getTurnover(LocalDate begin, LocalDate end);

    /**
     * Get user statistics
     * @param begin
     * @param end
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);
}
