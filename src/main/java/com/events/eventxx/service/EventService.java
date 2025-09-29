package com.events.eventxx.service;

import com.events.eventxx.model.Companion;
import com.events.eventxx.model.Event;
import com.events.eventxx.model.EventDto;
import com.events.eventxx.model.Guest;
import com.events.eventxx.repository.CompanionRepository;
import com.events.eventxx.repository.EventRepository;
import com.events.eventxx.repository.GuestRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    GuestRepository guestRepository;

    @Autowired
    CompanionRepository companionRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;


    public void saveEvent(EventDto eventDto) throws Exception {
        Event event = new Event();
        String codEvt = generateNumericCode();
        event.setCodEvent(codEvt);
        event.setEmail(eventDto.getEmail());
        event.setCategory(eventDto.getCategory());
        event.setDateEvent(eventDto.getDateEvent());
        event.setName(eventDto.getName());
        eventRepository.save(event);
        sendEmailWithCodeEvent(event.getEmail(), codEvt);
    }

    @Async
    public void processQrCodeAsync(String codEvt, Pageable pageable) {
        Event event = eventRepository.findByCodEvent(codEvt);
        processQRCode(codEvt, event, pageable);
        processQrCodeCompanion(codEvt, event);
    }

    public void processQRCode(String codEvt, Event event, Pageable pageable) {
        Page<Guest> guests = guestRepository.findByEvent_CodEvent(codEvt, pageable);

        for (Guest guest : guests) {
            String keyGuest = generateKey("guest_" + guest.getId() + "_");
            byte[] qrGuest = generateQRCode(keyGuest);
            try {
                sendEmailWithQrCode(guest.getNome(), guest.getEmail(), qrGuest, event);
                guest.setCodQrcode(keyGuest);
                guest.setStatusEnvio("enviado");
                guestRepository.save(guest);
            } catch (RuntimeException runtimeException) {
                guest.setCodQrcode(keyGuest);
                guest.setStatusEnvio("falha");
                guest.setMsgError(runtimeException.getMessage());
                guestRepository.save(guest);
            }
        }
    }

    private void processQrCodeCompanion(String cod, Event event) {
        List<Companion> companions = companionRepository.findByGuest_Event_CodEvent(cod);
        for (Companion companion : companions) {
            String keyCompanion = generateKey("companion_" + companion.getId() + "_");
            byte[] qrCompanion = generateQRCode(keyCompanion);
            try {
                sendEmailWithQrCode(companion.getNome(), companion.getEmail(), qrCompanion, event);
                companion.setCodQrcode(keyCompanion);
                companion.setStatusEnvio("enviado");
                companionRepository.save(companion);
            } catch (RuntimeException runtimeException) {
                companion.setCodQrcode(keyCompanion);
                companion.setStatusEnvio("falha");
                companion.setMsgError(runtimeException.getMessage());
                companionRepository.save(companion);
            }
        }
    }

    public static String generateNumericCode() {
        Random random = new Random();
        int code = random.nextInt(100_000); // 0 a 99999
        return String.format("%05d", code); // String com 5 dígitos, zeros à esquerda
    }

    public String generateKey(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar chave", e);
        }
    }

    public byte[] generateQRCode(String texto) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(texto, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);
            return output.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar QR Code", e);
        }
    }

    public void sendEmailWithQrCode(String name, String email, byte[] qrCode, Event event) {
        try {
            if (email != null) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom("convidado@eventxx.com.br");
                helper.setTo(email);
                helper.setSubject("Seu QR Code para o Evento");

                String htmlContent = "<html>"
                        + "<head>"
                        + "<style>"
                        + "body { font-family: Arial, sans-serif; line-height: 1.6; }"
                        + ".container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }"
                        + ".header { background-color: #f4f4f4; padding: 10px; text-align: center; }"
                        + ".footer { margin-top: 20px; font-size: 0.9em; color: #555; text-align: center; }"
                        + "</style>"
                        + "</head>"
                        + "<body>"
                        + "<div class='container'>"
                        + "<div class='header'>"
                        + "<h2>Olá, " + name + "!</h2>"
                        + "</div>"
                        + "<h3>Aqui está seu QR Code para o evento <strong>" + event.getName() + "</strong>:</h3>"
                        + "<img src='cid:qrCodeImage' alt='QR Code' style='width:200px;height:auto;'/>"
                        + "<p>Sua presença será parte essencial deste momento único. Caso tenha alguma dúvida, não hesite em nos contatar</p>"
                        + "<div class='footer'>"
                        + "<p>Atenciosamente,<br/>Equipe Eventxx</p>"
                        + "</div>"
                        + "</div>"
                        + "</body>"
                        + "</html>";

                helper.setText(htmlContent, true);
                helper.addInline("qrCodeImage", new ByteArrayResource(qrCode), "image/png");

                mailSender.send(message);
            }
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }

//    public void sendEmailWithQrCode(String email, byte[] qrCode, Event event) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setFrom("convidado@eventxx.com.br");
//            helper.setTo(email);
//            helper.setSubject("Seu QR Code");
//
//            String htmlContent = "<h3>Olá, " + email + "!</h3>"
//                    + "<h3>Aqui está seu QR Code para o Evento" + event.getName() + ":</h3>"
//                  //  + "<img src='cid:qrCodeImage' alt='Logo da Empresa' style='width:200px;height:auto;'/>";
//
//            helper.setText(htmlContent, true);
//            helper.addInline("qrCodeImage", new ByteArrayResource(qrCode), "image/png");
//
//            mailSender.send(message);
//		} catch (MessagingException e) {
//            throw new RuntimeException("Erro ao enviar e-mail", e);
//        }
//    }


    public void sendEmailWithCodeEvent(String email, String codEvent) {
        emailService.sendCodigoEvent(email, codEvent);
    }

    @Async
    public void sendQrCode(Guest guest, Event event) {
        String keyGuest = generateKey("guest_" + guest.getId() + "_");

        byte[] qrGuest = generateQRCode(keyGuest);
        sendEmailWithQrCode(guest.getNome(), guest.getEmail(), qrGuest, event);
        guest.setCodQrcode(keyGuest);
        guest.setStatusEnvio("enviado");
        guestRepository.save(guest);
    }

    @Async
    public void sendQrCodeCompanion(Companion companion, Event event) {

        String keyGuest = generateKey("companion_" + companion.getId() + "_");
        byte[] qrGuest = generateQRCode(keyGuest);
            sendEmailWithQrCode(companion.getNome(), companion.getEmail(), qrGuest, event);
            companion.setCodQrcode(keyGuest);
            companion.setStatusEnvio("enviado");
            companionRepository.save(companion);

    }
}
