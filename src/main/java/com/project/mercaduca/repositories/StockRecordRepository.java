package com.project.mercaduca.repositories;

import com.project.mercaduca.models.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {

}
