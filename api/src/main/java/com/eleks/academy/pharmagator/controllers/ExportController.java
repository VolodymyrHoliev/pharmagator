package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.services.ExportService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/export")
public class ExportController {
    private final ExportService exportService;

    @SneakyThrows
    @GetMapping
    public void export(HttpServletResponse response) {
        XSSFWorkbook workbook = exportService.getExportData();
        ServletOutputStream outputStream = response.getOutputStream();

        response.addHeader("Content-Disposition", "attachment; filename=export.xlsx");

        workbook.write(outputStream);

        workbook.close();
    }
}
