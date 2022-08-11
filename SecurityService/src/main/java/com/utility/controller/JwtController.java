package com.utility.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utility.config.CustomUserDetailsService;
import com.utility.config.JwtUtil;
import com.utility.entity.User;
import com.utility.model.JwtRequest;
import com.utility.model.JwtResponse;
import com.utility.service.UserService;

@RestController
@RequestMapping("/api/secure")
@CrossOrigin
public class JwtController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;
	@PostMapping("/token")
	public ResponseEntity generateToken(@RequestBody JwtRequest jwtRequest) throws Exception{
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
		}
		catch (UsernameNotFoundException  | BadCredentialsException e) {
			throw new Exception("Bad Credentials");
		}
		
	UserDetails userDetails=this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
	String token=	this.jwtUtil.generateToken(userDetails);
	HttpHeaders htt=new HttpHeaders();
	htt.set("Authorization" , token);
//	htt.set("Access-Control-Allow-Origin", "*");
	return  ResponseEntity.ok().headers(htt).body(token);
	
	}
	@GetMapping("/getuser")
	public User  getUser(@RequestHeader(value = "Authorization") String auth) {
	String	Username=jwtUtil.getUsernameFromToken(auth.substring(7));
		return userService.findByUsername(Username);
	}
	
	@GetMapping("/about")
	public String  about() {
		return "about";
	}
}
