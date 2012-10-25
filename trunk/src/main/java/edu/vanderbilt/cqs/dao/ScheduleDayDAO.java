package edu.vanderbilt.cqs.dao;

import java.util.Date;

import edu.vanderbilt.cqs.bean.ScheduleDay;

public interface ScheduleDayDAO extends GenericDAO<ScheduleDay, Long> {
	boolean hasComingScheduleDay();

	ScheduleDay addNextScheduleDay(Date fromDay, int dayOfWeek);
}
