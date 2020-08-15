package org.kiworkshop.blind.post.controller;

import lombok.RequiredArgsConstructor;

import org.kiworkshop.blind.post.controller.dto.request.PostRequestDto;
import org.kiworkshop.blind.post.controller.dto.response.PostResponseDto;
import org.kiworkshop.blind.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAll() {
        return ResponseEntity.ok(postService.readAll());
    }

    @PostMapping
    public ResponseEntity<Long> createPost(HttpSession httpSession, @RequestBody PostRequestDto postRequestDto) {
        Long id = postService.createPost(httpSession, postRequestDto);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Long> likePost(HttpSession httpSession, @PathVariable Long id) {
        postService.likePost(httpSession, id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto post = postService.readPost(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id,
                                           @RequestBody PostRequestDto postRequestDto) {
        postService.updatePost(id, postRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
