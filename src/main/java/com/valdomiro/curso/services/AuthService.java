package com.valdomiro.curso.services;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valdomiro.curso.dto.CredentialsDTO;
import com.valdomiro.curso.dto.TokenDTO;
import com.valdomiro.curso.entities.Order;
import com.valdomiro.curso.entities.User;
import com.valdomiro.curso.repositories.UserRepository;
import com.valdomiro.curso.security.JWTUtil;
import com.valdomiro.curso.services.exceptions.JWTAuthenticationException;
import com.valdomiro.curso.services.exceptions.JWTAuthorizationException;
import com.valdomiro.curso.services.exceptions.ResourceNotFoundException;

@Service
public class AuthService {
	
	private static final Logger LOG= LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private AuthenticationManager authenicationManager;
	
	@Autowired
	private  JWTUtil jwtUtil;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncode;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional(readOnly=true)
	public TokenDTO authenticate(CredentialsDTO dto) {
		
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword());
			authenicationManager.authenticate(authToken);
			String token= jwtUtil.generateToken(dto.getEmail());
			return new TokenDTO(dto.getEmail(),token);
		}catch(AuthenticationException e) {
			throw new JWTAuthenticationException("Bad credentials");
		}
	}
	public User authenticated() {
		try{
			
		UserDetails userDetails= (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepository.findByEmail(userDetails.getUsername());
		
		}catch(Exception e) {
			throw new JWTAuthorizationException("Acess denied");
		}
	}
	
	public void validateSelfOrAdmin(Long userId) {
		User user= authenticated();
		if(user == null || (!user.getId().equals(userId)) && !user.hasRole("ROLE_ADMIN")) {
			throw new JWTAuthorizationException("Acess denied");
		}
	}
	
	
	public void validadeOwnOrderOrAdmin(Order order) {
		User user= authenticated();
		if(user == null || (!user.getId().equals(order.getId())) && !user.hasRole("ROLE_ADMIN")) {
			throw new JWTAuthorizationException("Acess denied");
		}
	}
	public TokenDTO refreshToken() {
		User user= authenticated();
		return new TokenDTO(user.getEmail(),jwtUtil.generateToken(user.getEmail()));
		
	}
	
	@Transactional
	public void sendNewPassword(String email) {
		User user= userRepository.findByEmail(email);
		if( user == null) {
			throw new ResourceNotFoundException("Email not found");
		}
		
	    String newPass= newPassword();
	    user.setPassword(passwordEncode.encode(newPass));
	    userRepository.save(user);
	    
	    LOG.info("New Password "+newPass);
	    
	    
	}
	
	private String newPassword() {
		char[] vect= new char[10];
		for(int i=0;i<10;i++) {
			vect[i]= randomChar();
		}
		
		return new String(vect);
	}
	private char randomChar() {
		Random rand = new Random();
		int opt = rand.nextInt(3);
		if(opt==0) {
			
			return (char) (rand.nextInt(10) + 48);
		}else if(opt==1) {
			return (char) (rand.nextInt(26) + 65);
		}else {
			return (char) (rand.nextInt(26) + 97);
		}
		
		
	}
}
