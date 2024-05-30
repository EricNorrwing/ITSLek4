package se.norrwing.itslek4demo.controller;

import com.google.zxing.qrcode.encoder.QRCode;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import se.norrwing.itslek4demo.DTO.UserDTO;
import se.norrwing.itslek4demo.repository.UserRepository;

@Controller
public class PageController {

    private PasswordEncoder passwordEncoder;
    private QRCode qrCode;

    private UserRepository userRepository;

    public MyController(PasswordEncoder passwordEncoder,
                        QRCode qrCode, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.qrCode = qrCode;
        this.userRepository = userRepository;
    }


    @GetMapping("/user/home")
    public String homePage () {
        return "home";
    }

    @GetMapping("/admin/home")
    public String adminHome () {
        return "adminHome";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "registerPage";
    }

    @PostMapping("/register")
    public String submitForm(@ModelAttribute("user") UserDTO userDTO, Model model) {
        UserDetails toRegister = User.builder()
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .username(userDTO.getEmail())
                .roles("USER")
                .build();
        inMemoryUserDetailsManager.createUser(toRegister);
        model.addAttribute("user", userDTO);
        return "result";
    }

    @GetMapping("/login")
    public String loginScreen(){

        return "login";
    }

}
