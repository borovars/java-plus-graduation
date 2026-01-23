package ru.practicum.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentCreateOrUpdateDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.services.interfaces.PrivateCommentService;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {

    private final PrivateCommentService privateCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createComment(@PathVariable(name = "userId") Long userId,
                                        @PathVariable(name = "eventId") Long eventId,
                                        @RequestBody @Valid CommentCreateOrUpdateDto dto) throws ConflictException,
                                                                                                 NotFoundException {
        return privateCommentService.createComment(userId, eventId, dto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto updateComment(@PathVariable(name = "userId") Long userId,
                                        @PathVariable(name = "eventId") Long eventId,
                                        @PathVariable(name = "commentId") Long commentId,
                                        @RequestBody @Valid CommentCreateOrUpdateDto dto) throws ConflictException,
                                                                                                 NotFoundException {
        return privateCommentService.updateComment(userId, eventId, commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable(name = "userId") Long userId,
                              @PathVariable(name = "eventId") Long eventId,
                              @PathVariable(name = "commentId") Long commentId) throws ConflictException,
                                                                                       NotFoundException {
        privateCommentService.deleteComment(userId, eventId, commentId);
    }
}