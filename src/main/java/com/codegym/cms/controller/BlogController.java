package com.codegym.cms.controller;

import com.codegym.cms.model.Blog;
import com.codegym.cms.model.Category;
import com.codegym.cms.service.BlogService;
import com.codegym.cms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping("create-blog")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("blog/create");
        modelAndView.addObject("blog", new Blog());
        return modelAndView;
    }

    @PostMapping("create-blog")
    public ModelAndView createBlog(@ModelAttribute("blog") Blog blog) {
        blogService.save(blog);
        ModelAndView modelAndView = new ModelAndView("blog/create");
        modelAndView.addObject("blog", blog);
        modelAndView.addObject("message", "Upload successfully");
        return modelAndView;
    }

    @GetMapping("blogs")
    public ModelAndView showList(@RequestParam("s") Optional<String> s, Pageable pageable) {
        Page<Blog> blogs;
        if (s.isPresent()) {
            blogs = blogService.findAllByName(s.get(), pageable);
        }else {
            blogs= blogService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("blog/list");
        modelAndView.addObject("blogs", blogs);
        return modelAndView;
    }

    @GetMapping("edit-blog/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Blog blog = blogService.findById(id);
        if (blog != null) {
            ModelAndView modelAndView = new ModelAndView("blog/edit");
            modelAndView.addObject("blog", blog);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("blog/error.404");
            return modelAndView;
        }
    }

    @PostMapping("edit-blog")
    public ModelAndView updateBlog(@ModelAttribute("blog") Blog blog) {
        blogService.save(blog);
        ModelAndView modelAndView = new ModelAndView("blog/edit");
        modelAndView.addObject("blog", blog);
        modelAndView.addObject("message", "Update successfully!");
        return modelAndView;
    }

    @GetMapping("delete-blog/{id}")
    public ModelAndView deleteBlog(@PathVariable Long id, Pageable pageable) {
        blogService.remove(id);
        ModelAndView modelAndView = new ModelAndView("blog/list");
        modelAndView.addObject("blogs", blogService.findAll(pageable));
        return modelAndView;
    }

    @GetMapping("view-blog/{id}")
    public ModelAndView viewBlog(@PathVariable Long id) {
        Blog blog = blogService.findById(id);
        ModelAndView modelAndView = new ModelAndView("blog/detail");
        modelAndView.addObject("blog", blog);
        return modelAndView;
    }

    //RESTful
    @GetMapping("api/list-blog")
    public ResponseEntity<Page<Blog>> listBlog(Pageable pageable) {
        Page<Blog> blogs = blogService.findAll(pageable);
        if (blogs == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("api/list-category")
    public ResponseEntity<Iterable<Category>> listCategory() {
        Iterable<Category> categories = categoryService.findAll();
        if (categories == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("api/blog-of-category/{id}")
    public ResponseEntity<Iterable<Blog>> showBlogsOfCategory(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Iterable<Blog> blogs = blogService.findAllByCategory(category);
        if (blogs == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("api/blog/{id}")
    public ResponseEntity<Blog> viewBlogById(@PathVariable Long id) {
        Blog blog = blogService.findById(id);
        if (blog == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }


}
