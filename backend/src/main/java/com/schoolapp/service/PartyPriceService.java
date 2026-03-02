package com.schoolapp.service;

import org.springframework.stereotype.Service;

import com.schoolapp.dao.PartyPriceDto;
import com.schoolapp.entity.PartyPrice;
import com.schoolapp.entity.Products;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.repository.PartyPriceRepository;
import com.schoolapp.repository.ProductRepository;
import com.schoolapp.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartyPriceService {

    private final PartyPriceRepository repo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public PartyPriceService(
            PartyPriceRepository repo,
            UserRepository userRepo,
            ProductRepository productRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    // CREATE / UPDATE
    public PartyPriceDto save(PartyPriceDto dto) {

        // ❌ prevent duplicate
        repo.findByParty_IdAndProduct_Id(dto.getPartyId(), dto.getProductId())
                .ifPresent(p -> {
                    if (dto.getId() == null || !p.getId().equals(dto.getId())) {
                        throw new RuntimeException("Price already exists for this party & product");
                    }
                });

        UserEntity party = userRepo.findById(dto.getPartyId())
                .orElseThrow(() -> new RuntimeException("Party not found"));

        Products product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        PartyPrice price = (dto.getId() != null)
                ? repo.findById(dto.getId()).orElse(new PartyPrice())
                : new PartyPrice();

        price.setParty(party);
        price.setProduct(product);
        price.setPrice(dto.getPrice());

        PartyPrice saved = repo.save(price);
        return toDto(saved);
    }

    // READ ALL
    public List<PartyPriceDto> getAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // READ BY PARTY
    public List<PartyPriceDto> getByParty(Long partyId) {
        return repo.findByParty_Id(partyId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // DELETE
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // MAPPER
    private PartyPriceDto toDto(PartyPrice p) {
        PartyPriceDto dto = new PartyPriceDto();
        dto.setId(p.getId());
        dto.setPartyId(p.getParty().getId());
        dto.setPartyName(p.getParty().getUsername());
        dto.setProductId(p.getProduct().getId());
        dto.setProductName(p.getProduct().getName());
        dto.setPrice(p.getPrice());
        return dto;
    }
}
