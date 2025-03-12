package com.kshirsa.trackingservice.repository;

import com.kshirsa.trackingservice.entity.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TransactionRepo extends ListCrudRepository<Transactions, String> {

    @Query(value = " SELECT * FROM transactions WHERE user_id = ?1 ", nativeQuery = true)
    List<Transactions> findAllByUserId(String user, Pageable pageRequest);

    @Query(value = """
                        SELECT DISTINCT t.* FROM transactions t
                        INNER JOIN category c ON t.category_id = c.category_id
                        INNER JOIN transaction_tags tt ON t.transaction_id = tt.transaction_id
                        WHERE t.user_id = ?9
                        AND ( c.category_name = ANY(CAST(?1 AS text[])) OR CAST(?1 AS text[]) IS NULL )
                        AND ( t.payment_mode = ANY(CAST(?2 AS text[])) OR CAST(?2 AS text[]) IS NULL )
                        AND ( t.transaction_type = ANY(CAST(?3 AS text[])) OR CAST(?3 AS text[]) IS NULL )
                        AND ( t.amount <= CAST(?4 AS numeric) OR CAST(?4 AS numeric) IS NULL )
                        AND ( t.amount >= CAST(?5 AS numeric) OR CAST(?5 AS numeric) IS NULL )
                        AND ( t.transaction_time >= CAST(?6 AS timestamp) OR CAST(?6 AS timestamp) IS NULL )
                        AND ( t.transaction_time <= CAST(?7 AS timestamp) OR CAST(?7 AS timestamp) IS NULL )
                        AND ( tt.tags ILIKE ANY(CAST(?8 AS text[])) OR CAST(?8 AS text[]) IS NULL )
            """, nativeQuery = true)
    Page<Transactions> findTransactions(String[] category, String[] paymentMode, String[] transactionType,
                                        Double amountMax, Double amountMin, LocalDateTime transactionAfter,
                                        LocalDateTime transactionBefore, String[] hashtags, String user, PageRequest pageRequest);

    @Query(value = """
                        SELECT DISTINCT tt.tags FROM transactions tr
                        INNER JOIN transaction_tags tt ON tr.transaction_id = tt.transaction_id
                        WHERE tr.user_id = ?1
            """, nativeQuery = true)
    Set<String> findHashTagsByUserId(String userId);
}
