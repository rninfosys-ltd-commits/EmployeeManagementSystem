package com.schoolapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.schoolapp.entity.CustomerTrnDetails;
import com.schoolapp.entity.BatchDetails;

@Repository
public interface CustomerTrnDetailsRepository extends JpaRepository<CustomerTrnDetails, Long> {
    List<CustomerTrnDetails> findByBatchDetails(BatchDetails batchDetails);

    List<CustomerTrnDetails> findByTrbactno(Long trbactno);
}
