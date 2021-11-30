package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.controllers.dto.AdvancedSearchRequest;
import com.eleks.academy.pharmagator.controllers.dto.SearchRequest;
import com.eleks.academy.pharmagator.entities.AdvancedSearchView;
import com.eleks.academy.pharmagator.services.AdvancedSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ViewController {

    @Value("${pharmagator.ui.default-page-size}")
    private Integer defaultPageSize;

    private final AdvancedSearchService advancedSearchService;

    @GetMapping("/{page}")
    public String searchMedicines(@PathVariable Integer page,
                                @ModelAttribute(value = "searchRequest") SearchRequest request,
                                Model model) {
        AdvancedSearchRequest advancedSearchRequest = new AdvancedSearchRequest();

        advancedSearchRequest.setMedicine(request.getSearchQuery());

        PageRequest pageRequest = PageRequest.of(page, defaultPageSize, request.getSortDirection(), request.getSortBy());

        Page<AdvancedSearchView> search = advancedSearchService.search(advancedSearchRequest, pageRequest);

        List<AdvancedSearchView> list = search.get().toList();

        model.addAttribute("itemsList", list);

        model.addAttribute("currentPage", page);

        model.addAttribute("pagesCount", search.getTotalPages() - 1);

        model.addAttribute("searchRequest", request);

        log.info(request.toString());

        log.info("Current page = " + page + "; Pages count = " + search.getTotalPages());

        return "index";
    }

    @GetMapping("/")
    public String homePage(Model model,
                           @RequestParam(required = false, defaultValue = "price") String sortBy,
                           @RequestParam(required = false, defaultValue = "ASC") String order) {
        PageRequest pageRequest = PageRequest.of(0, defaultPageSize, Sort.Direction.valueOf(order), sortBy);

        Page<AdvancedSearchView> items = advancedSearchService.findAll(pageRequest);

        int pagesCount = items.getTotalPages();

        model.addAttribute("itemsList", items);

        model.addAttribute("pagesCount", pagesCount);

        model.addAttribute("currentPage", 0);

        model.addAttribute("searchRequest", new SearchRequest());

        return "index";
    }

}
