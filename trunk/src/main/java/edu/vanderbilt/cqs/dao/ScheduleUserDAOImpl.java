package edu.vanderbilt.cqs.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import edu.vanderbilt.cqs.bean.ScheduleUser;

@Repository
public class ScheduleUserDAOImpl extends GenericDAOImpl<ScheduleUser, Long>
		implements ScheduleUserDAO {

	@Override
	public ScheduleUser findScheduleUser(Long dayid, String email) {
		return (ScheduleUser) (getSession()
				.createCriteria(getPersistentClass())
				.add(Restrictions.eq("email", email))
				.add(Restrictions.eq("day.id", dayid)).uniqueResult());
	}
}
