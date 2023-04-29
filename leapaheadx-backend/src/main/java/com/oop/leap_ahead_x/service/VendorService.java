package com.oop.leap_ahead_x.service;

import com.oop.leap_ahead_x.domain.User;
import com.oop.leap_ahead_x.domain.Vendor;

import com.oop.leap_ahead_x.dto.VendorDTO;
import com.oop.leap_ahead_x.repos.UserRepository;
import com.oop.leap_ahead_x.repos.VendorRepository;
import com.oop.leap_ahead_x.exceptions.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    public VendorService(final VendorRepository vendorRepository,
            final UserRepository userRepository) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
    }

    public List<VendorDTO> findAll() {
        final List<Vendor> vendors = vendorRepository.findAll(Sort.by("vendorUuid"));
        return vendors.stream()
                .map((vendor) -> mapToDTO(vendor, new VendorDTO()))
                .toList();
    }

    public VendorDTO get(final UUID vendorUuid) {
        return vendorRepository.findById(vendorUuid)
                .map(vendor -> mapToDTO(vendor, new VendorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final VendorDTO vendorDTO) {
        final Vendor vendor = new Vendor();
        mapToEntity(vendorDTO, vendor);
        return vendorRepository.save(vendor).getVendorUuid();
    }

    public void update(final UUID vendorUuid, final VendorDTO vendorDTO) {
        final Vendor vendor = vendorRepository.findById(vendorUuid)
                .orElseThrow(NotFoundException::new);
        mapToEntity(vendorDTO, vendor);
        vendorRepository.save(vendor);
    }

    public void delete(final UUID vendorUuid) {
        vendorRepository.deleteById(vendorUuid);
    }

    private VendorDTO mapToDTO(final Vendor vendor, final VendorDTO vendorDTO) {
        vendorDTO.setVendorUuid(vendor.getVendorUuid());
        vendorDTO.setCompany(vendor.getCompany());
        vendorDTO.setCountry(vendor.getCountry());
        vendorDTO.setBusinessNature(vendor.getBusinessNature());
        vendorDTO.setGstNumber(vendor.getGstNumber());
        vendorDTO.setCompanyRegistrationNo(vendor.getCompanyRegistrationNo());
        vendorDTO.setUId(vendor.getUId() == null ? null : vendor.getUId().getUId());
        return vendorDTO;
    }

    private Vendor mapToEntity(final VendorDTO vendorDTO, final Vendor vendor) {
        vendor.setCompany(vendorDTO.getCompany());
        vendor.setCountry(vendorDTO.getCountry());
        vendor.setBusinessNature(vendorDTO.getBusinessNature());
        vendor.setGstNumber(vendorDTO.getGstNumber());
        vendor.setCompanyRegistrationNo(vendorDTO.getCompanyRegistrationNo());
        final User uId = vendorDTO.getUId() == null ? null : userRepository.findById(vendorDTO.getUId())
                .orElseThrow(() -> new NotFoundException("uId not found"));
        vendor.setUId(uId);
        return vendor;
    }

    public VendorDTO getVendorByUID(final UUID uId){
        Optional<User> user = userRepository.findById(uId);
        if (user.isPresent()) {
            UUID vendorUUID = vendorRepository.findByuId(user).getVendorUuid();
            return this.get(vendorUUID);
        }else{
            throw new NotFoundException("Not found");
        }
    }
}
