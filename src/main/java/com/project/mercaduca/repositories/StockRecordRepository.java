package com.project.mercaduca.repositories;

import com.project.mercaduca.models.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {

}
