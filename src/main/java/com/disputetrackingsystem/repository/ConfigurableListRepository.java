package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.model.ConfigurableList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurableListRepository extends JpaRepository<ConfigurableList, Long> {
//    @Query("SELECT * FROM ConfigurableListDetails c WHERE c.name = :name")
//    ConfigurableList findByName(@Param("name") String name);
}
