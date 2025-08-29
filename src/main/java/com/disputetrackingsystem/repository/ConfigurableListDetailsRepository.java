package com.disputetrackingsystem.repository;

import com.disputetrackingsystem.entity.ConfigurableListDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConfigurableListDetailsRepository extends JpaRepository<ConfigurableListDetails, Long> {

    @Query("SELECT c FROM ConfigurableListDetails c WHERE c.configurableList.name = :listName AND c.name = :detailsName")
    ConfigurableListDetails findByListNameAndDetailsName(@Param("listName") String listName, @Param("detailsName") String detailsName);

}
