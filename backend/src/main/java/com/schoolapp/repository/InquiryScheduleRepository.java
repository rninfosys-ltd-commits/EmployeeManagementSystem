package com.schoolapp.repository;

//package com.Crmemp.repository;

//import com.Crmemp.entity.InquirySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolapp.entity.InquirySchedule;

@Repository
public interface InquiryScheduleRepository extends JpaRepository<InquirySchedule, Long> {
}
