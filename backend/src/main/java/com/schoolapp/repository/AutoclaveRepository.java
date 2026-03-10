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

  @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM AutoclaveWagon w WHERE w.batchNo = :batchNo")
  boolean existsByWagonBatch(@Param("batchNo") Integer batchNo);

  List<AutoclaveCycle> findByStartedDateBetween(Date start, Date end);

  @Query("SELECT DISTINCT c FROM AutoclaveCycle c JOIN c.wagons w WHERE " +
      "CAST(w.batchNo as string) = :batchNo")
  List<AutoclaveCycle> findByBatchNo(@Param("batchNo") String batchNo);

  @org.springframework.data.jpa.repository.Query("SELECT a FROM AutoclaveCycle a WHERE a.plantName = :plantName")
  List<AutoclaveCycle> findByPlantName(@org.springframework.data.repository.query.Param("plantName") String plantName);

  @org.springframework.data.jpa.repository.Query("SELECT a FROM AutoclaveCycle a WHERE a.startedDate BETWEEN :start AND :end AND a.plantName = :plantName")
  List<AutoclaveCycle> findByStartedDateBetweenAndPlantName(
      @org.springframework.data.repository.query.Param("start") Date start,
      @org.springframework.data.repository.query.Param("end") Date end,
      @org.springframework.data.repository.query.Param("plantName") String plantName);
}
