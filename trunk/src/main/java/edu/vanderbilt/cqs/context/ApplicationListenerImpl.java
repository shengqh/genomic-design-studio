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
		}

		if (!service.hasComingScheduleDay()) {
			ScheduleDay sday1 = service.addNextScheduleDay(null,
					Calendar.TUESDAY);
			ScheduleDay sday2 = service.addNextScheduleDay(
					sday1.getScheduleDate(), Calendar.TUESDAY);

			for (int i = 0; i < 15; i++) {
				addScheduleUser(sday1, "email" + String.valueOf(i)
						+ "@vanderbilt.edu", "Temp" + String.valueOf(i), "User");
			}
			addScheduleUser(sday2, "shengqh@gmail.com", "Tiger", "Sheng");
			addScheduleUser(sday2, "quanhu.sheng@vanderbilt.edu", "Quanhu",
					"Sheng");
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

	private ScheduleUser addScheduleUser(ScheduleDay aday, String email,
			String firstName, String lastName) {
		ScheduleUser result = new ScheduleUser();
		result.setDay(aday);
		result.setEmail(email);
		result.setFirstname(firstName);
		result.setLastname(lastName);
		service.addScheduleUser(result);
		return result;
	}
}
