package com.gmail.kolyvas.hotelapp.repositories;

import com.gmail.kolyvas.hotelapp.model.PricingPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingPolicyRepository extends JpaRepository<PricingPolicy, Long> {
    public PricingPolicy findPricingPolicyById(Long id);
    @Query("select pp from PricingPolicy  pp where pp.customerType.id = ?1")
    public List<PricingPolicy> findPricingPolicyForCustomerType(Long customerTypeId);

    @Query("select pp from PricingPolicy  pp where pp.roomCategory.id = ?1")
    public List<PricingPolicy> findPricingPolicyForRoomCategory(Long categoryId);
    @Query("select pp from PricingPolicy pp where pp.roomCategory.id = ?1 and pp.customerType = null ")
    public List<PricingPolicy> findDefaultPricingPolicies(Long categoryId);

    @Query("select pp from PricingPolicy pp where pp.roomCategory.id = ?1 and pp.customerType.id = ?2")
    public List<PricingPolicy> findSpecificPricingPolicies(Long categoryId, Long custTypeId);
}
