package com.example.demo.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.OrderProducts;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts, Integer>{

}
