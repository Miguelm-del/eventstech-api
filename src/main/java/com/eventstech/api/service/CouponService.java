package com.eventstech.api.service;

import com.eventstech.api.domain.coupon.Coupon;
import com.eventstech.api.domain.coupon.CouponRequestDTO;
import com.eventstech.api.domain.event.Event;
import com.eventstech.api.repositories.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventService eventService;

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO couponData) {
        Event event = eventService.getEventById(eventId);

        Coupon coupon = new Coupon();
        coupon.setCode(couponData.code());
        coupon.setDiscount(couponData.discount());
        coupon.setValid(new Date(couponData.valid()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }
}
