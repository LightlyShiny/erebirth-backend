package com.lightlyshiny.backend.service;

import com.lightlyshiny.backend.configuration.FrontEndProperties;
import com.lightlyshiny.backend.configuration.StripeProperties;
import com.lightlyshiny.backend.dto.response.CheckoutResponseDTO;
import com.lightlyshiny.backend.exception.custom.ActiveSubscriptionException;
import com.lightlyshiny.backend.exception.custom.UserNotFoundException;
import com.lightlyshiny.backend.model.UserEntity;
import com.lightlyshiny.backend.repository.UserRepository;
import com.stripe.Stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final FrontEndProperties frontEndProperties;
    private final StripeProperties stripeProperties;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeProperties.getApiSecret();
    }

    public CheckoutResponseDTO checkout() throws StripeException {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (user.get().getSubscriptionEnd() != null && user.get().getSubscriptionEnd().isAfter(LocalDateTime.now())) {
            throw new ActiveSubscriptionException();
        }
        SessionCreateParams parameters = SessionCreateParams
                .builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontEndProperties.getUrl() + stripeProperties.getSuccessUrl())
                .setCancelUrl(frontEndProperties.getUrl() + stripeProperties.getCancelUrl())
                .putMetadata("email", email)
                .addLineItem(SessionCreateParams
                        .LineItem
                        .builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams
                                .LineItem
                                .PriceData
                                .builder()
                                .setCurrency(stripeProperties.getSubscriptionCurrency())
                                .setUnitAmount(stripeProperties.getSubscriptionPrice() * 100L)
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Subscription")
                                        .build())
                                .build())
                        .build())
                .build();
        Session session = Session.create(parameters);
        String url = session.getUrl();
        CheckoutResponseDTO response = new CheckoutResponseDTO(url);
        return response;
    }

    public void activate(@RequestBody String payload,
                         @RequestHeader("Stripe-Signature") String signature) throws StripeException {
        Event event = Webhook.constructEvent(payload, signature, stripeProperties.getWebhookSecret());
        if (event.getType().equals("checkout.session.completed")
                && event.getDataObjectDeserializer().getObject().isPresent()) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().get();
            String email = session.getMetadata().get("email");
            Optional<UserEntity> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                throw new UserNotFoundException();
            }
            if (user.get().getSubscriptionEnd() == null) {
                user.get().setSubscriptionEnd(LocalDateTime.now().plusDays(31));
            } else if (user.get().getSubscriptionEnd().isAfter(LocalDateTime.now())) {
                user.get().setSubscriptionEnd(user.get().getSubscriptionEnd().plusDays(31));
            } else {
                user.get().setSubscriptionEnd(LocalDateTime.now().plusDays(31));
            }
            userRepository.save(user.get());
        }
    }
}