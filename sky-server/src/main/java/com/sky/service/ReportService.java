package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * Get turnover report
     * @param begin
     * @param end
     */
    TurnoverReportVO getTurnover(LocalDate begin, LocalDate end);
}
