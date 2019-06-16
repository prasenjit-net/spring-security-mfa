package net.prasenjit.poc.springsecuritymfa;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityMfaApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityMfaApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String random = Base32.random();
		Totp otp = new Totp(random);
		System.out.println(otp.uri("admin"));
		System.out.println(otp.verify(otp.now()));
	}
}
