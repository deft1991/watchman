package com.deft.admin.services;

import com.deft.admin.data.BotMessageEntity;
import com.deft.admin.repository.BotMessageAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BotMessageAdminService {

    private final BotMessageAdminRepository repository;

    public Optional<BotMessageEntity> get(String id) {
        return repository.findById(id);
    }

    public BotMessageEntity save(BotMessageEntity entity) {
        return repository.save(entity);
    }

    public BotMessageEntity update(BotMessageEntity entity) {
        return repository.save(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Page<BotMessageEntity> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<BotMessageEntity> findAll() {
        return repository.findAll();
    }

    public Page<BotMessageEntity> list(Pageable pageable, Specification<BotMessageEntity> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
