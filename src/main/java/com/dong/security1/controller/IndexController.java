package com.dong.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dong.security1.config.auth.PrincipalDetails;
import com.dong.security1.model.User;
import com.dong.security1.repository.UserRepository;

@Controller
public class IndexController  {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails) {
		System.out.println("================IndexController::loginTest");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println(principalDetails.toString());
		System.out.println(userDetails.toString());
		
		
		return "test";
		
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String loginoauthTest(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User) {
		System.out.println("================IndexController::loginTest");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println(oauth2User.getAttributes());
		System.out.println(oAuth2User.getAttributes());
		
		
		return "test oauth";
		
	}
	
	@GetMapping({"","/"})
	public String index() {
		//머스테치 기본폴더 src/main/resources/
		//prefix: /templates/ 기본경로이기 때문에 생략가능
		//suffix: .mustache
		return "index";
	}
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println(principalDetails);
		return "user";
	}
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	@GetMapping("/joinForm")
	public  String joinForm() {
		return "joinForm";
	}
	@PostMapping("/join")
	public String join(User user) {
		System.out.println("================="+user.toString());
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		userRepository.save(user);
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody  String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
	@GetMapping("/data")
	public @ResponseBody  String data() {
		return "데이터정보";
	}
	
}
