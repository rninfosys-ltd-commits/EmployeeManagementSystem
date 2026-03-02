package com.schoolapp.repository;

//package com.Crmemp.repository;

//package com.Crmemp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.schoolapp.entity.AutoclaveCycle;

//import com.Crmemp.entity.AutoclaveCycle;

import java.util.Date;
import java.util.List;

public interface AutoclaveRepository extends JpaRepository<AutoclaveCycle, Long> {

  @Query(value = "SELECT autoclave_no FROM autoclave_cycle ORDER BY id DESC LIMIT 1", nativeQuery = true)
  String findLastAutoclaveNo();

  @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM AutoclaveWagon w WHERE w.eBatch = :batchNo")
  boolean existsByWagonBatch(@Param("batchNo") Integer batchNo);

  List<AutoclaveCycle> findByStartedDateBetween(Date start, Date end);

  @Query("SELECT DISTINCT c FROM AutoclaveCycle c JOIN c.wagons w WHERE " +
      "CAST(w.eBatch as string) = :batchNo OR " +
      "CAST(w.mBatch as string) = :batchNo OR " +
      "CAST(w.wBatch as string) = :batchNo")
  List<AutoclaveCycle> findByBatchNo(@Param("batchNo") String batchNo);
}
