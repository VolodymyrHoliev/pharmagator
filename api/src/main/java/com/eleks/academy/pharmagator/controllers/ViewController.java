package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.entities.AdvancedSearchView;
import com.eleks.academy.pharmagator.services.AdvancedSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ViewController {

    @Value("${pharmagator.ui.default-page-size}")
    private Integer defaultPageSize;

    private final AdvancedSearchService advancedSearchService;

    @GetMapping("/{page}")
    public String loadPage(Model model, @PathVariable Integer page) {
        page = page < 0 ? 0 : page;

        Page<AdvancedSearchView> items = advancedSearchService.findAll(Pageable.ofSize(defaultPageSize));

        long pagesCount = advancedSearchService.getPagesCount(defaultPageSize);

        model.addAttribute("itemsList", items);

        model.addAttribute("pagesCount", pagesCount);

        model.addAttribute("currentPage", page);

        return "index";
    }

}
