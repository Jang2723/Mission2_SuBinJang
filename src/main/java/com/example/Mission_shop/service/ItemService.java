package com.example.Mission_shop.service;

import com.example.Mission_shop.dto.ItemDto;
import com.example.Mission_shop.entity.Item;
import com.example.Mission_shop.repo.ItemRepository;
import com.example.Mission_shop.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public String registerItem(ItemDto itemDto, String username) {
        // 아이템 등록 처리
        Item item = new Item();
        item.setTitle(itemDto.getTitle());
        item.setDescription(itemDto.getDescription());
        item.setMinimumPrice(itemDto.getMinimumPrice());
        item.setStatus("판매중"); // 처음 등록할 때 "판매중" 상태로 설정
        item.setUser(userRepository.findIdByUsername(username).orElse(null)); // 현재 사용자의 아이디 설정

        // 아이템 저장
        itemRepository.save(item);

        return "Item registered successfully";
    }

    public List<ItemDto> itemAllList() {
        // 모든 물품 정보를 가져와서 리스트로 저장
        List<Item> itemList = itemRepository.findAll();

        // 각 물품을 ItemDto로 변환하여 저장할 리스트 생성
        List<ItemDto> itemDtoList = new ArrayList<>();

        // 각 물품을 ItemDto로 변환하여 리스트에 추가
        for (Item item : itemList) {
            itemDtoList.add(ItemDto.fromEntity(item));
        }

        // 변환된 ItemDto 리스트 반환
        return itemDtoList;
    }

    public String updateItem(ItemDto itemDto, String username) {
        // 아이템 내용 수정
        // 아이템 엔티티를 가져옴
        Item item = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemDto.getId()));

        // 현재 사용자가 해당 아이템의 소유자인지 확인
        if (!item.getUser().getUsername().equals(username)) {
            return "You are not authorized to update this item.";
        }

        // 아이템 정보 수정
        item.setTitle(itemDto.getTitle());
        item.setDescription(itemDto.getDescription());
        item.setMinimumPrice(itemDto.getMinimumPrice());

        // 아이템 저장
        itemRepository.save(item);

        return "Item updated successfully";
    }

    public String deleteItem(ItemDto itemDto, String username) {
        // 아이템 삭제
        // 아이템 엔티티를 가져옴
        Optional<Item> optionalItem = itemRepository.findById(itemDto.getId());
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            // 현재 사용자가 해당 아이템의 소유자인지 확인
            if (item.getUser().getUsername().equals(username)) {
                // 아이템 정보 삭제
                itemRepository.delete(item);
                return "Item deleted successfully";
            } else {
                return "You are not authorized to delete this item.";
            }
        } else {
            return "Item not found with id: " + itemDto.getId();
        }
    }
}
