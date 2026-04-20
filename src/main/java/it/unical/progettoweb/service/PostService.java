package it.unical.progettoweb.service;

import it.unical.progettoweb.dao.PostDao;
import it.unical.progettoweb.dto.PostCreateDto;
import it.unical.progettoweb.dto.PostDto;
import it.unical.progettoweb.model.Post;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {
    private PostDao postDao;

    public PostService(PostDao postDao) {
        this.postDao = postDao;
    }

    public PostDto save(PostCreateDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setPhotos(postDto.getPhotoUrls());
        post.setCreatedAt(LocalDateTime.now());
        post.setCurrentPrice(postDto.getCurrentPrice());
        post.setPreviousPrice(0);
        post.setSellerId(postDto.getSellerId());
        post.setRealEstateId(postDto.getRealEstateId());
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
        return new PostDto(post.getId(), post.getTitle(), post.getDescription(), post.getPreviousPrice(),
                post.getCurrentPrice(), post.getCreatedAt(), post.getSellerId(), post.getRealEstateId());
    }

}
