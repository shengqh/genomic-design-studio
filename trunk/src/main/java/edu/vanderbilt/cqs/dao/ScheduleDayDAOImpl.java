package edu.vanderbilt.cqs.dao;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import edu.vanderbilt.cqs.Utils;
import edu.vanderbilt.cqs.bean.ScheduleDay;

@Repository
public class ScheduleDayDAOImpl extends GenericDAOImpl<ScheduleDay, Long> implements ScheduleDayDAO {

	@Override
	public boolean hasComingScheduleDay() {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.lt("scheduleDate", new Date()));
		criteria.setProjection(Projections.rowCount());
		return ((Long) criteria.list().get(0)).longValue() > 0;
	}

	@Override
	public ScheduleDay addNextScheduleDay(Date fromDay, int dayOfWeek) {
		Date nextDay = Utils.getNextDay(fromDay, dayOfWeek);
		ScheduleDay result = new ScheduleDay();
		result.setScheduleDate(nextDay);
		save(result);
		return result;
	}
}
