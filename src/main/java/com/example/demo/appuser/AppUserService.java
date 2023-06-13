package com.example.demo.appuser;

import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) //used to load an AppUser entity by email address
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser) { //to register a new user
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {

            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());
        //the password is encrypted using the BCryptPasswordEncoder if user not found

        appUser.setPassword(encodedPassword);  //encrypted password is then set on the AppUser entity

        appUserRepository.save(appUser);//save to the databse

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
//new confirmation token is generated and saved to the database
        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

//
//        public void sendConfirmationEmail(String recipientEmail, String token) {
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setTo(recipientEmail);
//            mailMessage.setSubject("Complete Registration!");
//            mailMessage.setFrom("<your-email-address>");
//            mailMessage.setText("To confirm your account, please click here : "
//                    +"http://localhost:8080/confirm-account?token="+token);
//
//            javaMailSender.send(mailMessage);
//        }

        return token;
    }

    public int enableAppUser(String email) {
//        confirmationTokenService.saveConfirmationToken(confirmationToken);
//        sendConfirmationEmail(appUser.getEmail(), token);

        return appUserRepository.enableAppUser(email);
    }
}
