package com.events.eventxx.controller;

import com.events.eventxx.service.QrCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")

public class QrCodeController {
    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/validate")
    public ResponseEntity processQrcode(@RequestBody String cod) {
        String result = qrCodeService.processQRcode(cod);

        if (result.contains("invalid")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (result.contains("used")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            return ResponseEntity.ok(result);
        }
    }
}
