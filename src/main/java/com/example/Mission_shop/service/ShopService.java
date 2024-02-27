package com.example.Mission_shop.service;

import com.example.Mission_shop.dto.ShopDto;
import com.example.Mission_shop.entity.Shop;
import com.example.Mission_shop.repo.ShopRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public String updateShop(ShopDto shopDto, String username) {
        // username을 사용하여 사용자의 쇼핑몰을 찾음
        Optional<Shop> optionalShop = shopRepository.findByUserUsername(username);

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            // 쇼핑몰 정보 수정
            shop.setName(shopDto.getName());
            shop.setIntroduction(shopDto.getIntroduction());
            shop.setCategory(shopDto.getCategory());

            // 저장
            shopRepository.save(shop);

            return "Shop updated successfully";
        } else {
            return "Shop not found for username: " + username;
        }
    }

    public String shopApplyOpen(String username) {
        // username을 사용하여 사용자의 쇼핑몰을 찾음
        Optional<Shop> optionalShop = shopRepository.findByUserUsername(username);

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            // name, introduction, category가 모두 null이 아니고 비어있지 않은 경우에만 개설 신청 상태로 변경
            if (shop.getName() != null && !shop.getName().isEmpty()
                    && shop.getIntroduction() != null && !shop.getIntroduction().isEmpty()
                    && shop.getCategory() != null && !shop.getCategory().isEmpty()) {

                shop.setStatus("개설 신청");
                shopRepository.save(shop);
                return "쇼핑몰 개설 신청이 완료되었습니다.";
            } else {
                return "Name, introduction, category should not be null or empty.";
            }
        } else {
            return "Shop not found for username: " + username;
        }
    }

    // 관리자, 개설 신청 쇼핑몰 사용자 - 개설신청 쇼핑몰 목록 읽기
    public List<ShopDto> applyRead(String username) {
        // 쇼핑몰 개설 신청 목록 가져와서 리스트로 저장
        List<Shop> shopList;
        String status1 = "개설 신청";
        String status2 = "오픈";

        // 만약 사용자가 관리자이면 모든 개설 신청 목록을 반환
        if ("admin".equals(username)) {
            shopList = shopRepository.findByStatus(status1);
        } else {
            // 관리자가 아닌 경우 해당 사용자의 쇼핑몰 개설 신청 및 오픈 목록을 반환
            shopList = shopRepository.findByUserUsernameAndStatusIn(username, Arrays.asList(status1, status2));
        }

        // 각 쇼핑몰을 ShopDto로 변환하여 저장할 리스트 생성
        List<ShopDto> shopDtoList = new ArrayList<>();

        // 각 쇼핑몰을 ShopDto로 변환하여 리스트에 추가
        for (Shop shop : shopList) {
            shopDtoList.add(ShopDto.fromEntity(shop));
        }

        return shopDtoList;
    }

    public String acceptRefuse(ShopDto shopDto) {
        // shop name으로 쇼핑몰을 찾음
        Optional<Shop> optionalShop = shopRepository.findByName(shopDto.getName());

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            // status가 개설 신청일 경우 허가 또는 불허
            if (shop.getStatus().equals("개설 신청")) {
                // 허가
                if (shopDto.getOpenAcceptRefuse().equals("허가")) {
                    shop.setOpenAcceptRefuse(shopDto.getOpenAcceptRefuse());
                    shop.setRefuseReason(null);
                    shop.setStatus("오픈");
                }
                // 불허, 불허일 경우 이유도 작성
                else if (shopDto.getOpenAcceptRefuse().equals("불허")) {
                    shop.setOpenAcceptRefuse(shopDto.getOpenAcceptRefuse());
                    shop.setRefuseReason(shopDto.getRefuseReason());
                    shopRepository.save(shop);
                    return "쇼핑몰 불허 완료";
                }
            }
            shopRepository.save(shop);
            return "쇼핑몰 허가 완료";
        } else {
            return "Shop not found for shop name: " + shopDto.getName();
        }
    }

    public String shopApplyClose(String username, ShopDto shopDto) {
        // shop name으로 쇼핑몰을 찾음
        Optional<Shop> optionalShop = shopRepository.findByUserUsername(username);

        // shop이 존재한다면 폐쇄요청 신청
        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            // 폐쇄 요청 사유 없으면 실패
            if (shopDto.getClosureReason() == null || shopDto.getClosureReason().isEmpty()) {
                return "폐쇄 요청 실패. 폐쇄 사유를 작성해야 합니다.";
            }

            shop.setClosureRequest("폐쇄 요청");
            shop.setClosureReason(shopDto.getClosureReason());
            shopRepository.save(shop);
            return "폐쇄 요청 완료";
        } else {
            return "폐쇄 요청 실패." + username+"의 쇼핑몰을 찾을 수 없습니다.";
        }
    }

    // 관리자 - 폐쇄 요청 확인
    public List<ShopDto> applyCloseRead() {
        String closureRequest = "폐쇄 요청";

        // 폐쇄 요청 상태인 쇼핑몰 목록 조회
        List<Shop> shopList = shopRepository.findByClosureRequest(closureRequest);

        // 각 쇼핑몰을 ShopDto로 변환하여 저장할 리스트 생성
        List<ShopDto> shopDtoList = new ArrayList<>();

        // 각 쇼핑몰을 ShopDto로 변환하여 리스트에 추가
        for (Shop shop : shopList) {
            shopDtoList.add(ShopDto.fromEntity(shop));
        }

        return shopDtoList;
    }

    public String closeAcceptRefuse(ShopDto shopDto){
        // name으로 shop 찾기
        Optional<Shop> optionalShop = shopRepository.findByName(shopDto.getName());

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            // shop의 closureStatus가 "폐쇄 요청"이라면
            if ("폐쇄 요청".equals(shop.getClosureRequest())) {
                // shop의 closureStatus를 "폐쇄"로 변경
                shop.setClosureRequest("수락");
                shop.setOpenAcceptRefuse("거절");
                shop.setRefuseReason("쇼핑몰의 폐쇄 요청");
                shop.setStatus("폐쇄");
                shopRepository.save(shop);
                return shopDto.getName() + " 스토어의 폐쇄 요청을 수락했습니다.";
            } else {
                return shopDto.getName() + " 스토어의 폐쇄 요청이 아닙니다.";
            }
        } else {
            return "존재하지 않는 스토어입니다.";
        }
    }
}