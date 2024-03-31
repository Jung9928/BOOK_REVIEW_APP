package com.jung9407.bookreviewapp.controller;

import com.jung9407.bookreviewapp.model.dto.PostCreateDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.GeneralForumSearchConditionDTO;
import com.jung9407.bookreviewapp.model.dto.requestDTO.PostModifyRequestDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.GeneralForumPagingResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.GeneralForumResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.PostModifyResponseDTO;
import com.jung9407.bookreviewapp.model.dto.responseDTO.ResponseResultCode;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.jung9407.bookreviewapp.repository.GeneralForumRepository;
import com.jung9407.bookreviewapp.service.GeneralForumService;
import com.jung9407.bookreviewapp.util.PostValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class GeneralForumController {

    private final GeneralForumService generalForumService;

    private GeneralForumRepository generalForumRepository;

    private PostValidator postValidator;

    // 게시글 작성
    @PostMapping
    public ResponseResultCode<Void> create(@RequestBody PostCreateDTO postCreateDTO, Authentication authentication) {
        generalForumService.create(postCreateDTO.getTitle(), postCreateDTO.getContent(), authentication.getName());
        return ResponseResultCode.success();
    }


    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseResultCode<PostModifyResponseDTO> modify(@PathVariable Long postId, @RequestBody PostModifyRequestDTO postModifyRequestDTO, Authentication authentication) {
        PostModifyResponseDTO postModifyResponseDTO = generalForumService.modify(postModifyRequestDTO.getTitle(), postModifyRequestDTO.getContent(), authentication.getName(), postId);
        System.out.println("========================================");
        System.out.println("postId : " + postModifyResponseDTO.getPostId());
        System.out.println("title : " + postModifyResponseDTO.getTitle());
        System.out.println("content : " + postModifyResponseDTO.getContent());
        System.out.println("========================================");

        return ResponseResultCode.success(postModifyResponseDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseResultCode<Void> delete(@PathVariable Long postId, Authentication authentication) {
        generalForumService.delete(authentication.getName(), postId);
        return ResponseResultCode.success();
    }


    // 게시글 조회
    @GetMapping("/generalForumList")
    public GeneralForumPagingResponseDTO<List<GeneralForumResponseDTO>> getGeneralForumList(@PageableDefault(sort = {"registeredAt"}) Pageable pageable, GeneralForumSearchConditionDTO generalForumSearchConditionDTO) {
        log.info("searchCategory : " + generalForumSearchConditionDTO.getSearchCategory());
        log.info("searchValue : " + generalForumSearchConditionDTO.getSearchValue());

        return generalForumService.getForumBoardList(pageable, generalForumSearchConditionDTO);
    }

    @GetMapping("/content")
    public String content(Model model, @RequestParam(required = false) Long postId) {

        if(postId == null) {
            model.addAttribute("post", new GeneralForumEntity());
        } else {
            GeneralForumEntity post = generalForumRepository.findById(postId).orElse(null);
            model.addAttribute("post", post);
        }

        model.addAttribute("post", new GeneralForumEntity());
        return "post/content";
    }

    @PostMapping("/content")
    public String postUpload(@ModelAttribute GeneralForumEntity generalForumEntity, BindingResult bindingResult) {

        postValidator.validate(generalForumEntity, bindingResult);
        if(bindingResult.hasErrors()) {
            return "post/content";
        }

        generalForumRepository.save(generalForumEntity);
        return "redirect:/post/list";
    }
}
