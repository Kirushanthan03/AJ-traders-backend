package com.ajtraders.product.service;

import com.ajtraders.product.dto.InventoryLogDTO;
import com.ajtraders.product.dto.ResponseDto;
import com.ajtraders.product.entity.InventoryLog;
import com.ajtraders.product.exception.ServiceException;
import com.ajtraders.product.repository.InventoryLogRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class InventoryLogService {
    private final InventoryLogRepository inventoryLogRepository;

    public ResponseDto addInventoryLog(InventoryLogDTO inventoryLogDTO) {
        InventoryLog inventoryLog = new InventoryLog();
        BeanUtils.copyProperties(inventoryLogDTO, inventoryLog);
        inventoryLogRepository.save(inventoryLog);
        return new ResponseDto("Inventory Log created successfully");
    }

    public List<InventoryLogDTO> getInventoryLogs() {
        List<InventoryLog> inventoryLogs = inventoryLogRepository.findAll();
        List<InventoryLogDTO> inventoryLogDTOS = new ArrayList<>();
        for (InventoryLog inventoryLog : inventoryLogs) {
            InventoryLogDTO inventoryLogDTO = new InventoryLogDTO();
            BeanUtils.copyProperties(inventoryLog, inventoryLogDTO);
            inventoryLogDTOS.add(inventoryLogDTO);
        }

        return inventoryLogDTOS;
    }

    public InventoryLogDTO getInventoryLogById(Long id) {
        InventoryLog inventoryLog = inventoryLogRepository.findById(id).orElseThrow(() -> new ServiceException("The requested InventoryLog is not available", "Not Found", HttpStatus.NOT_FOUND));
        InventoryLogDTO inventoryLogDTO = new InventoryLogDTO();
        BeanUtils.copyProperties(inventoryLog, inventoryLogDTO);
        return inventoryLogDTO;
    }

    public ResponseDto deleteById(Long id) {
        inventoryLogRepository.findById(id).orElseThrow(() -> new ServiceException("The requested InventoryLog is not available", "Not Found", HttpStatus.NOT_FOUND));
        inventoryLogRepository.deleteById(id);
        return new ResponseDto("The InventoryLog has been deleted successfully");
    }

    public ResponseDto updateInventoryLog(Long id, InventoryLogDTO inventoryLogDTO) {
        InventoryLog inventoryLog = inventoryLogRepository.findById(id).orElseThrow(() -> new ServiceException("The requested InventoryLog is not available", "Not Found", HttpStatus.NOT_FOUND));
        BeanUtils.copyProperties(inventoryLogDTO, inventoryLog);
        inventoryLogRepository.save(inventoryLog);
        return new ResponseDto("The InventoryLog has been updated successfully");
    }
}
