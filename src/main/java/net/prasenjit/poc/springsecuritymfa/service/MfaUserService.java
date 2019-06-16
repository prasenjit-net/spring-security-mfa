package net.prasenjit.poc.springsecuritymfa.service;

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

import java.net.URLEncoder;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MfaUserService implements UserDetailsService {

    private static final String APP_NAME = "mfa";
    private static String QR_PREFIX =
            "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

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

    public String generateQRUrl(MfaUser user) {
        Totp totp = new Totp(user.getMfaSecret());
        return QR_PREFIX + URLEncoder.encode(totp.uri(user.getUsername()));
    }
}
