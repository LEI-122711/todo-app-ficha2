package com.example.newFeatures;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    private static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    private static final String FROM_PHONE = System.getenv("TWILIO_PHONE_NUMBER");
    private static final String WHATSAPP_FROM = System.getenv("TWILIO_WHATSAPP_NUMBER");

    public MessageService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void enviarSMS(String numeroDestino, String texto) {
        Message.creator(
                new PhoneNumber(numeroDestino),
                new PhoneNumber(FROM_PHONE),
                texto
        ).create();
    }

    public void enviarWhatsApp(String numeroDestino, String texto) {
        Message.creator(
                new PhoneNumber("whatsapp:" + numeroDestino),
                new PhoneNumber("whatsapp:" + WHATSAPP_FROM),
                texto
        ).create();
    }
}
