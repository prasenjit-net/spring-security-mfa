package net.prasenjit.poc.springsecuritymfa.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import net.prasenjit.poc.springsecuritymfa.model.MfaUser;
import net.prasenjit.poc.springsecuritymfa.repository.MfaUserRepository;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MfaUserService implements UserDetailsService {

    private final MfaUserRepository mfaUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MfaUser> optionalUser = mfaUserRepository.findById(username);
        if (optionalUser.isPresent()) {
            return User.withUsername(username)
                    .authorities("USER")
                    .password(optionalUser.get().getMfaSecret())
                    .roles("USER")
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Transactional
    public MfaUser createUser(String username, String password) {
        MfaUser mfaUser = new MfaUser();
        mfaUser.setUsername(username);
        mfaUser.setPassword(password);
        mfaUser.setMfaSecret(Base32.random());

        return mfaUserRepository.save(mfaUser);
    }

    public String generateBase64QrImage(MfaUser user) throws WriterException, IOException {
        Totp totp = new Totp(user.getMfaSecret());
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix matrix = new MultiFormatWriter().encode(totp.uri("mfa:" + user.getUsername()),
                BarcodeFormat.QR_CODE, 200, 200, hintMap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", stream);

        return "data:image/png;base64," + Base64Utils.encodeToString(stream.toByteArray());
    }
}
