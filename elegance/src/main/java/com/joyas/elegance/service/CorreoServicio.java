package com.joyas.elegance.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sibApi.TransactionalEmailsApi;
import sibModel.CreateSmtpEmail;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

@Service
public class CorreoServicio {

    @Value("${brevo.api.key}")
    private String claveApi;

    @Value("${brevo.sender.email}")
    private String correoRemitente;

    @Value("${brevo.sender.name}")
    private String nombreRemitente;

    @Value("${app.base.url}")
    private String baseUrl;

    public void enviarCorreoRecuperacion(String destinatario, String nombre, String token) {
        try {
            ApiClient clienteDefault = Configuration.getDefaultApiClient();
            clienteDefault.setApiKey(claveApi);

            TransactionalEmailsApi api = new TransactionalEmailsApi(clienteDefault);

            SendSmtpEmail email = new SendSmtpEmail();

            SendSmtpEmailSender remitente = new SendSmtpEmailSender();
            remitente.setEmail(correoRemitente);
            remitente.setName(nombreRemitente);
            email.setSender(remitente);

            SendSmtpEmailTo para = new SendSmtpEmailTo();
            para.setEmail(destinatario);
            email.setTo(Collections.singletonList(para));

            email.setSubject("Recuperación de Contraseña - Joyería Elegante");

            String urlRecuperacion = baseUrl + "/cliente/restablecer-contrasena?token=" + token;

            String contenidoHtml = "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                    +
                    "<h2 style='color: #D3AF37;'>Joyería Elegante</h2>" +
                    "<h3>Recuperación de Contraseña</h3>" +
                    "<p>Hola <strong>" + nombre + "</strong>,</p>" +
                    "<p>Hemos recibido una solicitud para restablecer tu contraseña.</p>" +
                    "<p>Para crear una nueva contraseña, haz clic en el siguiente enlace:</p>" +
                    "<p><a href='" + urlRecuperacion
                    + "' style='background-color: #D3AF37; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; display: inline-block;'>Restablecer Contraseña</a></p>"
                    +
                    "<p>Este enlace expirará en 15 minutos.</p>" +
                    "<p>Si no solicitaste este cambio, ignora este mensaje.</p>" +
                    "<hr>" +
                    "<p style='font-size: 12px; color: #999;'>Joyería Elegante - Joyas de lujo para momentos especiales.</p>"
                    +
                    "</div>" +
                    "</body>" +
                    "</html>";

            email.setHtmlContent(contenidoHtml);

            CreateSmtpEmail respuesta = api.sendTransacEmail(email);
            System.out.println("Correo enviado con éxito. ID del mensaje: " + respuesta.getMessageId());

        } catch (ApiException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void enviarCorreoRecuperacionAdmin(String destinatario, String nombre, String token) {
        try {
            ApiClient clienteDefault = Configuration.getDefaultApiClient();
            clienteDefault.setApiKey(claveApi);

            TransactionalEmailsApi api = new TransactionalEmailsApi(clienteDefault);

            SendSmtpEmail email = new SendSmtpEmail();

            SendSmtpEmailSender remitente = new SendSmtpEmailSender();
            remitente.setEmail(correoRemitente);
            remitente.setName(nombreRemitente);
            email.setSender(remitente);

            SendSmtpEmailTo para = new SendSmtpEmailTo();
            para.setEmail(destinatario);
            email.setTo(Collections.singletonList(para));

            email.setSubject("Recuperación de Contraseña - Panel Administrador");

            String urlRecuperacion = baseUrl + "/admin/restablecer-contrasena?token=" + token;

            String contenidoHtml = "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                    +
                    "<h2 style='color: #D3AF37;'>Joyería Elegante - Panel Administrador</h2>" +
                    "<h3>Recuperación de Contraseña</h3>" +
                    "<p>Hola <strong>" + nombre + "</strong>,</p>" +
                    "<p>Hemos recibido una solicitud para restablecer tu contraseña de administrador.</p>" +
                    "<p>Para crear una nueva contraseña, haz clic en el siguiente enlace:</p>" +
                    "<p><a href='" + urlRecuperacion
                    + "' style='background-color: #D3AF37; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; display: inline-block;'>Restablecer Contraseña</a></p>"
                    +
                    "<p>Este enlace expirará en 15 minutos.</p>" +
                    "<p>Si no solicitaste este cambio, ignora este mensaje.</p>" +
                    "<hr>" +
                    "<p style='font-size: 12px; color: #999;'>Joyería Elegante - Joyas de lujo para momentos especiales.</p>"
                    +
                    "</div>" +
                    "</body>" +
                    "</html>";

            email.setHtmlContent(contenidoHtml);

            CreateSmtpEmail respuesta = api.sendTransacEmail(email);
            System.out.println("Correo enviado con éxito. ID del mensaje: " + respuesta.getMessageId());

        } catch (ApiException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}