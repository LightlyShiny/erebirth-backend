package com.lightlyshiny.backend.controller;

import com.lightlyshiny.backend.dto.response.CheckoutResponseDTO;
import com.lightlyshiny.backend.service.SubscriptionService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<CheckoutResponseDTO> checkout() throws StripeException {
        CheckoutResponseDTO response = subscriptionService.checkout();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@RequestBody String payload,
                                         @RequestHeader("Stripe-Signature") String signature) throws StripeException {
        subscriptionService.activate(payload, signature);
        return ResponseEntity.ok().build();
    }
}