package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.ReportMinDTO;
import com.devsuperior.dsmeta.dto.SummaryMinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;


@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}


	public Page<ReportMinDTO> getReport(String minDate, String maxDate, String name, Pageable pageable) {

		LocalDate finalDate = createFinalDate(maxDate);
		LocalDate initialDate = createInitialDate(minDate, finalDate);

		Page<Sale> sales = repository.getReport(initialDate, finalDate, name, pageable);

		Page<ReportMinDTO> dto = sales.map(sale -> new ReportMinDTO(sale));
		return dto;
	}


	public List<SummaryMinDTO> getSummary(String minDate, String maxDate) {
		LocalDate finalDate = createFinalDate(maxDate);
		LocalDate initialDate = createInitialDate(minDate, finalDate);

		List<SummaryMinDTO> dto = repository.getSummary(initialDate, finalDate);

		return dto;
	}

	private LocalDate createInitialDate(String date, LocalDate finalDate){
		LocalDate initialDate;

		if(date.isEmpty()){
			initialDate = finalDate.minusYears(1L);
		}else{
			initialDate = LocalDate.parse(date);
		}

		return initialDate;
	}

	private LocalDate createFinalDate(String date){
		LocalDate finalDate;

		if(date.isEmpty()){
			finalDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}else{
			finalDate = LocalDate.parse(date);
		}

		return finalDate;
	}
}
