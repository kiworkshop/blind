package org.kiworkshop.blind.post.controller;

import lombok.RequiredArgsConstructor;
import org.kiworkshop.blind.comment.controller.dto.CommentRequest;
import org.kiworkshop.blind.comment.controller.dto.CommentResponse;
import org.kiworkshop.blind.comment.service.CommentService;
import org.kiworkshop.blind.post.controller.dto.request.PostRequestDto;
import org.kiworkshop.blind.post.controller.dto.response.PostResponseDto;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping
    public Page<PostResponseDto> getAll(PostPageRequest postPageRequest) {
        PageRequest pageRequest = PageRequest.of(postPageRequest.getPage(), postPageRequest.getSize());
        return postService.getPagable(pageRequest);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Long createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Long> likePost(HttpSession httpSession, @PathVariable Long id) {
        postService.likePost(httpSession, id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto post = postService.getBy(id);
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

    @PostMapping("/{id}/comments/add")
    public CommentResponse addComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        return commentService.addComment(id, request);
    }

    @GetMapping("/{id}/comments/all")
    public List<CommentResponse> getAllComments(@PathVariable Long id) {
        return commentService.getAllComments(id);
    }

    @GetMapping("/{id}/comments/partial")
    public List<CommentResponse> getPartialComments(@PathVariable Long id) {
        Post post = postService.findById(id);
        return commentService.getTopNComments(post);
    }

    @GetMapping("/{id}/comments/rest/{last-index}")
    public List<CommentResponse> getRestComments(@PathVariable("id") Long id, @PathVariable("last-index") Long lastIndex) {
        Post post = postService.findById(id);
        return commentService.getAfterIdComments(post, lastIndex);
    }
}
