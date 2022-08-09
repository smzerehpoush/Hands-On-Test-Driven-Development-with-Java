package com.tdd.supply.controller;

import com.tdd.supply.model.SaleRecord;
import com.tdd.supply.model.SupplyRecord;
import com.tdd.supply.service.SupplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SupplyController {
    private final SupplyService supplyService;

    /**
     * Retrieves supply record for a given id
     *
     * @param id of the supply to search for
     * @return ResponseEntity with found Supply
     */
    @GetMapping("/supply/{id}")
    public ResponseEntity<SupplyRecord> getSupplyRecord(@PathVariable Integer id) {
        logger.info("Finding supply record for product with id:" + id);
        Optional<SupplyRecord> supplyRecordOptional = supplyService.getSupplyRecord(id);
        if (supplyRecordOptional.isPresent()) {
            SupplyRecord supplyRecord = supplyRecordOptional.get();
            try {
                return ResponseEntity
                        .ok()
                        .location(new URI("/supply/" + supplyRecord.getProductId()))
                        .body(supplyRecord);
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new sale record from a purchase
     *
     * @param saleRecord to be saved
     * @return ResponseEntity with the sale record created
     */
    @PostMapping("/supply/saleRecord")
    public ResponseEntity<SupplyRecord> newSaleRecord(@RequestBody SaleRecord saleRecord) {
        logger.info("Creating new sale record");
        Optional<SupplyRecord> optionalSupplyRecord = supplyService.purchaseProduct(saleRecord.getProductId(), saleRecord.getQuantity());
        if (optionalSupplyRecord.isPresent()) {
            SupplyRecord supplyRecord = optionalSupplyRecord.get();
            try {
                return ResponseEntity
                        .created(new URI("/supply/" + supplyRecord.getProductId()))
                        .body(supplyRecord);
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
