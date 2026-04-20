package it.unical.progettoweb.controller;

import it.unical.progettoweb.dao.impl.PostDaoImpl;
import it.unical.progettoweb.dto.PostCreateDto;
import it.unical.progettoweb.dto.PostDto;
import it.unical.progettoweb.model.Post;
import it.unical.progettoweb.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final PostDaoImpl postDao;

    public PostController(PostDaoImpl postDao,PostService postService) {
        this.postDao = postDao;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAll() {
        return ResponseEntity.ok(postDao.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable int id) {
        return postDao.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Post>> getBySeller(@PathVariable int sellerId) {
        return ResponseEntity.ok(postDao.findBySellerId(sellerId));
    }

    @GetMapping("/realestate/{realEstateId}")
    public ResponseEntity<List<Post>> getByRealEstate(@PathVariable int realEstateId) {
        return ResponseEntity.ok(postDao.findByRealEstateId(realEstateId));
    }

    @GetMapping("/order/price/asc")
    public ResponseEntity<List<Post>> orderByPriceAsc() {
        return ResponseEntity.ok(postDao.findAllOrderByPriceAsc());
    }

    @GetMapping("/order/price/desc")
    public ResponseEntity<List<Post>> orderByPriceDesc() {
        return ResponseEntity.ok(postDao.findAllOrderByPriceDesc());
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@RequestBody PostCreateDto post) {
        PostDto savedPost = postService.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Post post) {
        return postDao.get(id).map(existing -> {
            post.setId(id);
            postDao.update(post);
            return ResponseEntity.ok("Post updated");
        }).orElse(ResponseEntity.notFound().build());
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
    public ResponseEntity<String> delete(@PathVariable int id) {
        return postDao.get(id).map(existing -> {
            postDao.delete(id);
            return ResponseEntity.ok("Post deleted");
        }).orElse(ResponseEntity.notFound().build());
    }
}