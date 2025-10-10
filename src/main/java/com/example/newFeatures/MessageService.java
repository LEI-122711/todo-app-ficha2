package com.example.newFeatures;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final String accountSid;
    private final String authToken;
    private final String numeroTwilio;
    private final String numeroWhatsappTwilio;

    public MessageService(
            @Value("${twilio.account-sid}") String accountSid,
            @Value("${twilio.auth-token}") String authToken,
            @Value("${twilio.phone-number}") String numeroTwilio,
            @Value("${twilio.whatsapp-number}") String numeroWhatsappTwilio
    ) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.numeroTwilio = numeroTwilio;
        this.numeroWhatsappTwilio = numeroWhatsappTwilio;

        Twilio.init(accountSid, authToken);
    }

    public void enviarSMS(String numeroDestino, String texto) {
        Message.creator(
                new PhoneNumber(numeroDestino),
                new PhoneNumber(numeroTwilio),
                texto
        ).create();
    }

    public void enviarWhatsApp(String numeroDestino, String texto) {
        Message.creator(
                new PhoneNumber("whatsapp:" + numeroDestino),
                new PhoneNumber("whatsapp:" + numeroWhatsappTwilio),
                texto
        ).create();
    }
}
