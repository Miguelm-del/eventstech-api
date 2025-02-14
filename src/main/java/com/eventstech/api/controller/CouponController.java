package com.eventstech.api.controller;

import com.eventstech.api.domain.coupon.Coupon;
import com.eventstech.api.domain.coupon.CouponRequestDTO;
import com.eventstech.api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> create(@PathVariable UUID eventId,  @RequestBody CouponRequestDTO body) {
        Coupon coupons = couponService.addCouponToEvent(eventId, body);

        return ResponseEntity.ok(coupons);
    }
}
