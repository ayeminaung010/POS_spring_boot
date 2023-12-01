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

import com.example.demo.daos.BrandRepository;
import com.example.demo.model.Brand;

import jakarta.validation.Valid;

@Controller
public class BrandController {
	@Autowired
	private BrandRepository brandRepo;

	@GetMapping("/brand")
	public String viewBrand(@RequestParam(name = "search", required = false) String query,
			 @RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
			 Model model) {
		
		Page<Brand> brandsPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			brandsPage = brandRepo.findByBrandNameContainingIgnoreCase(query.trim(), pageRequest);
	    } else {
	    	brandsPage = brandRepo.findAll(pageRequest);
	    }
		List<Brand> brands = brandsPage.getContent();
		Brand brand = new Brand();
		model.addAttribute("brand", brand);
		model.addAttribute("brands", brands);
		
		model.addAttribute("currentPage", brandsPage.getNumber() + 1);
	    model.addAttribute("totalPages", brandsPage.getTotalPages());
	    
		return "admin/brand/index";
	}

	@GetMapping("/brand/add")
	public String addBrand(Model model) {
		Brand brand = new Brand();
		model.addAttribute("brand", brand);
		return "admin/brand/add";
	}

	@PostMapping("/brand/add")
	public String addBrand(@ModelAttribute("brand") @Valid Brand brand, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "admin/brand/add";
		}
		Brand existingBrand = brandRepo.findByBrandName(brand.getBrandName());
		if (existingBrand != null) {
			bindingResult.rejectValue("brandName", "error.brand", "Brand with this name already exists");
			return "admin/brand/add";
		}
		brandRepo.save(brand);
		redirectAttributes.addFlashAttribute("success", "Brand Add successful!!");
		return "redirect:/brand";
	}
	
	@PostMapping("/brand/delete")
	public String deleteCategory(@ModelAttribute("brand") Brand brand, Model model, RedirectAttributes redirectAttributes) {

		Brand ExistingBrand = brandRepo.findById(brand.getBrandId()).orElse(null);

		System.out.println("Delete category: " + brand.getBrandId());
		if (ExistingBrand != null) {
			if (ExistingBrand.getProducts().isEmpty()) {
				brandRepo.deleteById(brand.getBrandId());
				redirectAttributes.addFlashAttribute("success", "Brand deleted successfully!");
			} else {
				redirectAttributes.addFlashAttribute("error", "Cannot delete brand with attached Products");
			}
		} else {
			redirectAttributes.addFlashAttribute("error", "Brand not found");
		}

		return "redirect:/brand";
	}

	@GetMapping("/brand/update/{id}")
	public String updateBrand(@PathVariable("id") Integer id, Model model) {

		Brand brand = brandRepo.getReferenceById(id);
		model.addAttribute("brand", brand);
		return "admin/brand/update";
	}

	@PostMapping("/brand/update/{id}")
	public String updateBrand(@ModelAttribute("brand") @Valid Brand brand, BindingResult bindingResult ,@PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			return "admin/brand/update";
		}
		Brand existingBrand = brandRepo.findByBrandName(brand.getBrandName());
		
		if (existingBrand != null) {
			if (id != existingBrand.getBrandId()) {
				bindingResult.rejectValue("brandName", "error.brand", "brand with this name already exists");
				return "admin/brand/update";
			}
		}

		Brand brandById = brandRepo.getReferenceById(id);
		brandById.setBrandName(brand.getBrandName());
		
		brandRepo.save(brandById);
		redirectAttributes.addFlashAttribute("success", "brand Update successful!!");
		return "redirect:/brand";
	}
}
