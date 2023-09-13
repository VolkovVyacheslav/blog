package com.volkov.blog.comtrollers;

import com.volkov.blog.models.Post;
import com.volkov.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("name", "Блог сайа");
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model){
        model.addAttribute("name", "Добавление статьи");
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons,  @RequestParam String fullText, Model model){
        Post post = new Post(title, anons, fullText);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") Long id, Model model){
        if(!postRepository.existsById(id)){
            return "redirect:/blog";
        }
        model.addAttribute("name", "Статья " + id);
        Optional<Post> post = postRepository.findById(id);
        Post realPost = post.get();
        model.addAttribute("post", realPost);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") Long id, Model model){
        if(!postRepository.existsById(id)){
            return "redirect:/blog";
        }
        model.addAttribute("name", "Редактирование сатьи " + id);
        Optional<Post> post = postRepository.findById(id);
        Post realPost = post.get();
        model.addAttribute("post", realPost);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostEdit(@PathVariable(value = "id") Long id, @RequestParam String title, @RequestParam String anons,  @RequestParam String fullText, Model model){
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFullText(fullText);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/delete")
    public String blogPostDelete(@PathVariable(value = "id") Long id, Model model){
        Post post = postRepository.findById(id).orElseThrow();
       postRepository.delete(post);
        return "redirect:/blog";
    }
}
