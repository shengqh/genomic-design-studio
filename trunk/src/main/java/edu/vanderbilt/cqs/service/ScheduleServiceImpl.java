package edu.vanderbilt.cqs.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.vanderbilt.cqs.Config;
import edu.vanderbilt.cqs.RegistrationType;
import edu.vanderbilt.cqs.bean.LogTrace;
import edu.vanderbilt.cqs.bean.ScheduleDay;
import edu.vanderbilt.cqs.bean.ScheduleUser;
import edu.vanderbilt.cqs.bean.User;
import edu.vanderbilt.cqs.dao.LogTraceDAO;
import edu.vanderbilt.cqs.dao.ScheduleDayDAO;
import edu.vanderbilt.cqs.dao.ScheduleUserDAO;
import edu.vanderbilt.cqs.dao.SystemOptionDAO;
import edu.vanderbilt.cqs.dao.UserDAO;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ScheduleUserDAO scheduleUserDAO;

	@Autowired
	private ScheduleDayDAO scheduleDayDAO;

	@Autowired
	private SystemOptionDAO systemOptionDAO;

	@Autowired
	private LogTraceDAO logTraceDAO;

	@Transactional
	@Override
	public void addUser(User user) {
		userDAO.save(user);
	}

	@Transactional
	@Override
	public void removeUser(User user) {
		userDAO.delete(user);
	}

	@Transactional
	@Override
	public boolean hasUser() {
		return !userDAO.isEmpty();
	}

	@Transactional
	@Override
	public User findUser(Long id) {
		return userDAO.findById(id, false);
	}

	@Transactional
	@Override
	public User findUserByEmail(String email) {
		return userDAO.findByEmail(email);
	}

	@Transactional
	@Override
	public boolean hasUser(Long id) {
		return userDAO.hasUser(id);
	}

	@Transactional
	@Override
	public void updatePassword(Long id, String newPassword) {
		userDAO.updatePassword(id, newPassword);
	}

	@Transactional
	@Override
	public List<User> listValidUser() {
		return userDAO.listValidUser();
	}

	@Transactional
	@Override
	public List<User> listValidUser(Integer role) {
		return userDAO.listValidUser(role);
	}

	@Transactional
	@Override
	public List<User> listInvalidUser() {
		return userDAO.listInvalidUser();
	}

	@Transactional
	@Override
	public void updateUser(User user) {
		userDAO.update(user);
	}

	@Transactional
	@Override
	public ScheduleDay findScheduleDay(Long id) {
		return scheduleDayDAO.findById(id, false);
	}

	@Transactional
	@Override
	public void addScheduleDay(ScheduleDay obj) {
		scheduleDayDAO.save(obj);
	}

	@Transactional
	@Override
	public void updateScheduleDay(ScheduleDay obj) {
		scheduleDayDAO.update(obj);
	}

	@Transactional
	@Override
	public void removeScheduleDay(ScheduleDay day) {
		scheduleDayDAO.delete(day);
	}

	@Transactional
	@Override
	public ScheduleUser findScheduleUser(Long id) {
		return scheduleUserDAO.findById(id, false);
	}

	@Transactional
	@Override
	public ScheduleUser findScheduleUser(Long dayid, String email) {
		return scheduleUserDAO.findScheduleUser(dayid, email);
	}

	@Transactional
	@Override
	public void addScheduleUser(ScheduleUser obj) {
		scheduleUserDAO.save(obj);
		if (obj.getRegType() == RegistrationType.rtOnsite) {
			obj.getDay().setRegisteredNumber(
					obj.getDay().getRegisteredNumber() + 1);
			scheduleDayDAO.update(obj.getDay());
		}
	}

	@Transactional
	@Override
	public void updateScheduleUser(ScheduleUser obj) {
		scheduleUserDAO.update(obj);
	}

	@Transactional
	@Override
	public void removeScheduleUser(ScheduleUser user) {
		if (user.getRegType() == RegistrationType.rtOnsite) {
			ScheduleDay day = user.getDay();
			day.setRegisteredNumber(day.getRegisteredNumber() - 1);
			scheduleDayDAO.update(day);
		}
		scheduleUserDAO.delete(user);
	}

	@Transactional
	@Override
	public List<ScheduleDay> listScheduleDay() {
		return scheduleDayDAO.findAll();
	}

	@Transactional
	@Override
	public boolean hasComingScheduleDay() {
		return scheduleDayDAO.hasComingScheduleDay();
	}

	@Transactional
	@Override
	public ScheduleDay addNextScheduleDay(Date fromDay, int dayOfWeek) {
		return scheduleDayDAO.addNextScheduleDay(fromDay, dayOfWeek);
	}

	@Transactional
	@Override
	public List<ScheduleDay> listComingScheduleDay() {
		return scheduleDayDAO.listComingScheduleDay();
	}

	@Transactional
	@Override
	public void addLogTrace(LogTrace log) {
		logTraceDAO.save(log);
	}

	@Transactional
	@Override
	public void loadOption() {
		Config.LimitCountPerDay = systemOptionDAO.getLimitUserCount();
		Config.CloseRegistrationHour = systemOptionDAO.getCloseRegistrationHour();
	}

	@Transactional
	@Override
	public List<LogTrace> listLog() {
		return logTraceDAO.listAll();
	}

	@Transactional
	@Override
	public void setLimitUserCount(int count) {
		systemOptionDAO.saveLimitUserCount(count);
		Config.LimitCountPerDay = count;
	}

	@Transactional
	@Override
	public void setCloseRegistrationHour(int hour) {
		systemOptionDAO.saveCloseRegistrationHour(hour);
		Config.CloseRegistrationHour = hour;
	}

	@Transactional
	@Override
	public List<ScheduleDay> listPassedScheduleDay() {
		return scheduleDayDAO.listPassedScheduleDay();
	}
}
