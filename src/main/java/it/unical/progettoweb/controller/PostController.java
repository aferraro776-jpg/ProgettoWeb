package it.unical.progettoweb.controller;

import it.unical.progettoweb.dto.PostCreateDto;
import it.unical.progettoweb.dto.PostDto;
import it.unical.progettoweb.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAll(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        return ResponseEntity.ok(postService.getAll(sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(postService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getBySeller(@PathVariable int sellerId) {
        try {
            return ResponseEntity.ok(postService.getBySellerId(sellerId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/realestate/{realEstateId}")
    public ResponseEntity<?> getByRealEstate(@PathVariable int realEstateId) {
        try {
            return ResponseEntity.ok(postService.getByRealEstateId(realEstateId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@RequestBody PostCreateDto post) {
        PostDto savedPost = postService.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody PostCreateDto postDto) {
        try {
            PostDto updated = postService.update(id, postDto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/reduce-price")
    public ResponseEntity<?> reducePrice(@PathVariable int id, @RequestParam double newPrice) {
        try {
            PostDto updated = postService.reducePrice(id, newPrice);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            postService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}