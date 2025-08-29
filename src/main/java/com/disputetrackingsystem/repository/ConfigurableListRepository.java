package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.entity.ConfigurableList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConfigurableListRepository extends JpaRepository<ConfigurableList, Long> {
//    @Query("SELECT * FROM ConfigurableListDetails c WHERE c.name = :name")
//    ConfigurableList findByName(@Param("name") String name);
}
