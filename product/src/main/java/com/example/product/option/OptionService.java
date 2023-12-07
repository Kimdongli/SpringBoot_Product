package com.example.product.option;

import com.example.product.product.Product;
import com.example.product.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;


    public List<OptionResponse.FindByProductIdDTD> findByProductId(Long id){
        List<Option> optionList = optionRepository.findByProductId(id);
        List<OptionResponse.FindByProductIdDTD> optionResponses =
                optionList.stream().map(OptionResponse.FindByProductIdDTD::new)
                        .collect(Collectors.toList());
        return optionResponses;
    }

    public List<OptionResponse.FindAllDTO> findAll() {
        List<Option> optionList = optionRepository.findAll();

        List<OptionResponse.FindAllDTO> findAllDTOS =
                optionList.stream().map(OptionResponse.FindAllDTO::new)
                        .collect(Collectors.toList());

        return findAllDTOS;
    }


    @Transactional
    public List<OptionResponse.CreateDTO> createOption(OptionResponse.CreateDTO createDTO) {
        Option option = createDTO.toEntity(this.productRepository);
        Option savedOption = optionRepository.save(option);

       List<OptionResponse.CreateDTO> result = new ArrayList<>();
       result.add(new OptionResponse.CreateDTO(savedOption));
       return result;
    }
}
