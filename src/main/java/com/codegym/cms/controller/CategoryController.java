package com.codegym.cms.controller;

import com.codegym.cms.model.Blog;
import com.codegym.cms.model.Category;
import com.codegym.cms.service.BlogService;
import com.codegym.cms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BlogService blogService;

    @GetMapping("view-category/{id}")
    public ModelAndView viewCategory(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return new ModelAndView("error.404");
        }
        Iterable<Blog> blogs = blogService.findAllByCategory(category);
        ModelAndView modelAndView = new ModelAndView("category/view");
        modelAndView.addObject("category", category);
        modelAndView.addObject("blogs", blogs);
        return modelAndView;
    }
}
