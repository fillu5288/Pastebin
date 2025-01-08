package com.example.pastebin.controllers;

import com.example.pastebin.dto.PostDTO;
import com.example.pastebin.pojo.Post;
import com.example.pastebin.services.PostService;
import com.example.pastebin.services.UserService;
import com.example.pastebin.util.PostErrorResponse;
import com.example.pastebin.util.PostNotCreated;
import com.example.pastebin.util.PostNotFound;
import com.example.pastebin.util.UserNotCreated;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public PostController(PostService postService,
                          ModelMapper modelMapper,
                          UserService userService) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping()
    public List<PostDTO> getPost() {
        // статус 200, все ок
        return postService.findAll()
                .stream()
                .map(this::convertToPostDTO)
                .collect(Collectors.toList()); //джэксон конвертирует в json
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PostDTO postDTO,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";"); // делает красивенькое сообщение об ошибке
            }

            throw new UserNotCreated(errorMsg.toString());
        }

        Post post = convertToPost(postDTO);
        post.setUser(userService.findOneNameIdOrThrow(post.getUser().getName()));
        postService.save(post);
        return ResponseEntity.ok(HttpStatus.OK); // отправляет ок
    }

    @ExceptionHandler
    private ResponseEntity<PostErrorResponse> handleException(PostNotFound postNotFound) {
        PostErrorResponse response = new PostErrorResponse(
                "Post not found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // статус 404, плохо
    }

    @ExceptionHandler
    private ResponseEntity<PostErrorResponse> handleException(PostNotCreated postNotCreated) {
        PostErrorResponse response = new PostErrorResponse(
                postNotCreated.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // статус 404, плохо
    }

    private Post convertToPost(@Valid PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }

    private PostDTO convertToPostDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }
}
