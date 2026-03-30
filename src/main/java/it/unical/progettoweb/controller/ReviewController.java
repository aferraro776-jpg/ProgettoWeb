package it.unical.progettoweb.controller;

import it.unical.progettoweb.dao.impl.ReviewDaoImpl;
import it.unical.progettoweb.model.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewDaoImpl reviewDao;

    public ReviewController(ReviewDaoImpl reviewDao) {
        this.reviewDao = reviewDao;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAll() {
        return ResponseEntity.ok(reviewDao.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getById(@PathVariable int id) {
        return reviewDao.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/realestate/{realEstateId}")
    public ResponseEntity<List<Review>> getByRealEstate(@PathVariable int realEstateId) {
        return ResponseEntity.ok(reviewDao.findByRealEstateId(realEstateId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getByUser(@PathVariable int userId) {
        return ResponseEntity.ok(reviewDao.findByUserId(userId));
    }

    @GetMapping("/realestate/{realEstateId}/average")
    public ResponseEntity<Map<String, Double>> getAverageRating(@PathVariable int realEstateId) {
        Double avg = reviewDao.getAverageRatingForRealEstate(realEstateId);
        return ResponseEntity.ok(Map.of("averageRating", avg));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Review review) {
        if (review.getId() == 0) {
            review.setId(new Random().nextInt(89999) + 10000);
        }

        if (review.getRating() < 0 || review.getRating() > 5) {
            return ResponseEntity.badRequest().body("Il rating deve essere tra 0 e 5");
        }

        reviewDao.save(review);
        return ResponseEntity.ok("Review created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Review review) {
        return reviewDao.get(id).map(existing -> {
            review.setId(id);
            if (review.getRating() < 0 || review.getRating() > 5) {
                return ResponseEntity.badRequest().body("Il rating deve essere tra 0 e 5");
            }
            reviewDao.update(review);
            return ResponseEntity.ok("Review updated");
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return reviewDao.get(id).map(existing -> {
            reviewDao.delete(id);
            return ResponseEntity.ok("Review deleted");
        }).orElse(ResponseEntity.notFound().build());
    }
}