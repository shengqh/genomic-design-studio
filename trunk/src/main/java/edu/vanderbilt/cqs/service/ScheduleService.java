package edu.vanderbilt.cqs.service;

import java.util.Date;
import java.util.List;

import edu.vanderbilt.cqs.bean.LogTrace;
import edu.vanderbilt.cqs.bean.ScheduleDay;
import edu.vanderbilt.cqs.bean.ScheduleUser;
import edu.vanderbilt.cqs.bean.User;

public interface ScheduleService {
	// User
	void addUser(User user);

	void updateUser(User user);

	void removeUser(User user);

	User findUser(Long id);

	User findUserByEmail(String email);

	// ScheduleDay
	ScheduleDay findScheduleDay(Long id);

	void addScheduleDay(ScheduleDay day);

	void updateScheduleDay(ScheduleDay day);

	void removeScheduleDay(ScheduleDay day);

	List<ScheduleDay> listScheduleDay();

	List<ScheduleDay> listComingScheduleDay();

	boolean hasComingScheduleDay();

	ScheduleDay addNextScheduleDay(Date fromDay, int dayOfWeek);

	// ScheduleUser
	ScheduleUser findScheduleUser(Long id);

	void addScheduleUser(ScheduleUser user);

	void updateScheduleUser(ScheduleUser user);

	void removeScheduleUser(ScheduleUser user);

	ScheduleUser findScheduleUser(Long dayid, String email);

	// Other
	boolean hasUser();

	boolean hasUser(Long id);

	void updatePassword(Long id, String newPassword);

	List<User> listValidUser();

	List<User> listValidUser(Integer role);

	List<User> listInvalidUser();

	// SystemOption
	void loadOption();
	
	void setLimitUserCount(int count);
	
	void setCloseRegistrationHour(int hour);

	// Log
	void addLogTrace(LogTrace log);
	
	List<LogTrace> listLog();

	List<ScheduleDay> listPassedScheduleDay();
}
