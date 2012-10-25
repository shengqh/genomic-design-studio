package edu.vanderbilt.cqs.context;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import edu.vanderbilt.cqs.Role;
import edu.vanderbilt.cqs.Utils;
import edu.vanderbilt.cqs.bean.ScheduleDay;
import edu.vanderbilt.cqs.bean.ScheduleUser;
import edu.vanderbilt.cqs.bean.User;
import edu.vanderbilt.cqs.service.ScheduleService;

@Repository
public class ApplicationListenerImpl implements
		ApplicationListener<ContextRefreshedEvent> {
	@Resource
	private ScheduleService service;
	private boolean bFirst = true;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		if (bFirst) {
			bFirst = false;
			initializeDatabase();
		}
	}

	private void initializeDatabase() {
		if (!service.hasUser()) {
			addUser("yu.shyr@vanderbilt.edu", "cqs", Role.ADMIN);
			addUser("yan.guo@vanderbilt.edu", "cqs", Role.ADMIN);
			addUser("fei.ye@vanderbilt.edu", "cqs", Role.ADMIN);
			
			Date day1 = Utils.getNextDay(null, Calendar.TUESDAY);
			ScheduleDay sday1 = addDay(day1);
			Date day2 = Utils.getNextDay(day1, Calendar.TUESDAY);
			ScheduleDay sday2 = addDay(day2);
			
			addScheduleUser(sday1, "quanhu.sheng@vanderbilt.edu", "Quanhu", "Sheng");
			addScheduleUser(sday1, "shengqh@gmail.com", "Tiger", "Sheng");
			addScheduleUser(sday2, "shengqh@gmail.com", "Tiger", "Sheng");
			addScheduleUser(sday2, "quanhu.sheng@vanderbilt.edu", "Quanhu", "Sheng");
		}
	}

	private User addUser(String email, String password, Integer permission) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(Utils.md5(password));
		user.setCreateDate(new Date());
		user.setRole(permission);

		service.addUser(user);

		return user;
	}

	private ScheduleDay addDay(Date adate) {
		ScheduleDay result = new ScheduleDay();
		result.setScheduleDate(adate);
		service.addScheduleDay(result);
		return result;
	}

	private ScheduleUser addScheduleUser(ScheduleDay aday, String email, String firstName, String lastName) {
		ScheduleUser result = new ScheduleUser();
		result.setDay(aday);
		result.setEmail(email);
		result.setFirstname(firstName);
		result.setLastname(lastName);
		service.addScheduleUser(result);
		return result;
	}
}
