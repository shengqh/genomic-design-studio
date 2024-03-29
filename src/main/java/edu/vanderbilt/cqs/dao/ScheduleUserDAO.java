package edu.vanderbilt.cqs.dao;

import edu.vanderbilt.cqs.bean.ScheduleUser;

public interface ScheduleUserDAO extends GenericDAO<ScheduleUser, Long> {
	ScheduleUser findScheduleUser(Long dayid, String email);
}
