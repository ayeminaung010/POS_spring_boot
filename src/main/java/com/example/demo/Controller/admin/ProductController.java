package com.example.demo.Controller.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.validation.Valid;

@Controller
public class ProductController {
	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/product")
	public String products(Model model) {
		List<Product> products = productRepository.findAll();
		model.addAttribute("products", products);
		Product product = new Product();
		model.addAttribute("product", product);
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
		String imageName = imagesFile.getOriginalFilename();
		product.setThumbnailImage(imageName);
		Product savedImage = productRepository.save(product);
		try {

			String uploadDir = "src/main/resources/static/uploads/products/" + savedImage.getId();
			Path uploadPath = (Path) Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			Path fileToCreatePath = uploadPath.resolve(imageName);

			Files.copy(imagesFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {

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
	public String productUpdate(@PathVariable("id") Integer id, @RequestParam("thumbImages") MultipartFile imagesFile,
			@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (bindingResult.hasErrors()) {
			return "/admin/product/update";
		}
		Product existingProduct = productRepository.getReferenceById(id);
		if (imagesFile != null) {
			try {
				String photoPath = "src/main/resources/static/uploads/products/" + existingProduct.getId() + "/"
						+ existingProduct.getThumbnailImage();
				// Create a File object with the photo path
				File photoFile = new File(photoPath);

				// Delete the file associated with the photo path
				if (photoFile.exists()) {
					photoFile.delete();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		String imageName = imagesFile.getOriginalFilename();
		existingProduct.setName(product.getName());
		existingProduct.setBrand(product.getBrand());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setDiscount(product.getDiscount());
		existingProduct.setPrice(product.getPrice());
		existingProduct.setStock(product.getStock());
		existingProduct.setSubCategory(product.getSubCategory());
		existingProduct.setThumbnailImage(imageName);

		Product savedImage = productRepository.save(existingProduct);
		try {

			String uploadDir = "src/main/resources/static/uploads/products/" + savedImage.getId();
			Path uploadPath = (Path) Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			Path fileToCreatePath = uploadPath.resolve(imageName);

			Files.copy(imagesFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {

			e.printStackTrace();
		}
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
				String photoPath = "src/main/resources/static/uploads/products/" + existingProduct.getId() + "/"
						+ existingProduct.getThumbnailImage();
				// Create a File object with the photo path
				File photoFile = new File(photoPath);

				// Delete the file associated with the photo path
				if (photoFile.exists()) {
					photoFile.delete();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		productRepository.deleteById(product.getId());
		redirectAttributes.addFlashAttribute("success", "Product Deleted Successful!");
		return "redirect:/product";
	}
}
