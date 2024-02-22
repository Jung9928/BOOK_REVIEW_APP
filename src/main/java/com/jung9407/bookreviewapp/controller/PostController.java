package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.PostCreateDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ResponseResultCode;
import com.jung9407.bookreviewapp.model.entity.PostEntity;
import com.jung9407.bookreviewapp.repository.PostRepository;
import com.jung9407.bookreviewapp.service.PostService;
import com.jung9407.bookreviewapp.util.PostValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private PostRepository postRepository;

    private PostValidator postValidator;

//    @PostMapping("/api/v1/posts")
//    public Map<String, String> post(@RequestBody @Valid PostCreateDTO postCreateDTO) {
//        // 데이터 검증 --> PostCreateDTO에서 @NotBlank로 처리
//        postService.create(postCreateDTO.getTitle(), postCreateDTO.getContent(), "");
//
//        return Map.of();
//    }

    // 게시글 작성
    @PostMapping
    public ResponseResultCode<Void> create(@RequestBody PostCreateDTO postCreateDTO, Authentication authentication) {
        postService.create(postCreateDTO.getTitle(), postCreateDTO.getContent(), authentication.getName());
        return ResponseResultCode.success(null);
    }



    @GetMapping("/api/v1/postList")
    public String list(Model model) {
        List<PostEntity> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "post/list";
    }

    @GetMapping("/api/v1/content")
    public String content(Model model, @RequestParam(required = false) Long postId) {

        if(postId == null) {
            model.addAttribute("post", new PostEntity());
        } else {
            PostEntity post = postRepository.findById(postId).orElse(null);
            model.addAttribute("post", post);
        }

        model.addAttribute("post", new PostEntity());
        return "post/content";
    }

    @PostMapping("/api/v1/content")
    public String postUpload(@ModelAttribute PostEntity postEntity, BindingResult bindingResult) {

        postValidator.validate(postEntity, bindingResult);
        if(bindingResult.hasErrors()) {
            return "post/content";
        }

        postRepository.save(postEntity);
        return "redirect:/post/list";
    }
}
