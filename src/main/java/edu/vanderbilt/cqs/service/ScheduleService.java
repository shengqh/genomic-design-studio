package edu.vanderbilt.cqs.service;

import java.util.Date;
import java.util.List;

import edu.vanderbilt.cqs.bean.ScheduleDay;
import edu.vanderbilt.cqs.bean.ScheduleUser;
import edu.vanderbilt.cqs.bean.User;

public interface ScheduleService {
	// User
	void addUser(User user);

	void updateUser(User user);

	void removeUser(Long id);

	User findUser(Long id);

	User findUserByEmail(String email);

	// ScheduleDay
	ScheduleDay findScheduleDay(Long id);

	void addScheduleDay(ScheduleDay project);

	void updateScheduleDay(ScheduleDay project);

	void removeScheduleDay(Long id);

	List<ScheduleDay> listScheduleDay();
	
	boolean hasComingScheduleDay();
	
	ScheduleDay addNextScheduleDay(Date fromDay, int dayOfWeek);

	// ScheduleUser
	ScheduleUser findScheduleUser(Long id);

	void addScheduleUser(ScheduleUser project);

	void updateScheduleUser(ScheduleUser project);

	void removeScheduleUser(Long id);

	// Other
	boolean hasUser();

	boolean hasUser(Long id);

	void updatePassword(Long id, String newPassword);

	List<User> listValidUser();

	List<User> listValidUser(Integer role);

	List<User> listInvalidUser();
}
