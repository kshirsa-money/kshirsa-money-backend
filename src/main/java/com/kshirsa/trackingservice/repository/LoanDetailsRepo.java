package com.kshirsa.trackingservice.repository;

import com.kshirsa.trackingservice.entity.LoanDetails;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface LoanDetailsRepo extends ListCrudRepository<LoanDetails,String> {

    Optional<LoanDetails> findByTransaction_TransactionId(String s);
}
