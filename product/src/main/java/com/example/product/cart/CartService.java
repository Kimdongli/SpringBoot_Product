package com.example.product.cart;

import com.example.product.core.error.exception.Exception400;
import com.example.product.core.error.exception.Exception404;
import com.example.product.core.error.exception.Exception500;
import com.example.product.option.Option;
import com.example.product.option.OptionRepository;
import com.example.product.user.User;
import com.example.product.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;

    public CartResponse.FindAllDTO findAll() {
        // Cart 모든 항목 조회
        List<Cart> cartList = cartRepository.findAll();

        // 조회한것들을 DTO로 변환하여 반환
        return new CartResponse.FindAllDTO(cartList);
    }

    // ** 카트에 상품 추가
    @Transactional
    public void addCartList(List<CartRequest.SaveDTO> saveDTOS, User user) {

        // ** 동일한 상품 예외 처리
        Set<Long> optionsId = new HashSet<>();

        // 요청받은 각 상품에 대해
        for(CartRequest.SaveDTO cart: saveDTOS){
            if(cart.getOptionId() == null) {
                throw new Exception400("optionId가 null입니다.");
            }
            // 동일한 상품이 있을경우 예외 발생
            if(!optionsId.add(cart.getOptionId()))

                throw new Exception400("이미 동일한 상품 옵션이 있습니다." + cart.getOptionId());
        }

        userRepository.save(user);


        // ** 상품 존재 유무
        List<Cart> cartList = saveDTOS.stream().map(cartDTO->{
            // 상품 유무 확인
            Option option = optionRepository.findById(cartDTO.getOptionId()).orElseThrow(
                    () -> new Exception404("해당 상품 옵션을 찾을 수 없습니다." + cartDTO.getOptionId())
            );

            // cart에 상품 추가
            return cartDTO.toEntity(option, user);
        }).collect(Collectors.toList());

        // ** 상품 DB 오류
        cartList.forEach(cart -> {

            try {
                // DB에 장바구니 항목 저장
                cartRepository.save(cart);
            }catch (Exception e){
                // 에러 발생 시 예외 처리
                throw new Exception500("카트에 담던 중 오류가 발생했습니다."+e.getMessage());
            }
        });
    }

    // Cart 항목을 업데이트 메소드
    @Transactional
    public CartResponse.UpdateDTO update(List<CartRequest.UpdateDTO> requestDTO, User user) {
        // user Cart 항목 조회
        List<Cart> cartList = cartRepository.findAllByUserId(user.getId());

        // 장바구니 항목의 ID 리스트 생성
        List<Long> cartIds = cartList.stream().map(cart -> cart.getId()).collect(Collectors.toList());
        // 요청으로 받은 장바구니 항목의 ID 리스트 생성
        List<Long> requestIds = requestDTO.stream().map(dto -> dto.getCartId()).collect(Collectors.toList());

        // 장바구니 항목이 없는 경우 예외 처리
        if(cartIds.size() == 0){
            throw new Exception404("주문 가능한 상품이 없습니다.");
        }

        // 동일한 장바구니 항목 ID가 요청으로 들어온 경우 예외 처리
        if(requestIds.stream().distinct().count() != requestIds.size()){
            throw new Exception400("동일한 카트 아이디를 주문할 수 없습니다.");
        }
        // 요청으로 받은 장바구니 항목 ID가 실제 장바구니 항목에 없는 경우 예외 처리
        for (Long requestid : requestIds) {
            if(!cartIds.contains(requestIds)){

                throw new Exception400("카트에 없는 상품은 주문할 수 없습니다." + requestid);
            }
        }

        // 요청으로 받은 각 장바구니 항목에 대해
        for (CartRequest.UpdateDTO updateDTO : requestDTO){
            for (Cart cart : cartList){
                // 장바구니 항목의 ID가 일치하는 경우 업데이트
                if(cart.getId() == updateDTO.getCartId()){
                    cart.update(updateDTO.getQuantity(), cart.getPrice() * cart.getQuantity());
                }
            }
        }
        // 업데이트한 장바구니 항목을 DTO로 변환하여 반환
        return new CartResponse.UpdateDTO(cartList);

    }

}
