package it.unical.progettoweb.service;

import it.unical.progettoweb.dao.PostDao;
import it.unical.progettoweb.dto.request.PostRequest;
import it.unical.progettoweb.dto.response.PostDto;
import it.unical.progettoweb.model.Post;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PostService {
    private final PostDao postDao;

    public PostService(PostDao postDao) {
        this.postDao = postDao;
    }

    public PostDto save(PostRequest postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setPhotos(postDto.getPhotoUrls());
        post.setCreatedAt(LocalDateTime.now());
        post.setCurrentPrice(postDto.getCurrentPrice());
        post.setPreviousPrice(0);
        post.setSellerId(postDto.getSellerId());
        post.setRealEstateId(postDto.getRealEstateId());
        post.setId(generateUniqueId());
        post = postDao.save(post);
        return toDto(post);
    }

    public PostDto reducePrice(int id, double newPrice) {
        Post existing = postDao.get(id).orElse(null);

        if (existing == null) {
            throw new RuntimeException("Post non trovato");
        }

        if (newPrice >= existing.getCurrentPrice()) {
            throw new IllegalArgumentException("Il nuovo prezzo deve essere inferiore a quello attuale");
        }

        existing.setPreviousPrice(existing.getCurrentPrice());
        existing.setCurrentPrice(newPrice);
        Post saved = postDao.save(existing);
        return toDto(saved);
    }

    private PostDto toDto(Post post) {
        return new PostDto(
                post.getId(), post.getTitle(), post.getDescription(),
                post.getPreviousPrice(), post.getCurrentPrice(),
                post.getCreatedAt(), post.getSellerId(), post.getRealEstateId(),
                post.getPhotos()
        );
    }

    public PostDto update(int id, PostRequest postDto) {
        Optional<Post> existing = postDao.get(id);

        if (existing.isEmpty()) {
            throw new RuntimeException("Post non trovato");
        }

        Post post = existing.get();

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setCurrentPrice(postDto.getCurrentPrice());
        post.setPhotos(postDto.getPhotoUrls());
        post.setRealEstateId(postDto.getRealEstateId());

        Post saved = postDao.update(post);
        return toDto(saved);
    }

    public void delete(int id) {
        Optional<Post> existing = postDao.get(id);

        if (existing.isEmpty()) {
            throw new RuntimeException("Post non trovato");
        }

        postDao.delete(id);
    }

    public List<PostDto> getAll(String sortBy, String direction) {
        List<Post> posts = postDao.getAll();
        if (sortBy != null && sortBy.equalsIgnoreCase("price")) {
            for (int i = 0; i < posts.size() - 1; i++) {
                for (int j = 0; j < posts.size() - i - 1; j++) {
                    Post a = posts.get(j);
                    Post b = posts.get(j + 1);

                    boolean scambia = "desc".equalsIgnoreCase(direction)
                            ? a.getCurrentPrice() < b.getCurrentPrice()
                            : a.getCurrentPrice() > b.getCurrentPrice();
                    if (scambia) {
                        posts.set(j, b);
                        posts.set(j + 1, a);
                    }
                }
            }
        }
        List<PostDto> result = new ArrayList<>();
        for (Post p : posts) {
            result.add(toDto(p));
        }
        return result;
    }

    public PostDto getById(int id) {
        Optional<Post> existing = postDao.get(id);

        if (existing.isEmpty()) {
            throw new RuntimeException("Post non trovato");
        }

        return toDto(existing.get());
    }

    public List<PostDto> getBySellerId(int sellerId) {
        List<Post> posts = postDao.findBySellerId(sellerId);

        if (posts == null || posts.isEmpty()) {
            throw new RuntimeException("Nessun post trovato per il venditore con id: " + sellerId);
        }

        List<PostDto> result = new ArrayList<>();
        for (Post p : posts) {
            result.add(toDto(p));
        }
        return result;
    }

    public List<PostDto> getByRealEstateId(int realEstateId) {
        List<Post> posts = postDao.findByRealEstateId(realEstateId);

        if (posts == null || posts.isEmpty()) {
            throw new RuntimeException("Nessun post trovato per l'immobile con id: " + realEstateId);
        }

        List<PostDto> result = new ArrayList<>();
        for (Post p : posts) {
            result.add(toDto(p));
        }
        return result;
    }
    private int generateUniqueId() {
        int id;
        do {
            Random random = new Random();
            id = random.nextInt(89999) + 10000;
        } while (postDao.get(id).isPresent());
        return id;
    }
}