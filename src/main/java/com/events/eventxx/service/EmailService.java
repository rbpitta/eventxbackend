package com.events.eventxx.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender emailSender;

	@Async
	public void sendCodigoEvent(String email, String codEvent){
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom("convidado@eventxx.com.br");
			helper.setTo(email);
			helper.setSubject("Seu QR Code");

			String htmlContent = "<h3>Olá, " + email + "!</h3>"
					+ "<h3>Evento cadastrado com sucesso! Código de acesso " + codEvent + " </h3>";

			helper.setText(htmlContent, true);

			emailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar e-mail", e);
		}
	}

	@Async
	public void sendSimpleEmail(String to, String subject, String text) throws WriterException, MessagingException {
		String imagemBase64 = gerarQRCodeBase64("teste");
		String html = "<h3>Olá!</h3>" +
			"<p>Segue seu QR Code:</p>" +
			"<img src='data:image/png;base64," + imagemBase64 + "' alt='QR Code'/>";

//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(to);
//		message.setSubject(subject);
//		message.setText(html);
//		emailSender.send(message);
		enviarComQrNoCorpo(to,gerarQRCodeBase64("teste"));
	}
	public static String gerarQRCodeBase64(String texto) throws WriterException {
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, 250, 250);
			ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
			byte[] pngData = pngOutputStream.toByteArray();
			return Base64.getEncoder().encodeToString(pngData);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao gerar QR Code", e);
		}
	}


	public void enviarComQrNoCorpo(String emailDestino, String base64Qr) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(emailDestino);
		helper.setSubject("Seu QR Code");
		String html = "<p>Olá! Escaneie o QR abaixo:</p>" +
			"<img src='data:image/png;base64," + base64Qr + "'/>";
		helper.setText(html, true);

		emailSender.send(message);
	}


}
