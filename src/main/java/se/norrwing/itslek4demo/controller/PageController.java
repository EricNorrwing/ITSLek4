package se.norrwing.itslek4demo.controller;


import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    @GetMapping("/Success")
    public String successPage () {
        return "success";
    }

    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute("user") UserDTO userDTO,
                               BindingResult result, Model model){

        if(result.hasErrors()){
            System.out.println("ERRORS");
            return "registerPage";
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole("USER");
        user.setSecret(Base32.random());
        userRepository.save(user);
        model.addAttribute("qrcode", qrCode.dataUrl(user));
        return "QRCodePage";
    }


    @GetMapping("/login")
    public String loginScreen(){
        return "login";
    }

}
