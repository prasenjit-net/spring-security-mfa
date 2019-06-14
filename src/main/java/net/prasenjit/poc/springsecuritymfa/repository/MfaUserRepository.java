package net.prasenjit.poc.springsecuritymfa.repository;

import net.prasenjit.poc.springsecuritymfa.model.MfaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MfaUserRepository extends JpaRepository<MfaUser, String> {
}
