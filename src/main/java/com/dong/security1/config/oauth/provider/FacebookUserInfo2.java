package com.dong.security1.config.oauth.provider;

import java.util.Map;

public class FacebookUserInfo2 implements OAuth2UserInfo {

	private Map<String, Object> attributes;

	public FacebookUserInfo2(Map<String, Object> attributes) {
				this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		// TODO Auto-generated method stub
		return (String) attributes.get("id");
	}

	@Override
	public String getProvider() {
		// TODO Auto-generated method stub
		return "FACEBOOK";
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return (String) attributes.get("email");
	}

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return (String) attributes.get("name");
	}

}
