package com.oop.leap_ahead_x.repos;

import com.oop.leap_ahead_x.domain.InputComponent;

import java.util.List;
import java.util.UUID;

import com.oop.leap_ahead_x.domain.SubformCanvas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InputComponentRepository extends JpaRepository<InputComponent, UUID> {
    @Query("SELECT c FROM InputComponent c WHERE c.parentCanvas= :sfcId order by c.orderNo")
    List<InputComponent> findByParentCanvas(SubformCanvas sfcId);
}
