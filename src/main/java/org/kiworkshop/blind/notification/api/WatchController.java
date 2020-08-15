package org.kiworkshop.blind.notification.api;

import java.net.URI;
import java.util.List;

import org.kiworkshop.blind.notification.service.WatchService;
import org.kiworkshop.blind.post.controller.dto.response.PostSummaryResponseDto;
import org.kiworkshop.blind.user.controller.dto.UserSummaryResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/watch")
@RestController
public class WatchController {
    private final WatchService watchService;

    @PostMapping
    public ResponseEntity<Void> startWatch(@RequestParam Long postId, @RequestParam Long userId) {
        Long watchId = watchService.startWatch(postId, userId);
        return ResponseEntity
            .created(URI.create("/api/watch/" + watchId))
            .build();
    }

    @GetMapping
    public ResponseEntity<Boolean> fetchWatchId(@RequestParam Long postId, @RequestParam Long userId) {
        Boolean isWatching = watchService.isWatching(postId, userId);
        return ResponseEntity.ok(isWatching);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> stopWatch(@PathVariable("postId") Long postId, @RequestParam Long userId) {
        watchService.stopWatch(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/postlist")
    public ResponseEntity<List<PostSummaryResponseDto>> fetchWatchingList(@RequestParam Long userId) {
        List<PostSummaryResponseDto> posts = watchService.getWatchList(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/userlist")
    public ResponseEntity<List<UserSummaryResponseDto>> fetchWatcherList(@RequestParam Long postId) {
        List<UserSummaryResponseDto> users = watchService.getWatchers(postId);
        return ResponseEntity.ok(users);
    }
}
