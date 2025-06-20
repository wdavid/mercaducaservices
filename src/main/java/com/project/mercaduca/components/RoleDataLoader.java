package com.project.mercaduca.components;

import com.project.mercaduca.models.Role;
import com.project.mercaduca.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RoleDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            List<Role> roles = List.of(
                    new Role("ROLE_ADMIN"),
                    new Role("ROLE_EMPRENDEDOR")
            );
            roleRepository.saveAll(roles);
            System.out.println("✅ Roles iniciales cargados");
        }
    }
}
