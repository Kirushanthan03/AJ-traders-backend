package com.ajtraders.product.controller;

import com.ajtraders.product.dto.InventoryLogDTO;
import com.ajtraders.product.dto.ProductDTO;
import com.ajtraders.product.dto.ResponseDto;
import com.ajtraders.product.service.InventoryLogService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/v1/product/inventory")
public class InventoryLogController {

    private final InventoryLogService inventoryLogService;


    @PostMapping("/add")
    public ResponseDto addProduct(@RequestBody @Valid InventoryLogDTO inventoryLogDTO) {
        return inventoryLogService.addInventoryLog(inventoryLogDTO);
    }
    @GetMapping("/getAll")
    public List<InventoryLogDTO> getInventoryLogs() {
        return inventoryLogService.getInventoryLogs();
    }
    @GetMapping("/getById/{id}")
    public InventoryLogDTO getInventoryLogById(@PathVariable Long id) {
        return inventoryLogService.getInventoryLogById(id);
    }
    @DeleteMapping ("/deleteById/{id}")
    public ResponseDto deleteById(@PathVariable Long id) {
        return inventoryLogService.deleteById(id);
    }
    @PostMapping("/update/{id}")
    public ResponseDto updateProduct(@PathVariable Long id,@RequestBody @Valid InventoryLogDTO inventoryLogDTO) {
        return inventoryLogService.updateInventoryLog(id,inventoryLogDTO);
    }

}
