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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.SubCategoryRepository;
import com.example.demo.model.SubCategory;

import jakarta.validation.Valid;

@Controller
public class SubCategoryController {
	@Autowired
	private SubCategoryRepository subcategoryRepo;

	@GetMapping("/subcategory")
	public String viewSubCategory(@RequestParam(name = "search", required = false) String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdTime") String sortBy,
			Model model) {
		Page<SubCategory> subcategoriesPage;

		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			subcategoriesPage = subcategoryRepo.findBySubCategoryNameContainingIgnoreCase(query.trim(),pageRequest);
	    } else {
	    	subcategoriesPage =  subcategoryRepo.findAll(pageRequest);
	    }
		List<SubCategory> subcategories = subcategoriesPage.getContent();
		SubCategory subcategory = new SubCategory();
		model.addAttribute("subcategory", subcategory);
		model.addAttribute("subcategories", subcategories);
		
		model.addAttribute("currentPage", subcategoriesPage.getNumber() + 1);
	    model.addAttribute("totalPages", subcategoriesPage.getTotalPages());
		return "admin/subCategory/index";
	}

	@GetMapping("/subcategory/add")
	public String addSubCategory(Model model) {
		SubCategory subcategory = new SubCategory();
		model.addAttribute("subcategory", subcategory);
		return "admin/subCategory/add";
	}

	@PostMapping("/subcategory/add")
	public String addSubCategory(@ModelAttribute("subcategory") @Valid SubCategory subcategory, BindingResult bindingResult, 
			Model model,RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
		
			return "admin/subCategory/add";
		}
		SubCategory existingsubcategory = subcategoryRepo.findBySubCategoryName(subcategory.getSubCategoryName());
		if (existingsubcategory != null) {
			bindingResult.rejectValue("subCategoryName", "error.subcategory", "SubCategory with this name already exists");
			return "admin/subCategory/add";
		}
		subcategoryRepo.save(subcategory);
		redirectAttributes.addFlashAttribute("success", "SubCategory Add successful!!");
		return "redirect:/subcategory";
	}
	@PostMapping("/subcategory/delete")
	public String deleteSubCategory(@ModelAttribute("subcategory") SubCategory subcategory, Model model, RedirectAttributes redirectAttributes) {

		SubCategory ExistingSubCategory = subcategoryRepo.findById(subcategory.getSubCategoryId()).orElse(null);

		System.out.println("Delete subcategory: " + subcategory.getSubCategoryId());
		
		if (ExistingSubCategory != null) {
			if (ExistingSubCategory.getProducts().isEmpty()) {
				subcategoryRepo.deleteById(subcategory.getSubCategoryId());
				redirectAttributes.addFlashAttribute("success", "SubCategory deleted successfully!");
			} else {
				redirectAttributes.addFlashAttribute("error", "Cannot delete Subcategory with attached Products");
			}
		} else {
			redirectAttributes.addFlashAttribute("error", "SubCategory not found");
		}

		return "redirect:/subcategory";
	}
	
	
	@GetMapping("/subcategory/update/{id}")
	public String updateSubCategory(@PathVariable("id") Integer id, Model model) {

		SubCategory subcategory = subcategoryRepo.getReferenceById(id);
		model.addAttribute("subcategory", subcategory);

		return "admin/subCategory/update";
	}

	@PostMapping("/subcategory/update/{id}")
	public String updateSubCategory(@ModelAttribute("subcategory") @Valid SubCategory subcategory, BindingResult bindingResult, @PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			return "admin/subCategory/update";
		}
		SubCategory existingsubcategory = subcategoryRepo.findBySubCategoryName(subcategory.getSubCategoryName());
		
		if (existingsubcategory != null) {
			if (id != existingsubcategory.getSubCategoryId()) {
				bindingResult.rejectValue("subCategoryName", "error.subcategory", "SubCategory with this name already exists");
				return "admin/subCategory/update";
			}
		}
		
		SubCategory subcategoryById = subcategoryRepo.getReferenceById(id);
		subcategoryById.setSubCategoryName(subcategory.getSubCategoryName());
		subcategoryById.setCategory(subcategory.getCategory());
		subcategoryRepo.save(subcategoryById);
		redirectAttributes.addFlashAttribute("success", "SubCategory Update successful!!");
		return "redirect:/subcategory";
	}

}
