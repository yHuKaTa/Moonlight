package com.aacdemy.moonlight.controller;
import com.aacdemy.moonlight.dto.car.FileResourceResponseDto;
import com.aacdemy.moonlight.service.FileResourceService;
import com.aacdemy.moonlight.util.image.ValidImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping(path = "/files")
@RequiredArgsConstructor
@Validated
public class FileResourceController {
    private final FileResourceService fileResourceService;

    @PostMapping(path = "/images/add")
    @Operation(summary = "Add new image of car", description = "Add new image of car by providing car's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileResourceResponseDto.class), examples = {@ExampleObject(value = """                                                                    
                    {
                        "id": "1",
                        "fileName": "94ef308a-7dda-4d55-b08b-c71f089d47dc",
                        "carId": "1"
                    }""")})),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "message" : "Constraint violation error:",
                "errors" : [
                    "Invalid image file",
                    "Only JPG images are allowed."
                ]
            }"""))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                    "timestamp": "2023-04-25T15:12:55.260+00:00",
                    "status": 403,
                    "error": "Forbidden",
                    "path": "/cars/addCar"
            }"""))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            Car with ID 2 isn't found!
            """)))
    })
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<FileResourceResponseDto> uploadImageOfCarToDB(@RequestHeader("Authorization") @RequestParam("image") @ValidImage MultipartFile[] images, @RequestParam("carId") @Valid @Pattern(regexp = "[\\d]+", message = "Car ID must consist digits!") String carId) throws IOException, SQLIntegrityConstraintViolationException {
        return ResponseEntity.status(HttpStatus.OK).body(fileResourceService.uploadImages(images, Long.parseLong(carId)));
    }

    @GetMapping(path = "images/{id}")
    @Operation(summary = "Download image by ID", description = "Downloads an image file by its ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FileResourceResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/files/images/{id}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/files/images/{id}"
            }""")))
    })
    public ResponseEntity<StreamingResponseBody> downloadImageById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-disposition", "attachment;filename=image.jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .body(fileResourceService.downloadImageById(id));
    }

    @GetMapping(path = "images/download/{id}/first", produces = "application/jpeg")
    @Operation(summary = "Download image by ID first", description = "Downloads an image file by its ID first.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FileResourceResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/files/images/download/{id}/first"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/files/images/download/{id}/first"
            }""")))
    })
    public ResponseEntity<StreamingResponseBody> downloadFirstImageOfCar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-disposition", "attachment;filename=image.jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .body(fileResourceService.downloadFirstImageOfCarByCarId(id));
    }

    @GetMapping(path = "/images/download/{id}", produces = "application/zip")
    @Operation(summary = "Download image by ID", description = "Downloads an image file by its ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FileResourceResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 404,
                "error": "Not Found",
                "path": "/files/images/download/{id}"
            }"""))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"), examples = @ExampleObject(value = """
            {
                "timestamp": "2023-04-25T15:27:05.776+00:00",
                "status": 500,
                "error": "Internal Server Error",
                "path": "/files/images/download/{id}"
            }""")))
    })
    public ResponseEntity<StreamingResponseBody> downloadImagesOfCar(@PathVariable Long id) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-disposition", "attachment;filename=images.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResourceService.downloadImagesOfCarByCarId(id));
    }
}