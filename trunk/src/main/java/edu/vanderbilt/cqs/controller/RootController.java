package edu.vanderbilt.cqs.controller;

import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import edu.vanderbilt.cqs.Role;
import edu.vanderbilt.cqs.bean.SpringSecurityUser;

@Controller
public class RootController {
	private SpringSecurityUser guest = new SpringSecurityUser(0L, Role.NONE,
			"guest", "", false, false, false, false,
			new ArrayList<GrantedAuthority>());

	protected SpringSecurityUser currentUser() {
		Object obj = SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (obj instanceof SpringSecurityUser) {
			return (SpringSecurityUser) obj;
		} else {
			return guest;
		}
	}
}
