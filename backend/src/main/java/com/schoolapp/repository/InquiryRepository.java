package com.schoolapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolapp.entity.Inquiry;
//import com.Crmemp.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {

    // CREATE CHECK
    boolean existsByLeadAccountIdAndProjectCodeAndUnitCode(
            int leadAccountId,
            int projectCode,
            int unitCode
    );

    // UPDATE CHECK (exclude same record)
    boolean existsByLeadAccountIdAndProjectCodeAndUnitCodeAndInqueryIdNot(
            int leadAccountId,
            int projectCode,
            int unitCode,
            int inqueryId
    );
}
