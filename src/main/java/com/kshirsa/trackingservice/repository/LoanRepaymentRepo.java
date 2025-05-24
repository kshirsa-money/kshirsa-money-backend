package com.kshirsa.trackingservice.repository;

import com.kshirsa.trackingservice.entity.LoanRepayment;
import org.springframework.data.repository.ListCrudRepository;

public interface LoanRepaymentRepo extends ListCrudRepository<LoanRepayment, String> {
}
