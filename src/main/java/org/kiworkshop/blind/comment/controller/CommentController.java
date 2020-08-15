package org.kiworkshop.blind.comment.controller;

import org.kiworkshop.blind.comment.controller.dto.CommentRequest;
import org.kiworkshop.blind.comment.controller.dto.CommentResponse;
import org.kiworkshop.blind.comment.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PutMapping("comments/{commentId}/edit")
    public CommentResponse editComment(HttpSession httpSession, @PathVariable("commentId") Long id, @RequestBody CommentRequest request) {
        return commentService.update(httpSession, id, request);
    }

    @DeleteMapping("comments/{commentId}/delete")
    public void deleteComment(HttpSession httpSession, @PathVariable("commentId") Long id) {
        commentService.delete(httpSession, id);
    }
}
