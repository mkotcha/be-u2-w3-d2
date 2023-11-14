package org.emmek.beu2w3d2.repositories;

import org.emmek.beu2w3d2.entities.Device;
import org.emmek.beu2w3d2.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Page<Device> getDevicesByUser(User user, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.state = 'maintenance'")
    Page<Device> getDevicesInMaintenance(Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.category = :category")
    Page<Device> getByCategory(String category, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.category = :category AND d.user = :user")
    Page<Device> getByCategoryAndUser(String category, User user, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.state = :state")
    Page<Device> getByState(String state, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.state = :state AND d.user = :user")
    Page<Device> getByStateAndUser(String state, User user, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.category = :category AND d.state = :state")
    Page<Device> getByStateAndCategory(String state, String category, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.category = :category AND d.state = :state AND d.user = :user")
    Page<Device> getByStateAndCategoryAndUser(String state, String category, User user, Pageable pageable);
}
