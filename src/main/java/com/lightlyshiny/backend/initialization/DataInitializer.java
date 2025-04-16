package com.lightlyshiny.backend.initialization;

import com.lightlyshiny.backend.model.RoleEntity;
import com.lightlyshiny.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new RoleEntity(null, "ROLE_OWNER"));
            roleRepository.save(new RoleEntity(null, "ROLE_EMPLOYEE"));
        }
    }
}