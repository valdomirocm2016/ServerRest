package com.valdomiro.curso.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenicationManager;
	
	@Autowired
	private  JWTUtil jwtUtil;
	
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
}
