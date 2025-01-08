package com.example.pastebin.controllers;

import com.example.pastebin.dto.UserDTO;
import com.example.pastebin.pojo.User;
import com.example.pastebin.services.UserService;
import com.example.pastebin.util.UserErrorResponse;
import com.example.pastebin.util.UserNotCreated;
import com.example.pastebin.util.UserNotFound;
import com.example.pastebin.validator.UserValidator;
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
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService,
                          ModelMapper modelMapper,
                          UserValidator userValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public List<UserDTO> getUser() {
        return userService.findAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") String name) {
        return convertToUserDTO(userService.findOneByName(name));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";"); // делает красивенькое сообщение об ошибке
            }

            throw new UserNotCreated(errorMsg.toString());
        }

        userService.save(convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);        // отправляет ок

    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFound userNotFound) {
        UserErrorResponse response = new UserErrorResponse(
                "User not found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // статус 404, плохо
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreated userNotCreated) {
        UserErrorResponse response = new UserErrorResponse(
                userNotCreated.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // статус 404, плохо
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}

