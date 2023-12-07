package com.example.product.option;

import com.example.product.core.utils.ApiUtils;
import com.example.product.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OptionController {
    private final OptionService optionService;


    @GetMapping("/options/create")
    public ModelAndView createOption(){return new ModelAndView("createOption");}
    /**
     * @param id
     * id 에 관련된 설명 (ProductId)
     * @return
     * 반환값에 관련된 설명
     * List &lt;OptionResponse.FindByProductIdDTD&gt; 반환값에 관련된 설명
     * */
    // 개인 출력
    @GetMapping("/products/{id}/options")
    public ResponseEntity<?> findById(@PathVariable Long id){
        List<OptionResponse.FindByProductIdDTD> optionResponses = optionService.findByProductId(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }

    // 전체 출력
    @GetMapping("/options")
    public ResponseEntity<?> findAll(){

        List<OptionResponse.FindAllDTO> optionResponses =
                optionService.findAll();

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/options/save")
    public ResponseEntity<?> OptionSave(@RequestBody OptionResponse.CreateDTO createDTO){
        List<OptionResponse.CreateDTO> optionResponses = optionService.createOption(createDTO);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }
}
