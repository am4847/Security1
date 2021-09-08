package com.dong.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.dong.security1.config.auth.PrincipalDetails;
import com.dong.security1.config.oauth.provider.FacebookUserInfo2;
import com.dong.security1.config.oauth.provider.GoogleUserInfo;
import com.dong.security1.config.oauth.provider.OAuth2UserInfo;
import com.dong.security1.model.User;
import com.dong.security1.repository.UserRepository;

@Service
public class principalOauthUserService extends DefaultOAuth2UserService {
	
	
	private final String defaultPasswordKey = "default1234";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("=================:"+userRequest);
		OAuth2User oauth2User = super.loadUser(userRequest);
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글로그인요청");
			oAuth2UserInfo= new GoogleUserInfo(oauth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			oAuth2UserInfo = new FacebookUserInfo2(oauth2User.getAttributes());
			System.out.println("페이스북로그인요청");
		}
		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider+"_"+providerId;
		String password = bCryptPasswordEncoder.encode(defaultPasswordKey) ;
		String email = oAuth2UserInfo.getEmail();
		String role = provider+"_OAUTHUSER";
		
		User user =   userRepository.findByUsername(username);
		if(user == null) {
			user =User.builder()
				.username(username)
				.password(password)
				.email(email)
				.role(role)
				.build();
			userRepository.save(user);
		}
		return new PrincipalDetails(user,oauth2User.getAttributes());
	}
	
		
	
}
