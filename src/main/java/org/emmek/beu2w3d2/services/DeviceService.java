package org.emmek.beu2w3d2.services;

import org.emmek.beu2w3d2.entities.Device;
import org.emmek.beu2w3d2.entities.User;
import org.emmek.beu2w3d2.exception.BadRequestException;
import org.emmek.beu2w3d2.exception.NotFoundException;
import org.emmek.beu2w3d2.payloads.DevicePostDTO;
import org.emmek.beu2w3d2.payloads.DevicePutDTO;
import org.emmek.beu2w3d2.repositories.DeviceRepository;
import org.emmek.beu2w3d2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    UserRepository userRepository;

    public Device save(DevicePostDTO body) {
        if (isValidCategory(body.category())) {
            Device device = new Device();
            device.setCategory(body.category());
            device.setState("available");
            return deviceRepository.save(device);
        } else {
            throw new BadRequestException("Device category '" + body.category() + "' is not allowed");
        }
    }

    private boolean isValidCategory(String category) {
        List<String> allowedCategories = Arrays.asList("smartphone", "tablet", "laptop", "smartwatch");
        return allowedCategories.contains(category);
    }

    public Page<Device> getDevices(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return deviceRepository.findAll(pageable);
    }

    public Device findById(long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        deviceRepository.delete(device);
    }

    public Device findByIdAndUpdate(long id, DevicePutDTO body) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        validateUpdateState(device, body.state());
        validateUpdateCategory(device, body.category());
        return deviceRepository.save(device);
    }

    private void validateUpdateState(Device device, String newState) {
        List<String> allowedStates = Arrays.asList("available", "assigned", "maintenance", "disused");
        if (!allowedStates.contains(newState)) {
            throw new BadRequestException("Device state '" + newState + "' is not allowed");
        }
        device.setState(newState);
    }

    private void validateUpdateCategory(Device device, String newCategory) {
        List<String> allowedCategories = Arrays.asList("smartphone", "tablet", "laptop");
        if (!allowedCategories.contains(newCategory)) {
            throw new BadRequestException("Device category '" + newCategory + "' is not allowed");
        }
        device.setCategory(newCategory);
    }

    public Page<Device> getDevicesByUserId(long id, int page, int size, String sort) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return deviceRepository.getDevicesByUser(user, pageable);
    }

    public Device assignUser(long deviceId, long userId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new NotFoundException(deviceId));
        device.setState("assigned");
        device.setUser(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId)));
        return deviceRepository.save(device);
    }

    public Device unassignUser(long deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new NotFoundException(deviceId));
        device.setState("available");
        device.setUser(null);
        return deviceRepository.save(device);
    }

    public Device putInMaintenance(long deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new NotFoundException(deviceId));
        device.setState("maintenance");
        return deviceRepository.save(device);
    }

    public Device putInDisused(long deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new NotFoundException(deviceId));
        device.setState("disused");
        if (device.getUser() != null) {
            device.setUser(null);
        }
        return deviceRepository.save(device);
    }

    public Device setAvailable(long deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new NotFoundException(deviceId));
        device.setState("available");
        if (device.getUser() != null) {
            device.setUser(null);
        }
        return deviceRepository.save(device);
    }

    public Page<Device> getDevices(long userId, String category, String state, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        if (userId > 0) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
            if (category.isEmpty() && state.isEmpty()) {
                return deviceRepository.getDevicesByUser(user, pageable);
            } else {
                if (category.isEmpty() && !state.isEmpty()) {
                    return deviceRepository.getByStateAndUser(state, user, pageable);
                }
                if (state.isEmpty() && !category.isEmpty()) {
                    return deviceRepository.getByCategoryAndUser(category, user, pageable);
                }
                return deviceRepository.getByStateAndCategoryAndUser(state, category, user, pageable);
            }
        } else {
            if (category.isEmpty() && state.isEmpty()) {
                return deviceRepository.findAll(pageable);
            } else {
                if (category.isEmpty() && !state.isEmpty()) {
                    return deviceRepository.getByState(state, pageable);
                }
                if (state.isEmpty() && !category.isEmpty()) {
                    return deviceRepository.getByCategory(category, pageable);
                }
                return deviceRepository.getByStateAndCategory(state, category, pageable);
            }
        }
    }
}




