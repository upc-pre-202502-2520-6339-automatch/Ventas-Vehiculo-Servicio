package com.sales.domain.model.aggregates;

import com.sales.domain.model.valueobjects.ImageUrl;
import com.sales.domain.model.valueobjects.SaleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@Entity
@Table(name = "sale_orders")
@EntityListeners(AuditingEntityListener.class)
public class SaleOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name="vehicle_id", nullable=false)
    private Long vehicleId;

    @Column(name="buyer_id",   nullable=false)
    private Long buyerId;

    @Column(name="seller_id",  nullable=false)
    private Long sellerId;

    @Column(name="price_amount", nullable=false, precision=18, scale=2)
    private BigDecimal priceAmount;

    @Column(name="price_currency", nullable=false, length=3)
    private String priceCurrency;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private SaleStatus status = SaleStatus.INITIATED;

    @Column(name="reservation_expires_at")
    private Instant reservationExpiresAt;

    @ElementCollection
    @CollectionTable(name="sale_images", joinColumns=@JoinColumn(name="sale_id"))
    @OrderColumn(name="position")
    @AttributeOverrides({
            @AttributeOverride(name="value", column=@Column(name="url", nullable=false, length=2048))
    })
    private List<ImageUrl> images;

    public SaleOrder() {}

    // --- Domain behavior ---
    public void addImages(List<String> urls, int max) {
        for (String u : urls) images.add(new ImageUrl(u));
        if (images.size() > max) throw new IllegalArgumentException("Max images exceeded");
    }
    public void replaceImages(List<String> urls, int max) {
        images.clear(); addImages(urls, max);
    }
    public void removeImageAt(int pos) {
        if (pos < 0 || pos >= images.size()) throw new IllegalArgumentException("Invalid position");
        images.remove(pos);
    }

    public void reserveUntil(Instant expiresAt){
        if (status != SaleStatus.INITIATED && status != SaleStatus.PAYMENT_PENDING)
            throw new IllegalArgumentException("Cannot reserve from status " + status);
        this.status = SaleStatus.RESERVED;
        this.reservationExpiresAt = expiresAt;
    }
    public void markPaid(){
        if (status != SaleStatus.RESERVED && status != SaleStatus.PAYMENT_PENDING)
            throw new IllegalArgumentException("Cannot mark paid from " + status);
        this.status = SaleStatus.PAID;
    }
    public void cancel(){ this.status = SaleStatus.CANCELLED; }

    public void expire() { this.status = SaleStatus.EXPIRED; }


}
