package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.dto.user.*;
import com.aacdemy.moonlight.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
     * to be updated according to application's need
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
                {
                        "timestamp": "2023-04-25T15:12:55.260+00:00",
                        "status": 403,
                        "error": "Forbidden",
                        "path": "/users"
                }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/users"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/users"
            }""")))
    })
    public ResponseEntity<List<UserResponseDto>> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        List<UserResponseDto> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    /*
     * to be updated according to application's need
     */
    @Operation(summary = "Get user by ID", description = "Retrieves a user by ID.")
    @PreAuthorize("#id == authentication.getPrincipal().id or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class), examples = @ExampleObject(value = """
            {
                "id": 123,
                "name": "Sam Smit",
                "email": "sam@example.com",
                "role": "USER"
            }"""))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:12:55.260+00:00",
                "status": 403,
                "error": "Forbidden",
                "path": "/users/{id}"
            }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/users/{id}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/users/{id}"
            }""")))
    })
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /*
     * to be updated according to application's need
     */
    @Operation(summary = "Delete user by ID", description = "Deletes a user by ID.")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:12:55.260+00:00",
                "status": 403,
                "error": "Forbidden",
                "path": "/users/{id}"
            }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/users/{id}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/users/{id}"
            }""")))
    })
    public ResponseEntity<HttpStatus> deleteUser(
            @Parameter(description = "The ID of the user to delete", example = "1", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/update-password")
    @PreAuthorize("isAuthenticated")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class), examples = {@ExampleObject(value = """                                                                    
            {
                "currentPassword": "password",
                "newPassword": "NewPassword"
            }
            }""")})), @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            { Your new password should have:
             at least one upper case English letter
             at least one lower case English letter
             at least one digit
             at least one special character
             minimum eight in length
             }"""))),@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                   {
                     "timestamp": "2023-04-25T15:27:05.776+00:00",
                     "status": 404,
                     "error": "User not found",
                     "path": "/users/update-password"
                     }
            }""")))})
    public ResponseEntity<UserPasswordUpdateResponseDto> updatePassword(
            @Valid @RequestBody UserPasswordUpdateRequestDto passwordUpdateRequestDto,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        String newPassword = passwordUpdateRequestDto.getNewPassword();
        String currentPassword = passwordUpdateRequestDto.getCurrentPassword();
        String confirmPassword = passwordUpdateRequestDto.getConfirmPassword();

        UserPasswordUpdateResponseDto passwordUpdateResponseDto = userService.updatePassword(token, currentPassword, newPassword, confirmPassword);

        return ResponseEntity.ok(passwordUpdateResponseDto);
    }
    /*
     * to be updated according to application's need
     */
    @Operation(summary = "Update user", description = "Updates an existing user.")
    @PreAuthorize("isAuthenticated")
    @ApiResponses(value = {@ApiResponse(responseCode = "500", ref = "internalServerErrorResponseAPI"), //uses the global defined ApiResponses
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class), examples = {@ExampleObject(value = """
                    {   "firstName": "LillyUpdated",
                        "lastName": "Ivanova",
                        "email":"testupd1@gmail.com",
                        "phoneNumber": "0899044888",
                        "passportID": "123456YYY",
                        "password": "Ss1!aaaaa",
                        "confirmPassword": "Ss1!aaaaa"
                    }""")})), @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            { Your new password should have:
             at least one upper case English letter
             at least one lower case English letter
             at least one digit
             at least one special character
             minimum eight in length
             }"""))), @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"), examples = @ExampleObject(value = "Duplicates for email not allowed")))})
    @PutMapping("/update-user")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserUpdateDto userUpdateDto, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);

        UserResponseDto updatedUser = userService.updateUser(userUpdateDto, token);
        return ResponseEntity.ok(updatedUser);
    }

    /*
     * to be updated according to application's need
     */
    @Operation(summary = "Get user by email", description = "Retrieves a user by email.")
    @PreAuthorize("#email == authentication.name or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/email/{email}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:12:55.260+00:00",
                "status": 403,
                "error": "Forbidden",
                "path": "/users/email/{email}"
            }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/users/email/{email}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/users/email/{email}"
            }""")))
    })
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @Parameter(description = "The email of the user to retrieve", example = "john@example.com", required = true)
            @PathVariable String email) {

        UserResponseDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /*
     * to be updated according to application's need
     */
    @Operation(summary = "Get user by phone number", description = "Retrieves a user by phone number.")
    @PreAuthorize("#phoneNumber == authentication.getPrincipal().phoneNumber or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/phone/{phoneNumber}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:12:55.260+00:00",
                "status": 403,
                "error": "Forbidden",
                "path": "/users/phone/{phoneNumber}"
            }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/users/phone/{phoneNumber}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/users/phone/{phoneNumber}"
            }""")))
    })
    public List<UserResponseDto> getUserByPhoneNumber(
            @Parameter(description = "The phone number of the user(s) to retrieve", example = "123456789", required = true)
            @PathVariable String phoneNumber) {
        return userService.getUserByPhoneNumber(phoneNumber);
    }

    /*
     * to be updated according to application's need
     */
    @Operation(summary = "Get user by passport ID", description = "Retrieve a user by their passport ID.")
    @PreAuthorize("#passportID == authentication.getPrincipal().passportID or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/passport/{passportID}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:12:55.260+00:00",
                "status": 403,
                "error": "Forbidden",
                "path": "/users/passport/{passportID}"
            }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/users/passport/{passportID}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/users/passport/{passportID}"
            }""")))
    })
    public ResponseEntity<UserResponseDto> getUserByPassportID(
            @Parameter(description = "The passport ID of the user to retrieve", example = "123456789", required = true)
            @PathVariable String passportID) {
        UserResponseDto user = userService.getUserByPassportID(passportID);
        return ResponseEntity.ok(user);
    }

    /*
     * to be updated according to application's need - it is not ok to have access to all users with your firstName
     */
    @GetMapping("/by-first-name/{firstName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get users by first name", description = "Returns a list of users with the given first name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:12:55.260+00:00",
                "status": 403,
                "error": "Forbidden",
                "path": "/users/by-first-name/{firstName}"
            }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/users/by-first-name/{firstName}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/users/by-first-name/{firstName}"
            }""")))
    })
    public List<UserResponseDto> getUsersByFirstName(
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "The first name of the users to retrieve", example = "John", required = true)
            @PathVariable String firstName,
            Authentication authentication) {
        return userService.getUsersByFirstName(firstName);
    }

    /*
     * to be updated according to application's need - it is not ok to have access to all users with your lastName
     */
    @GetMapping("/by-last-name/{lastName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get users by last name", description = "Returns a list of users with the given last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:12:55.260+00:00",
                "status": 403,
                "error": "Forbidden",
                "path": "/users/by-last-name/{lastName}"
            }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/users/by-last-name/{lastName}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/users/by-last-name/{lastName}"
            }""")))
    })
    public List<UserResponseDto> getUsersByLastName(
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "The last name of the users to retrieve", example = "Doe", required = true)
            @PathVariable String lastName) {
        return userService.getUsersByLastName(lastName);
    }

    /*
     * to be updated according to application's need
     */
    @GetMapping("/by-full-name/{firstName}/{lastName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get users by full name", description = "Returns a list of users with the given full name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:12:55.260+00:00",
                "status": 403,
                "error": "Forbidden",
                "path": "/users/by-full-name/{firstName}/{lastName}"
            }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/users/by-full-name/{firstName}/{lastName}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/users/by-full-name/{firstName}/{lastName}"
            }""")))
    })
    public List<UserResponseDto> getUsersByFullName(
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "The first name of the users to retrieve", example = "John", required = true)
            @PathVariable String firstName,
            @Parameter(description = "The last name of the users to retrieve", example = "Doe", required = true)
            @PathVariable String lastName
    ) {
        return userService.getUsersByFullName(firstName, lastName);
    }
}
