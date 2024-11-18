package com.sky.service;

import com.sky.entity.User;
import com.sky.vo.*;

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

    /**
     * Get order statistics
     * @param begin
     * @param end
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * Get top 10 sales statistics
     * @param begin
     * @param end
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
}
