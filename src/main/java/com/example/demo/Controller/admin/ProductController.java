package com.example.demo.Controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.ProductRepository;
import com.example.demo.model.Product;
import com.example.demo.service.CloudinaryImageService;

import jakarta.validation.Valid;

@Controller
public class ProductController {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CloudinaryImageService cloudinaryImageService;

	@GetMapping("/product")
	public String products(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdTime") String sortBy,
			Model model) {
		Page<Product> productPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			productPage = productRepository.searchProducts(query.trim(),pageRequest);
		} else {
			productPage = productRepository.findAll(pageRequest);
		}
		List<Product> products = productPage.getContent();
		model.addAttribute("products", products);
		Product product = new Product();
		model.addAttribute("product", product);
		
	    model.addAttribute("currentPage", productPage.getNumber() + 1);
	    model.addAttribute("totalPages", productPage.getTotalPages());
	    
		return "admin/product/index";
	}
	

	@GetMapping("/product/add")
	public String addProduct(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		return "admin/product/add";
	}

	@PostMapping("/product/save")
	public String saveProduct(@RequestParam("thumbImages") MultipartFile imagesFile,
			@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (bindingResult.hasErrors()) {
			return "/admin/product/add";
		}

		try {
			String imageURL = cloudinaryImageService.uploadFile(imagesFile);
			product.setThumbnailImage(imageURL);

			productRepository.save(product);

		} catch (Exception e) {

			e.printStackTrace();
		}

		redirectAttributes.addFlashAttribute("success", "Product Add Successful!");
		return "redirect:/product";
	}

	@GetMapping("/product/details/{id}")
	public String productDetail(@PathVariable("id") Integer id, Model model) {
		Product product = productRepository.getReferenceById(id);
		System.out.println(product.getSubCategory().getCategory());
		model.addAttribute("product", product);

		return "admin/product/show";
	}

	@GetMapping("/product/edit/{id}")
	public String productEdit(@PathVariable("id") Integer id, Model model) {
		Product product = productRepository.getReferenceById(id);
		model.addAttribute("product", product);
		return "admin/product/update";
	}

	@PostMapping("/product/update/{id}")
	public String productUpdate(@PathVariable("id") Integer id,
			@RequestParam(name = "thumbImages", required = false) MultipartFile imagesFile,
			@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (bindingResult.hasErrors()) {
			return "/admin/product/update";
		}

		Product existingProduct = productRepository.getReferenceById(id);
		// Check if a new image is provided
		if (imagesFile != null && !imagesFile.isEmpty()) {
			try {
				// Delete the existing thumbnail image
				cloudinaryImageService.deleteFile(existingProduct.getThumbnailImage());

				// Upload and set the new thumbnail image
				String imageURL = cloudinaryImageService.uploadFile(imagesFile);
				existingProduct.setThumbnailImage(imageURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// If no new image is provided, retain the existing thumbnail image
			product.setThumbnailImage(existingProduct.getThumbnailImage());
		}

		// Update other product details
		existingProduct.setName(product.getName());
		existingProduct.setBrand(product.getBrand());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setDiscount(product.getDiscount());
		existingProduct.setPrice(product.getPrice());
		existingProduct.setStock(product.getStock());
		existingProduct.setSubCategory(product.getSubCategory());

		// Save the updated product
		productRepository.save(existingProduct);

		redirectAttributes.addFlashAttribute("success", "Product Update Successful!");
		return "redirect:/product";
	}

	@PostMapping("/product/delete")
	public String deleteProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes,
			Model model) {
		System.out.println("Product Delete id" + product.getId());
		Product existingProduct = productRepository.getReferenceById(product.getId());
		if (existingProduct.getThumbnailImage() != null) {
			try {
				cloudinaryImageService.deleteFile(existingProduct.getThumbnailImage()); // delete img

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		productRepository.deleteById(product.getId());
		redirectAttributes.addFlashAttribute("success", "Product Deleted Successful!");
		return "redirect:/product";
	}
	
	@GetMapping("/product/discount")
	public String Discountproducts(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdTime") String sortBy, Double discount, Model model) {
		Page<Product> productPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			productPage = productRepository.findByDiscountContainingIgnoreCase(query.trim(), pageRequest);
		} else {
			discount = 1.0;
			productPage = productRepository.findByDiscountGreaterThan(discount, pageRequest);
		}
		
		List<Product> products = productPage.getContent();
		model.addAttribute("products", products);
		Product product = new Product();
		model.addAttribute("product", product);
		
	    model.addAttribute("currentPage", productPage.getNumber() + 1);
	    model.addAttribute("totalPages", productPage.getTotalPages());
	    
		return "admin/product/discountProduct";
	}
	@GetMapping("/product/outofstock")
	public String Outofstock(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdTime") String sortBy, Integer stock, Model model) {
		Page<Product> productPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			productPage = productRepository.findByOutofstockContainingIgnoreCase(query.trim(), pageRequest);
		} else {
			stock = 0;
			productPage = productRepository.findByStock(stock, pageRequest);
		}
		
		List<Product> products = productPage.getContent();
		model.addAttribute("products", products);
		Product product = new Product();
		model.addAttribute("product", product);
		
	    model.addAttribute("currentPage", productPage.getNumber() + 1);
	    model.addAttribute("totalPages", productPage.getTotalPages());
	    
		return "admin/product/Outofstock";
	}
	

}
