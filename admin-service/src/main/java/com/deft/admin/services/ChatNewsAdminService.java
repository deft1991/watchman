package com.deft.admin.services;

import com.deft.admin.data.ChatNewsAdminEntity;
import com.deft.admin.repository.ChatNewsAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatNewsAdminService {

    private final ChatNewsAdminRepository repository;

    public Optional<ChatNewsAdminEntity> get(String id) {
        return repository.findById(id);
    }

    public ChatNewsAdminEntity update(ChatNewsAdminEntity entity) {
        return repository.save(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Page<ChatNewsAdminEntity> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ChatNewsAdminEntity> list(Pageable pageable, Specification<ChatNewsAdminEntity> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
