package com.example.firstspringbootbyrahul.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.firstspringbootbyrahul.entities.Post;

// @Repository
// public interface PostRepository extends JpaRepository<Post, Long> {
//     List<Post> findByAuthor(String author);
//     List<Post> findByTitleContainingIgnoreCase(String title);
// }

//======ADDING PAGINATION======
// What this does
// Allows paginated search by title
// Allows paginated filter by author
// Spring automatically generates SQL like:
// SELECT * FROM posts WHERE title ILIKE '%spring%' LIMIT ? OFFSET ?

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Post> findByAuthor(String author, Pageable pageable);
}
