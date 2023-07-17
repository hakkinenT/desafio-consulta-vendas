package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.ReportMinDTO;
import com.devsuperior.dsmeta.dto.SummaryMinDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(value = "SELECT sale " +
            "FROM Sale sale " +
            "JOIN FETCH sale.seller " +
            "WHERE (sale.date >= :minDate AND sale.date <= :maxDate) " +
            "AND (UPPER(sale.seller.name) LIKE UPPER(CONCAT('%', :name)))",
    countQuery = "SELECT COUNT(sale) " +
            "FROM Sale sale " +
            "JOIN sale.seller " +
            "WHERE (sale.date >= :minDate AND sale.date <= :maxDate) " +
            "AND (UPPER(sale.seller.name) LIKE UPPER(CONCAT('%', :name)))")
    Page<Sale> getReport(LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);

    @Query("SELECT new com.devsuperior.dsmeta.dto.SummaryMinDTO(sale.seller.name, SUM(sale.amount)) " +
            "FROM Sale sale " +
            "WHERE (sale.date >= :minDate AND sale.date <= :maxDate) " +
            "GROUP BY sale.seller.name"
    )
    List<SummaryMinDTO> getSummary(LocalDate minDate, LocalDate maxDate);

}
