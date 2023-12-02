package com.example.demo.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Contact;
@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

	Page<Contact> findByNameContainingIgnoreCase(String trim, Pageable pageable);
	Page<Contact> findAll(Pageable pageable);

}
