package com.github.frankiie.springboot.controllers.api;

import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.frankiie.springboot.domain.collection.entity.Collection;
import com.github.frankiie.springboot.domain.collection.payload.CollectionResponse;
import com.github.frankiie.springboot.domain.collection.payload.CreateCollectionProps;
import com.github.frankiie.springboot.domain.collection.payload.UpdateCollectionProps;
import com.github.frankiie.springboot.domain.collection.service.CollectionService;
import com.github.frankiie.springboot.domain.collection_image.entity.Image;
import com.github.frankiie.springboot.domain.collection_image.payload.CreateImageProps;
import com.github.frankiie.springboot.domain.collection_image.payload.ImageResponse;
import com.github.frankiie.springboot.domain.collection_note_comment.entity.Comment;
import com.github.frankiie.springboot.domain.collection_note_comment.payload.CreateCommentProps;
import com.github.frankiie.springboot.domain.pagination.model.Page;

import static com.github.frankiie.springboot.utils.Responses.created;
import static com.github.frankiie.springboot.utils.Responses.ok;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static com.github.frankiie.springboot.utils.Responses.created;
import static com.github.frankiie.springboot.utils.Responses.ok;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static com.github.frankiie.springboot.constants.VALUES.DEFAULT_PAGE_NUMBER;
import static com.github.frankiie.springboot.constants.VALUES.DEFAULT_PAGE_SIZE;

@RequiredArgsConstructor
@RestController
@Tag(name = "Collections management")
@RequestMapping("/api/collections")
public class CollectionsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionsController.class);

    @Autowired private final CollectionService collectionService;

    @GetMapping
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Returns a list of courses with paging")
    public ResponseEntity<Page<CollectionResponse>> getMany(Optional<Integer> page, Optional<Integer> size) {
        var content = collectionService.findMany(page, size);
        return ok(content.map(CollectionResponse::new));
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(summary = "")
    public ResponseEntity<CollectionResponse> getOne(@PathVariable Long id) {
      var collection = collectionService.findById(id);
      var response = new CollectionResponse(collection);
      return ok(response);
    }

    @PostMapping
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(
        summary = "",
        description = ""
    )
    public ResponseEntity<CollectionResponse> save(@Validated @ModelAttribute CreateCollectionProps props) {
      var collection = collectionService.create(props);
      LOGGER.info(collection.toString());
      var response = new CollectionResponse(collection);
      return created(response, "api/courses");
    }

    @PutMapping("/{id}/addComment")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(
        summary = "",
        description = ""
    )
    public ResponseEntity<Comment> addOrUpdateComment(@PathVariable Long id,  @Validated @RequestBody CreateCommentProps createProps) {
      var comment = collectionService.updateByAddingComment(id, createProps);
      return created(comment, "/api/courses/" + id + "/addComment");
    }

    @PutMapping("/{id}/addImage")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(
        summary = "",
        description = ""
    )
    public ResponseEntity<Image> addOrUpdateImage(@PathVariable Long id,  @Validated @ModelAttribute CreateImageProps createProps) {
      var image = collectionService.updateByAddingImage(id, createProps);
      // var response = new  ImageResponse(image);
      LOGGER.info(image.toString());
      return created(image, "/api/collections/" + id + "/addImage");
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(
        summary = "",
        description = ""
    )
    public ResponseEntity<CollectionResponse> updateById(@PathVariable Long id,  @Validated @RequestBody UpdateCollectionProps updateProps) {
      var collection = collectionService.updateById(id, updateProps);
      var response = new CollectionResponse(collection);
      LOGGER.info(collection.toString());
      return created(response, "/api/collections/" + id);
    }

    @GetMapping("/{id}/comments")
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(summary = "Returns a list of courses with paging")
    public ResponseEntity<Page<Comment>> getCommentsById(
        @PathVariable Long id, 
         Optional<Integer> page, 
        Optional<Integer> size 
    )  {
      var response = collectionService.findCommentsById(id, page, size);
      return created(response, "/api/collections/comments", id);
    }


    @GetMapping("/showByKeyword")
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(summary = "Returns a list of courses with paging")
    public ResponseEntity<Page<CollectionResponse>> getByKeyword(
        @RequestParam String keyword, 
         Optional<Integer> page, 
        Optional<Integer> size 
    )  {
      LOGGER.info("keyword: " + keyword);
      var collections = collectionService.findByKeyword(keyword, page, size);
      return ok(collections.map(CollectionResponse::new));
    }

}
