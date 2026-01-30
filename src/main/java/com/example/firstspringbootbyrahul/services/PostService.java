package com.example.firstspringbootbyrahul.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.firstspringbootbyrahul.entities.Post;
import com.example.firstspringbootbyrahul.repositories.PostRepository;

@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Create
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // Read All
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Read by ID
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    // Read by Author
    public List<Post> getPostsByAuthor(String author) {
        return postRepository.findByAuthor(author);
    }

    // Search by Title
    public List<Post> searchPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title);
    }

    // Update (Full Replace - PUT)
    public Post updatePost(Long id, Post postDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setAuthor(postDetails.getAuthor());
        // Note: createdAt remains unchanged
        
        return postRepository.save(post);
    }

    // Partial Update (PATCH)
    public Post patchPost(Long id, Post postDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        
        // Only update fields that are not null
        if (postDetails.getTitle() != null) {
            post.setTitle(postDetails.getTitle());
        }
        if (postDetails.getContent() != null) {
            post.setContent(postDetails.getContent());
        }
        if (postDetails.getAuthor() != null) {
            post.setAuthor(postDetails.getAuthor());
        }
        
        return postRepository.save(post);
    }

    // Delete
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        postRepository.delete(post);
    }

    // Check if exists
    public boolean existsById(Long id) {
        return postRepository.existsById(id);
    }
}