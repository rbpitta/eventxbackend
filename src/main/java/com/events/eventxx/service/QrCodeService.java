package com.events.eventxx.service;

import com.events.eventxx.model.Companion;
import com.events.eventxx.model.Guest;
import com.events.eventxx.repository.CompanionRepository;
import com.events.eventxx.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QrCodeService {
    @Autowired
    GuestRepository guestRepository;
    @Autowired
    CompanionRepository companionRepository;

    public String processQRcode(String qrCode) {
        Optional<Guest> guest = guestRepository.findBycodQrcode(qrCode);
        if (guest.isPresent()) {
            if (guest.get().getStatusAccess()) {
                return "used";
            }
            guest.get().setStatusAccess(true);
            guestRepository.save(guest.get());
            return "valid";
        }

        Optional<Companion> companion = companionRepository.findBycodQrcode(qrCode);
        if (companion.isPresent()) {
            if (companion.get().getStatusAccess()) {
                return "used";
            }
            companion.get().setStatusAccess(true);
            companionRepository.save(companion.get());
            return "valid";
        }
        return "invalid";
    }
}
