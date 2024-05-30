package se.norrwing.itslek4demo.controller;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.norrwing.itslek4demo.DTO.UserDTO;
import se.norrwing.itslek4demo.model.User;
import se.norrwing.itslek4demo.repository.UserRepository;
import se.norrwing.itslek4demo.temp.QRCode;

@Controller
public class PageController {

    private final PasswordEncoder passwordEncoder;

    private final QRCode qrCode;

    private final UserRepository userRepository;

    public PageController(
            PasswordEncoder passwordEncoder,
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
        User user = new User(userDTO.getEmail(),passwordEncoder.encode(userDTO.getPassword()));
        /*UserDetails toRegister = User.builder()
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .username(userDTO.getEmail())
                .roles("USER")
                .build();
        inMemoryUserDetailsManager.createUser(toRegister);
         */
        userRepository.save(user);
        model.addAttribute("qrcode"
                , qrCode.dataUrl(user));
        model.addAttribute("user", userDTO);
        return "QRCodePage";
    }


    @GetMapping("/login")
    public String loginScreen(){

        return "login";
    }

}
