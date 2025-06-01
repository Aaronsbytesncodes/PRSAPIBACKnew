package com.PRS.DB;


import org.springframework.data.jpa.repository.JpaRepository;


import com.PRS.model.Vendor;

public interface VendorRepo extends JpaRepository<Vendor, Integer>{

}