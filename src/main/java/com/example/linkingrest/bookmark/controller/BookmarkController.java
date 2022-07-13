package com.example.linkingrest.bookmark.controller;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.bookmark.service.BookmarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@Api(tags = {"BOOKMARK API V1"})
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @ApiOperation(value = "BOOKMARK 저장", notes = "user ID와 post ID로 BOOKMARK 저장")
    @ApiResponses({
            @ApiResponse(code=200, message = "북마크 저장"),
            @ApiResponse(code=404, message = "user 또는 post 정보가 없음")
    })
    @PostMapping("/new/user/{userId}/post/{postId}")
    public ResponseEntity<BookmarkResponse> saveBookmark(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId){
        Long id = bookmarkService.saveBookmark(userId, postId);
        return ResponseEntity.ok(new BookmarkResponse(id));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<BookmarkResponse.Result> findBookmarksByUser(@PathVariable("userId") Long userId){
        List<Bookmark> findBookmarks = bookmarkService.findBookmarksByUser(userId);
        List<BookmarkResponse> collect = findBookmarks.stream()
                .map(b -> new BookmarkResponse(b.getId(),b.getUser().getId(),b.getPost().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new BookmarkResponse.Result(collect.size(),collect));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBookmark(@PathVariable("id") Long id){
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.noContent().build();
    }
}
