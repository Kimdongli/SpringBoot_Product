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
    // 특정 상품 ID에 해당하는 옵션조회 메소드
    @GetMapping("/products/{id}/options")
    public ResponseEntity<?> findById(@PathVariable Long id){
        // 서비스로부터 상품 ID에 해당하는 옵션들을 조회
        List<OptionResponse.FindByProductIdDTD> optionResponses = optionService.findByProductId(id);
        // 조회한 결과를 API 응답 형태로 변환
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }

    // 모든 옵션을 조회하는 메소드
    @GetMapping("/options")
    public ResponseEntity<?> findAll(){
        // 서비스로부터 모든 옵션을 조회
        List<OptionResponse.FindAllDTO> optionResponses =
                optionService.findAll();
        // 조회한 결과를 API 응답 형태로 변환
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }

    // 옵션 생성 메소드
    @PostMapping("/options/save")
    public ResponseEntity<?> OptionSave(@RequestBody OptionResponse.CreateDTO createDTO){
        // 서비스로부터 옵션을 저장하고 결과를 받음
        List<OptionResponse.CreateDTO> optionResponses = optionService.createOption(createDTO);
        // 조회한 결과를 API 응답 형태로 변환
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }
}
