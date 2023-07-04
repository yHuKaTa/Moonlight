package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.dto.ContactUsFormRequestDto;
import com.aacdemy.moonlight.dto.ContactUsFormResponseDto;
import com.aacdemy.moonlight.dto.user.*;
import com.aacdemy.moonlight.service.ContactUsFormService;
import com.aacdemy.moonlight.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/public")
public class PublicResourcesController {

    private final UserService userService;
    private final ContactUsFormService contactUsFormService;

    @Autowired
    public PublicResourcesController(UserService userService, ContactUsFormService contactUsFormService) {
        this.userService = userService;
        this.contactUsFormService = contactUsFormService;
    }

    @Operation(summary = "Registers client",
            description = "Registers a new user by providing user's first name, last name, e-mail, phone no and password. " +
                    "The given role is RoleType.CLIENT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", ref = "internalServerErrorResponseAPI"), //uses the global defined ApiResponses when ready
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRegistrationResponseDto.class),
                            examples = {@ExampleObject(value = """                                                                    
                                    {
                                        "message": "Your registration to Moonlight Hotel & Spa successful was successful.
                                         Your login details were sent to:",
                                        "email": "test27@gmail.com",
                                        "status": "201",
                                        "dateCreated": "10/04/2023"
                                    }""")})),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "Email is required" +
                                            }"""))),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "Duplicate entry 'test27@gmail.com' for key 'users.UK_6dotkott2kjsp8vw4d0m25fb7"
                                            }"""))),
    })
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto> registerUser(
            @RequestBody @Valid UserRegistrationRequestDto userRequestDto) {
        UserRegistrationResponseDto user = userService.saveUser(userRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @PostMapping(path = "/login")
    @Operation(summary = "User Login", description = "Authenticate user and generate login response.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserLoginResponseDto.class),
                    examples = {@ExampleObject(value = """                                                                    
                            {
                                "email":"string",
                                "password":"string"
                            }""")})),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "message" : "Invalid request",
                        "errors" : [
                            "Invalid email",
                            "Invalid password"
                        ]
                    }"""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "message" : "Unauthorized",
                        "errors" : [
                            "Invalid credentials"
                        ]
                    }""")))
    })
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset Password",
            description = "Generates a new password and sends it to the user's specified email, if one is registered.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResetPasswordResponseDto.class),
                    examples = {@ExampleObject(value = """                                                                    
                            {
                                "email":"string"
                            }""")})),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"),
                    examples = @ExampleObject(value = """
                    {
                        "message" : "Invalid request",
                        "errors" : [
                            "Invalid email",
                        ]
                    }"""))),

            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/public/reset"
            }""")))
    })
    public ResponseEntity<UserResetPasswordResponseDto> resetPassword(
            @RequestBody @Valid UserResetPasswordRequestDto userRequestDto, BindingResult bindingResult)
            throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
        }
        UserResetPasswordResponseDto user = userService.resetPassword(userRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @PostMapping("/contact")
    @Operation(summary = "Contact Us", description = "Sends a contact form submission.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactUsFormResponseDto.class),
                    examples = {@ExampleObject(value = """                                                                    
                {
                        "name": "John Doe",
                        "email": "mili@gmail.com",
                        "phoneNumber": "+123456789",
                        "message": "Hello, I have a question."
                    }""")})),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactUsFormResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                    {
                        "message" : "Validation errors:",
                        "errors" : [
                            "The name is required!",
                            "The email is required!",
                            "Doesn't seem to be a valid phone number!",
                            "Message is required!"
                        ]
                    }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/public/reset"
            }""")))
    })
    public ResponseEntity<ContactUsFormResponseDto> contactUs(
            @RequestBody @Valid ContactUsFormRequestDto contactUsFormRequestDto) {

        ContactUsFormResponseDto contactUsFormResponseDto = contactUsFormService.save(contactUsFormRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(contactUsFormResponseDto);
    }
}