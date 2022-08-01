package com.example.linkingrest.bookmark.controller;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.bookmark.service.BookmarkService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@Api(tags = {"BOOKMARK API V1"})
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @ApiOperation(value = "북마크 저장", notes = "user id 와 post id 로 BOOKMARK 저장")
    @ApiResponses({
            @ApiResponse(code=200, message = "북마크 저장"),
            @ApiResponse(code=404, message = "user 또는 post 정보가 없음")
    })
    @PostMapping("/new")
    public ResponseEntity<BookmarkResponse> saveBookmark(@RequestParam(value = "userId") @ApiParam(value = "북마크 등록할 user id", required = true) Long userId,
                                                         @RequestParam(value = "postId") @ApiParam(value = "북마크 등록할 post id", required = true)Long postId){
        Long id = bookmarkService.saveBookmark(userId, postId);
        return ResponseEntity.created(URI.create("/bookmarks/"+id)).body(new BookmarkResponse(id,userId,postId));
    }

    @ApiOperation(value = "회원 별 북마크 조회", notes = "user id로 등록한 북마크 조회")
    @GetMapping("/users/{userId}")
    public ResponseEntity<BookmarkResponse.Result> findBookmarksByUser(@PathVariable("userId") @ApiParam(value = "북마크 조회할 user id", required = true) Long userId){
        List<Bookmark> findBookmarks = bookmarkService.findBookmarksByUser(userId);
        List<BookmarkResponse> collect = findBookmarks.stream()
                .map(b -> new BookmarkResponse(b.getId(),b.getUser().getId(),b.getPost().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new BookmarkResponse.Result(collect.size(),collect));
    }

    @ApiOperation(value = "북마크 삭제", notes = "등록된 북마크 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteBookmark(@PathVariable("id") @ApiParam(value = "삭제할 북마크 id", required = true) Long id){
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.noContent().build();
    }
}
