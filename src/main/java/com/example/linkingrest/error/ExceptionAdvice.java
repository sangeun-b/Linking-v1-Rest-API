package com.example.linkingrest.error;

import com.example.linkingrest.error.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler({Exception.class,RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse defaultException(HttpServletRequest request, Exception e){
        log.error("DEFAULT ERROR",e);
        return new ErrorResponse(Messages.UNEXPECTED_EXCEPTION_MESSAGE);

    }
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFoundException(UserNotFoundException e){
        log.error("USER NOT FOUND", e);
        return new ErrorResponse(Messages.NOT_USER_MESSAGE);
    }
    @ExceptionHandler(EmailSignupFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse duplicateEmailException(EmailSignupFailedException e){
        log.error("DUPLICATE EMAIL", e);
        return new ErrorResponse(Messages.DUPLICATE_EMAIL_MESSAGE);
    }

    @ExceptionHandler(EmailLoginFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse loginFailException(EmailLoginFailedException e){
        log.error("LOGIN FAIL",e);
        return new ErrorResponse(Messages.LOGIN_FAIL_MESSAGE);
    }
    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse postNotFoundException(PostNotFoundException e){
        log.error("POST NOT FOUND", e);
        return new ErrorResponse(Messages.NOT_POST_MESSAGE);
    }
    @ExceptionHandler(BookmarkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse bookmarkNotFoundException(BookmarkNotFoundException e){
        log.error("BOOKMARK NOT FOUND", e);
        return new ErrorResponse(Messages.NOT_BOOKMARK_MESSAGE);
    }
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse commentNotFoundException(CommentNotFoundException e){
        log.error("COMMENT NOT FOUND", e);
        return new ErrorResponse(Messages.NOT_COMMENT_MESSAGE);
    }



}
