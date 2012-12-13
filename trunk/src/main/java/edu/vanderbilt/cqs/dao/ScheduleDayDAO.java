package edu.vanderbilt.cqs.dao;

import java.util.Date;
import java.util.List;

import edu.vanderbilt.cqs.bean.ScheduleDay;

public interface ScheduleDayDAO extends GenericDAO<ScheduleDay, Long> {
	boolean hasComingScheduleDay();

	ScheduleDay addNextScheduleDay(Date fromDay, int dayOfWeek);

	List<ScheduleDay> listComingScheduleDay();
	
	List<ScheduleDay> listAllScheduleDay();

	List<ScheduleDay> listPassedScheduleDay();
}
