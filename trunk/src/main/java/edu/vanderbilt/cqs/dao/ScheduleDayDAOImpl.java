package edu.vanderbilt.cqs.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import edu.vanderbilt.cqs.Utils;
import edu.vanderbilt.cqs.bean.ScheduleDay;

@Repository
public class ScheduleDayDAOImpl extends GenericDAOImpl<ScheduleDay, Long>
		implements ScheduleDayDAO {

	@Override
	public boolean hasComingScheduleDay() {
		return listComingScheduleDay().size() > 0;
	}

	@Override
	public ScheduleDay addNextScheduleDay(Date fromDay, int dayOfWeek) {
		Date nextDay = Utils.getNextDay(fromDay, dayOfWeek);
		ScheduleDay result = new ScheduleDay();
		result.setScheduleDate(nextDay);
		save(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleDay> listComingScheduleDay() {
		Criteria criteria = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.gt("scheduleDate", new Date()))
				.addOrder(Order.asc("scheduleDate"));
		return (List<ScheduleDay>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleDay> listAllScheduleDay() {
		List<ScheduleDay> result = (List<ScheduleDay>) getSession()
				.createCriteria(getPersistentClass())
				.add(Restrictions.gt("scheduleDate", new Date()))
				.addOrder(Order.asc("scheduleDate")).list();

		List<ScheduleDay> passed = (List<ScheduleDay>) getSession()
				.createCriteria(getPersistentClass())
				.add(Restrictions.lt("scheduleDate", new Date()))
				.addOrder(Order.desc("scheduleDate")).list();

		result.addAll(passed);

		return result;
	}
}
