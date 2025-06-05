package com.project.mercaduca.repositories;

import com.project.mercaduca.models.Contract;
import com.project.mercaduca.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findAllByUserId(Long userId);
    Optional<Contract> findByUser(User user);
    Optional<Contract> findByUserId(Long userId);

}
