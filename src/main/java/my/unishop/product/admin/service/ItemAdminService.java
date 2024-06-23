package my.unishop.product.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.product.domain.item.entity.Category;
import my.unishop.product.domain.item.entity.ItemImg;
import my.unishop.product.admin.repository.CategoryRepository;
import my.unishop.product.admin.repository.ItemImgRepository;
import my.unishop.product.domain.item.dto.ItemRequestDto;
import my.unishop.product.domain.item.dto.ItemResponseDto;
import my.unishop.product.domain.item.entity.Item;
import my.unishop.product.domain.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemAdminService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemRepository itemRepository;
    private final FileService fileService;
    private final ItemImgRepository itemImgRepository;
    private final CategoryRepository categoryRepository;

    public ItemResponseDto createItem(ItemRequestDto itemRequestDto) throws IOException {
        Category category = categoryRepository.findById(itemRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. id=" + itemRequestDto.getCategoryId()));

        Item item = new Item(itemRequestDto, category);
        itemRepository.save(item);
        log.info("상품 등록: " + item.getItemName());

        List<ItemImg> itemImgList = new ArrayList<>();
        for (MultipartFile itemImgFile : itemRequestDto.getItemImgFileList()) {
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/" + imgName;

            ItemImg itemImg = new ItemImg();
            itemImg.setImgName(imgName);
            itemImg.setImgUrl(imgUrl);
            itemImg.setOriImgName(oriImgName);
            itemImg.setItem(item);

            itemImgRepository.save(itemImg);
            itemImgList.add(itemImg);
        }

        return new ItemResponseDto(item);
    }

    public ItemResponseDto updateItem(Long itemId, ItemRequestDto itemRequestDto, List<MultipartFile> itemImgFileList, Long categoryId) throws IOException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + itemId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. id=" + categoryId));
        item.updateItem(itemRequestDto, category);

        List<ItemImg> itemImgList = new ArrayList<>();
        for (MultipartFile itemImgFile : itemImgFileList) {
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/" + imgName;

            ItemImg itemImg = new ItemImg();
            itemImg.setImgName(imgName);
            itemImg.setImgUrl(imgUrl);
            itemImg.setOriImgName(oriImgName);
            itemImg.setItem(item);

            itemImgRepository.save(itemImg);
            itemImgList.add(itemImg);
        }
        item.updateItemImgs(itemImgList);
        return new ItemResponseDto(item);
    }

    @Transactional(readOnly = true)
    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + itemId));
        return new ItemResponseDto(item);
    }

    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItems() {
        List<Item> items = itemRepository.findAllWithImagesAndCategory();
        return ItemResponseDto.listFromItems(items);
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> searchItemsByName(String itemName) {
        List<Item> items = itemRepository.findByItemNameContaining(itemName);
        return items.stream().map(ItemResponseDto::new).collect(Collectors.toList());
    }

    public List<ItemResponseDto> searchItemsByCategoryAndItemName(Long categoryId, String itemName) {
        List<Item> items = itemRepository.findByCategoryIdAndItemNameContaining(categoryId, itemName);
        return items.stream().map(ItemResponseDto::new).collect(Collectors.toList());
    }

    public List<ItemResponseDto> getItemsByCategory(Long categoryId) {
        List<Item> items = itemRepository.findByCategoryId(categoryId);
        return items.stream().map(ItemResponseDto::new).collect(Collectors.toList());
    }
}
