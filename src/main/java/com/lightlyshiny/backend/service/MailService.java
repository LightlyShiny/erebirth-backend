package com.lightlyshiny.backend.service;

import com.lightlyshiny.backend.configuration.FrontEndProperties;
import com.lightlyshiny.backend.configuration.MailProperties;
import com.lightlyshiny.backend.model.TokenEntity;
import com.lightlyshiny.backend.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailProperties mailProperties;
    private final FrontEndProperties frontEndProperties;
    private final JavaMailSender sender;

    public void sendActivationLink(UserEntity user, TokenEntity token) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(mailProperties.getUsername());
        mail.setTo(user.getEmail());
        mail.setSubject("eRebirth account activation link");
        mail.setText(frontEndProperties.getUrl() + "/authentication/activate?uuid=" + token.getToken());
        sender.send(mail);
    }
}