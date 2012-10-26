package edu.vanderbilt.cqs.context;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import edu.vanderbilt.cqs.Holiday;
import edu.vanderbilt.cqs.RegistrationType;
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
			addUser("quanhu.sheng@vanderbilt.edu", "cqs", Role.ADMIN);
			addUser("yan.guo@vanderbilt.edu", "cqs", Role.MANAGER);
			addUser("fei.ye@vanderbilt.edu", "cqs", Role.MANAGER);
		}

		if (!service.hasComingScheduleDay()) {
			Calendar day = Calendar.getInstance();

			day.set(2012, 10, 13, 9, 0, 0);
			// day.set(2012, 9, 2, 9, 0, 0);

			/*
			 * Calendar now = Calendar.getInstance(); now.setTime(new Date());
			 * while (day.before(now)) { day.add(Calendar.DAY_OF_YEAR, 7); }
			 */
			for (int j = 0; j < 100; j++) {
				if (!Holiday.isHoliday(day)) {
					ScheduleDay sday = new ScheduleDay();
					sday.setScheduleDate(day.getTime());
					service.addScheduleDay(sday);
				}

				/*
				 * int userCount = Math.min(15, RandomUtils.nextInt(20)); for
				 * (int i = 0; i < userCount; i++) { addScheduleUser(sday,
				 * "temp" + String.valueOf(i + 1) + "@vanderbilt.edu", "temp" +
				 * String.valueOf(i + 1), "user", "fake department"); }
				 */

				day.add(Calendar.DAY_OF_YEAR, 7);
			}
		}
	}

	protected User addUser(String email, String password, Integer permission) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(Utils.md5(password));
		user.setCreateDate(new Date());
		user.setRole(permission);

		service.addUser(user);

		return user;
	}

	protected ScheduleUser addScheduleUser(ScheduleDay aday, String email,
			String firstName, String lastName, String department) {
		ScheduleUser result = new ScheduleUser();
		result.setDay(aday);
		result.setEmail(email);
		result.setFirstname(firstName);
		result.setLastname(lastName);
		result.setDepartment(department);
		result.setRegType(RegistrationType.rtOnsite);
		service.addScheduleUser(result);
		return result;
	}
}
