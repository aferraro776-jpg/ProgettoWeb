package it.unical.progettoweb.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, new OtpEntry(code, LocalDateTime.now().plusMinutes(10)));
        return code;
    }

    public boolean verifyOtp(String email, String code) {
        OtpEntry entry = otpStore.get(email);
        if (entry == null) return false;
        if (LocalDateTime.now().isAfter(entry.expiry())) {
            otpStore.remove(email);
            return false;
        }
        boolean valid = entry.code().equals(code);
        if (valid) otpStore.remove(email);
        return valid;
    }

    public record OtpEntry(String code, LocalDateTime expiry) {}
}