package com.example.product.product;

import com.example.product.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/create")
    public String create(){return "create";}

    // ** 전체 상품 확인
    @GetMapping("/")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page ){
        List<ProductResponse.FindAllDTO> productDTOS = productService.findAll(page);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }

    // ** 개인 상품 확인
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        ProductResponse.FindByIdDTO productDTOS = productService.findById(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/save")
    public ResponseEntity<?> createProduct(@RequestBody ProductResponse.CreateDTO createDTO){
        ProductResponse.FindByIdDTO createdProduct = productService.createProduct(createDTO);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(createdProduct);
        return ResponseEntity.ok(apiResult);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success("Product with ID: " + id + " has been successfully deleted");
        return ResponseEntity.ok(apiResult);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,@RequestBody ProductResponse.CreateDTO updateDTO){
        ProductResponse.FindByIdDTO updatedProduct = productService.updateProduct(id, updateDTO);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(updatedProduct);
        return ResponseEntity.ok(apiResult);
    }

}