package com.deft.admin.services;

import com.deft.admin.data.ChatSettingsEntity;
import com.deft.admin.repository.ChatSettingsAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatSettingsAdminService {

    private final ChatSettingsAdminRepository repository;

    public Optional<ChatSettingsEntity> get(String id) {
        return repository.findById(id);
    }

    public ChatSettingsEntity update(ChatSettingsEntity entity) {
        return repository.save(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Page<ChatSettingsEntity> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<ChatSettingsEntity> findAll() {
        return repository.findAll();
    }

    public Page<ChatSettingsEntity> list(Pageable pageable, Specification<ChatSettingsEntity> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
