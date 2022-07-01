package de.wigeogis.wigeosocial.annotationserver.controller;


import de.wigeogis.wigeosocial.annotationserver.annotation.model.User;
import de.wigeogis.wigeosocial.annotationserver.annotation.service.UserService;
import de.wigeogis.wigeosocial.annotationserver.security.JwtResponse;
import de.wigeogis.wigeosocial.annotationserver.security.LoginRequest;
import de.wigeogis.wigeosocial.annotationserver.security.jwt.JwtUtils;
import de.wigeogis.wigeosocial.annotationserver.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserService userManagerService;


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;



    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {

        User user = userManagerService.getUserByUsername(loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/auto_login")
    @ResponseBody
    public ResponseEntity<?> autoLogin(@RequestBody LoginRequest loginRequest) throws Exception {

        if(userManagerService.getUserByUsername(loginRequest.getUsername()) == null) {
            User newUser = new User();
            newUser.setUsername(loginRequest.getUsername());
            newUser.setPassword(encoder.encode(loginRequest.getPassword()));
            newUser.setShowInstruction(true);
            newUser.setProlific(true);
            newUser.setTasks(0);
            userManagerService.createNewUser(newUser);
        }

        User user = userManagerService.getUserByUsername(loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    /*@RequestMapping(value = "register/{username}/{pass}", method = RequestMethod.GET)
    @ResponseBody
    public String registerNewUser(@PathVariable(value = "username",required = true) String username,
                          @PathVariable(value = "pass",required = true) String pass) throws Exception {

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(pass));
        userManagerService.createNewUser(user);
        return "ok";
    }*/


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            request.logout();
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return ResponseEntity.ok(new JwtResponse("",
                    null,
                    "",
                    null));
        }else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User is not logged out successfully");
        }
    }

}
