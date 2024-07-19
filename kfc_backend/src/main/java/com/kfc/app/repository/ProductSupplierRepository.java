package com.kfc.app.repository;

import com.kfc.app.dto.ProductSupplierDto;
import com.kfc.app.entities.ProductSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, Integer> {

//    @Query("SELECT m " +
//            "FROM product_supplier ps " +
//            "WHERE ps.supplier_id = :supplierId AND ps.product_id = :productId AND ps.organization_id = :organizationId")
//    ProductSupplierDto findBySupplierAndProductAndOrganization(@Param("supplierId")Integer supplierId,
//                                                               @Param("productId")Integer productId,
//                                                               @Param("organizationId")Integer organizationId);

    Optional<ProductSupplier> findBySupplierIdAndProductIdAndOrganizationId (Integer supplierId, Integer productId, Integer organizationId);

    Page<ProductSupplier> findByOrganizationId(@Param("orgId")Integer orgId, Pageable paging);

    Optional<List<ProductSupplier>> findByOrganizationIdAndSupplierId (Integer organizationId, Integer supplierId);

}
