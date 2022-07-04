package com.example.linkingrest.bookmark.controller;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/new/user/{userId}/post/{postId}")
    public BookmarkResponse saveBookmark(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId){
        Long id = bookmarkService.saveBookmark(userId, postId);
        return  new BookmarkResponse(id);
    }

    @GetMapping("/users/{userId}")
    public BookmarkResponse.Result findBookmarksByUser(@PathVariable("userId") Long userId){
        List<Bookmark> findBookmarks = bookmarkService.findBookmarksByUser(userId);
        List<BookmarkResponse> collect = findBookmarks.stream()
                .map(b -> new BookmarkResponse(b.getId(),b.getUser().getId(),b.getPost().getId()))
                .collect(Collectors.toList());
        return new BookmarkResponse.Result(collect);
    }

    @DeleteMapping("/{id}")
    public void deleteBookmark(@PathVariable("id") Long id){
        bookmarkService.deleteBookmark(id);
    }
}
