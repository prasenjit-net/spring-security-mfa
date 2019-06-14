package net.prasenjit.poc.springsecuritymfa.service;

import lombok.RequiredArgsConstructor;
import net.prasenjit.poc.springsecuritymfa.model.MfaUser;
import net.prasenjit.poc.springsecuritymfa.repository.MfaUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    .password(optionalUser.get().getPassword())
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
        mfaUser.setMfaSecret(password);

        return mfaUserRepository.save(mfaUser);
    }
}
