package com.example.demo.Controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.CategoryRepository;
import com.example.demo.model.Category;

import jakarta.validation.Valid;

@Controller

public class CategoryController {
	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/category")
	public String viewCategory(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		Category category = new Category();
		model.addAttribute("category", category);
		return "admin/category/index";
	}

	@GetMapping("/category/add")
	public String addCategory(Model model) {
		Category category = new Category();
		model.addAttribute("category", category);
		return "admin/category/add";
	}

	@PostMapping("/category/add")
	public String addCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "admin/category/add";
		}
		Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
		if (existingCategory != null) {
			bindingResult.rejectValue("categoryName", "error.category", "Category with this name already exists");
			return "admin/category/add";
		}
		categoryRepository.save(category);
		redirectAttributes.addFlashAttribute("success", "Category Add successful!!");
		return "redirect:/category";
	}

	@GetMapping("/category/update/{id}")
	public String updateCategory(@PathVariable("id") Integer id, Model model) {

		Category category = categoryRepository.getReferenceById(id);
		model.addAttribute("category", category);
		return "admin/category/update";
	}

	@PostMapping("/category/update/{id}")
	public String editCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult,
			@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "admin/category/update";
		}
		Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());

		if (existingCategory != null) {
			if (id != existingCategory.getCategoryId()) {
				bindingResult.rejectValue("categoryName", "error.category", "Category with this name already exists");
				return "admin/category/update";
			}
		}
		Category categoryById = categoryRepository.getReferenceById(id);
		categoryById.setCategoryName(category.getCategoryName());

		categoryRepository.save(categoryById);
		redirectAttributes.addFlashAttribute("success", "Category Update successful!!");

		return "redirect:/category";
	}
	
	@PostMapping("/category/delete")
	public String deleteCategory(@ModelAttribute("category") Category category, Model model, RedirectAttributes redirectAttributes) {

		Category ExistingCategory = categoryRepository.findById(category.getCategoryId()).orElse(null);
		if (ExistingCategory != null) {
			if (ExistingCategory.getSubCategories().isEmpty()) {
				categoryRepository.deleteById(category.getCategoryId());
				redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
			} else {
				redirectAttributes.addFlashAttribute("error", "Cannot delete category with attached SubCategories");
			}
		} else {
			redirectAttributes.addFlashAttribute("error", "Category not found");
		}

		return "redirect:/category";
	}

}
