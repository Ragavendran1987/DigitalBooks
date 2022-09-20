package com.digitalBook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digitalBook.models.Book;
import com.digitalBook.models.ERole;
import com.digitalBook.models.Role;

@Repository
public interface  BookRepository extends JpaRepository<Book,Integer> {
	Optional<Book> findByBookTitle(String name);
	
}

