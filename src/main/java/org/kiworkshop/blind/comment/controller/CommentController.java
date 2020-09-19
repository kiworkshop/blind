package org.kiworkshop.blind.comment.controller;

import lombok.RequiredArgsConstructor;
import org.kiworkshop.blind.comment.controller.dto.CommentRequest;
import org.kiworkshop.blind.comment.controller.dto.CommentResponse;
import org.kiworkshop.blind.comment.service.CommentService;
import org.kiworkshop.blind.security.LoginUserHolder;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final LoginUserHolder loginUserHolder;

    @PutMapping("comments/{commentId}/edit")
    public CommentResponse editComment(@PathVariable("commentId") Long id,
                                       @RequestBody CommentRequest request) {
        return commentService.update(id, request, loginUserHolder.get());
    }

    @DeleteMapping("comments/{commentId}/delete")
    public void deleteComment(@PathVariable("commentId") Long id) {
        commentService.delete(id, loginUserHolder.get());
    }
}
