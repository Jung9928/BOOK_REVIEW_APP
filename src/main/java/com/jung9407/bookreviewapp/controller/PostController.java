package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.PostCreateDTO;
import com.jung9407.bookreviewapp.model.dto.PostModifyDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.PostModifyRequestDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.PostModifyResponseDTO;
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

    // 게시글 작성
    @PostMapping
    public ResponseResultCode<Void> create(@RequestBody PostCreateDTO postCreateDTO, Authentication authentication) {
        postService.create(postCreateDTO.getTitle(), postCreateDTO.getContent(), authentication.getName());
        return ResponseResultCode.success();
    }


    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseResultCode<PostModifyResponseDTO> modify(@PathVariable Long postId, @RequestBody PostModifyRequestDTO postModifyRequestDTO, Authentication authentication) {
        PostModifyResponseDTO postModifyResponseDTO = postService.modify(postModifyRequestDTO.getTitle(), postModifyRequestDTO.getContent(), authentication.getName(), postId);
        System.out.println("========================================");
        System.out.println("postId : " + postModifyResponseDTO.getPostId());
        System.out.println("title : " + postModifyResponseDTO.getTitle());
        System.out.println("content : " + postModifyResponseDTO.getContent());
        System.out.println("========================================");

        return ResponseResultCode.success(postModifyResponseDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseResultCode<Void> delete(@PathVariable Long postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return ResponseResultCode.success();
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
