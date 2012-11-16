package edu.vanderbilt.cqs.controller;

import java.util.ArrayList;
import java.util.Calendar;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.vanderbilt.cqs.Role;
import edu.vanderbilt.cqs.bean.LogTrace;
import edu.vanderbilt.cqs.bean.SpringSecurityUser;
import edu.vanderbilt.cqs.service.ScheduleService;

@Controller
public class RootController {
	private SpringSecurityUser guest = new SpringSecurityUser(0L, Role.NONE,
			"guest", "", false, false, false, false,
			new ArrayList<GrantedAuthority>());

	protected static final Logger logger = Logger
			.getLogger(UserController.class);

	@Autowired
	protected ScheduleService service;

	protected SpringSecurityUser currentUser() {
		Object obj = SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (obj instanceof SpringSecurityUser) {
			return (SpringSecurityUser) obj;
		} else {
			return guest;
		}
	}

	protected String getIpAddress() {
		return ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest().getRemoteAddr();
	}

	private void addLogTrace(String username, Logger.Level level, String action) {
		LogTrace log = new LogTrace();
		log.setLogDate(Calendar.getInstance().getTime());
		log.setUser(username);
		log.setAction(action);
		log.setIpaddress(getIpAddress());
		log.setLevel(level.ordinal());
		service.addLogTrace(log);
	}

	protected void addUserLogInfo(String action, boolean addtodatabase) {
		logger.info(currentUser().getUsername() + ": " + action);
		if(addtodatabase){
			addLogTrace(currentUser().getUsername(), Logger.Level.INFO, action);
		}
	}
	protected void addUserLogInfo(String action) {
		addUserLogInfo(action, true);
	}

	protected void addUserLogError(String action, boolean addtodatabase) {
		logger.error(currentUser().getUsername() + ": " + action);
		addLogTrace(currentUser().getUsername(), Logger.Level.ERROR, action);
	}

	protected void addUserLogError(String action) {
		addUserLogError(action, true);
	}
	
	protected void addSystemLogInfo(String action) {
		logger.info("system : " + action);
		addLogTrace("system", Logger.Level.INFO, action);
	}

	protected void addSystemLogError(String action) {
		logger.error("system : " + action);
		addLogTrace("system", Logger.Level.ERROR, action);
	}

}
