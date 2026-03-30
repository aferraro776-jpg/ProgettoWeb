package it.unical.progettoweb.controller;

import it.unical.progettoweb.dao.impl.PostDaoImpl;
import it.unical.progettoweb.model.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostDaoImpl postDao;

    public PostController(PostDaoImpl postDao) {
        this.postDao = postDao;
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
    public ResponseEntity<String> create(@RequestBody Post post) {
        postDao.save(post);
        return ResponseEntity.ok("Post created");
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
    public ResponseEntity<String> reducePrice(@PathVariable int id, @RequestBody Map<String, Double> body) {
        double newPrice = body.get("newPrice");
        return postDao.get(id).map(existing -> {
            if (newPrice >= existing.getCurrentPrice()) {
                return ResponseEntity.badRequest().body("Il nuovo prezzo deve essere inferiore a quello attuale");
            }
            postDao.reducePrice(id, newPrice);
            return ResponseEntity.ok("Price reduced");
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return postDao.get(id).map(existing -> {
            postDao.delete(id);
            return ResponseEntity.ok("Post deleted");
        }).orElse(ResponseEntity.notFound().build());
    }
}