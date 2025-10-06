package com.example.newFeatures;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

@Route("oauth2callback")
public class GoogleAuthenticator extends VerticalLayout implements BeforeEnterObserver {

    private static final String CLIENT_ID = "1041964997115-vv0vgdr91kc7hgvk2auvj3glnvtig1sf.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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"; // ⚠️ mete o teu
    private static final String REDIRECT_URI = "http://localhost:8080/oauth2callback";

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        System.out.println("beforeEnter chamado ✅");

        QueryParameters queryParams = event.getLocation().getQueryParameters();
        System.out.println("Query Parameters: " + queryParams);

        var codes = queryParams.getParameters().get("code");
        if (codes == null || codes.isEmpty()) {
            add(new Span("Nenhum código recebido na URL."));
            return;
        }

        String code = codes.get(0);
        System.out.println("Código recebido: " + code);

        try {
            GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://oauth2.googleapis.com/token",
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    REDIRECT_URI
            );

            GoogleTokenResponse tokenResponse = tokenRequest.execute();
            String accessToken = tokenResponse.getAccessToken();

            add(new Span("Autenticação concluída! Token: " + accessToken));
            Notification.show("Autenticação concluída!", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            // Guardar token na sessão
            event.getUI().getSession().setAttribute("GOOGLE_ACCESS_TOKEN", accessToken);

        } catch (Exception e) {
            e.printStackTrace();
            add(new Span("Erro na autenticação: " + e.getMessage()));
            Notification.show("Erro na autenticação: " + e.getMessage(), 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
