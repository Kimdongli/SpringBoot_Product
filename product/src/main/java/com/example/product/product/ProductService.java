package com.example.product.product;

import com.example.product.core.error.exception.Exception404;
import com.example.product.option.Option;
import com.example.product.option.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    // ** 전체 상품 확인
    public List<ProductResponse.FindAllDTO> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse.FindAllDTO> productDTOS =
                productPage.getContent().stream().map(ProductResponse.FindAllDTO::new)
                .collect(Collectors.toList());

        return  productDTOS;
    }

    // ** 개인 상품 확인
    public ProductResponse.FindByIdDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 상품을 찾을 수 없습니다. : "+id));
        // ** product.getId() 로 Option 상품 검색.
        List<Option> optionList = optionRepository.findByProductId(product.getId());

        // ** 검색이 완료된 제품 반환.
        return new ProductResponse.FindByIdDTO(product, optionList);
    }

    @Transactional
    public ProductResponse.FindByIdDTO createProduct(ProductResponse.CreateDTO createDTO){
        Product product = new Product();
        product.setProductName(createDTO.getProductName());
        product.setDescription(createDTO.getDescription());
        product.setImage(createDTO.getImage());
        product.setPrice(createDTO.getPrice());

        productRepository.save(product);

        return new ProductResponse.FindByIdDTO(product,new ArrayList<>());
    }

    @Transactional
    public Long deleteProduct(Long id){
        productRepository.deleteById(id);
        return id;
    }

    @Transactional
    public ProductResponse.FindByIdDTO updateProduct(Long id, ProductResponse.CreateDTO updateDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Product with ID: " + id + " not found"));

        product.setProductName(updateDTO.getProductName());
        product.setDescription(updateDTO.getDescription());
        product.setImage(updateDTO.getImage());
        product.setPrice(updateDTO.getPrice());

        product = productRepository.save(product);

        return new ProductResponse.FindByIdDTO(product, product.getOptions());
    }
}
