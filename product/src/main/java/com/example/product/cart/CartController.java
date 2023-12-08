package com.example.product.cart;

import com.example.product.core.security.CustomUserDetails;
import com.example.product.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;


    // ** 카트 전체 상품 확인.
    // ** 인증 받지 않으면 즉시 401 에러가 뜬다.(CustomUserDetails)
    @GetMapping("/")
    // ** 인증된 사용자 세부 정보 가져온다.
    public ResponseEntity<?> carts(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        // 모든 정보를 받아온다.
        CartResponse.FindAllDTO findAllDTO = cartService.findAll();
        // API 호출이 성공했음
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(findAllDTO);
        // HTTP 상태 코드 200(ok)와 함께 apiResult를 HTTP 응답 생성.
        return ResponseEntity.ok(apiResult);
    }

    // ** 카트에 상품 추가.
    @PostMapping("/add")
    public ResponseEntity<?> addCartList(
            // ** HTTP 요청 본문을 List<CartRequest.SaveDTO> 타입의 객체로 변환
            // ** @Valid 객체에 대해 유효성 검사 수행
            @RequestBody @Valid List<CartRequest.SaveDTO> requestDTO,
            // ** 인증된 사용자 세부 정보 가져온다.
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Error error) {
        // ** 유저 정보 받아온다.
        cartService.addCartList(requestDTO, customUserDetails.getUser());
        // API 호출이 성공했음
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        // HTTP 상태 코드 200(ok)와 함께 apiResult를 HTTP 응답 생성.
        return ResponseEntity.ok(apiResult);
    }


    // ** 카트 업데이트
    @GetMapping("/update")
    public ResponseEntity<?> update(
          // ** HTTP 요청 본문을 List<CartRequest.UpdateDTO> 타입의 객체로 변환
          // ** @Valid 객체에 대해 유효성 검사 수행
          @RequestBody @Valid List<CartRequest.UpdateDTO> requestDTO,
          // ** 인증된 사용자 세부 정보 가져온다.
          @AuthenticationPrincipal CustomUserDetails customUserDetails,
          Error error) {
        // ** 서비스를 호출하여 카트 정보 업데이트
        CartResponse.UpdateDTO updateDTO = cartService.update(requestDTO, customUserDetails.getUser());

        // API 호출이 성공했음.
        // 업데이트된 장바구니의 정보가 포함됨.
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(updateDTO);
        // HTTP 상태 코드 200(ok)와 함께 apiResult를 HTTP 응답 생성.
        return ResponseEntity.ok(apiResult);
    }
}
