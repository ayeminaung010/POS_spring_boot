package com.example.demo.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Contact;
@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

}
