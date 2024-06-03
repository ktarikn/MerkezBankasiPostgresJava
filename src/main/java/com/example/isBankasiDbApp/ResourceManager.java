package com.example.isBankasiDbApp;

import com.example.isBankasiDbApp.security.AuthenticationRequest;
import com.example.isBankasiDbApp.security.AuthenticationResponse;
import com.example.isBankasiDbApp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;



@RestController
public class ResourceManager{
    private DisplayFactory displays;
    public ResourceManager() {
        displays = new DisplayFactory();
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;



    @GetMapping("/rates")
    //rates with empty fields have empty fields set to 0
    public RateRecord[] home(){
        return displays.menu();
    }
    private void reList(Model model){
        model.addAttribute("dataList", displays.getRates());
    }


    @GetMapping("/rates/remove")
    public RateRecord[] delete(@RequestParam(value = "kod") String kod){
        return displays.deleteRate(kod); //deletes the rate given with code and returns the new menu

    }
    @GetMapping("/rates/post")
    public RateRecord[] post(@RequestParam(value = "kod") String kod, @RequestParam(value = "buy") float buy,
                               @RequestParam(value = "sell") float sell,
                               @RequestParam(value = "ebuy") float ebuy,
                               @RequestParam(value = "esell") float esell){
        //updates the rate given with code and returns the new menu
        return displays.updateRate(kod,buy,sell,ebuy,esell);

    }
    @GetMapping("/rates/update")
    public RateRecord[] update(@RequestParam(value = "kod") String kod, @RequestParam(value = "buy") float buy,
                               @RequestParam(value = "sell") float sell)
    {
        //updates but sets effective values to normal values
        return post(kod,buy,sell,buy,sell);
    }
    @GetMapping("/rates/create")
    public RateRecord[] create(@RequestParam(value = "kod") String kod, @RequestParam(value = "buy") float buy,
                              @RequestParam(value = "sell") float sell)
    {
        //adds rate but sets effective values to normal values
        return add(kod,buy,buy,sell,sell);
    }
    @GetMapping("/rates/add")
    public RateRecord[] add(@RequestParam(value = "kod") String kod, @RequestParam(value = "buy") float buy,
                             @RequestParam(value = "sell") float sell,
                             @RequestParam(value = "ebuy") float ebuy,
                             @RequestParam(value = "esell") float esell){
        //adds exchange rate and goes to menu
        return displays.addRate(kod,buy,sell,ebuy,esell);

    }
    @GetMapping("/rates/exchange")
        public RateRecord exchange(@RequestParam(value="from") String from, @RequestParam(value = "to") String to,
                                    @RequestParam(value="cash") float cash){
        return displays.exchange(from,to,cash);

    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {

            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @RequestMapping({"/hello"})
    public String hello(){
        return "hello";
    }

}