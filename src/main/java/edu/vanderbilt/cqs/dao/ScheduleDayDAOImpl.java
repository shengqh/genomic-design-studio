package edu.vanderbilt.cqs.dao;

import org.springframework.stereotype.Repository;

import edu.vanderbilt.cqs.bean.ScheduleDay;

@Repository
public class ScheduleDayDAOImpl extends GenericDAOImpl<ScheduleDay, Long> implements ScheduleDayDAO {
}
