package edu.vanderbilt.cqs.dao;

import edu.vanderbilt.cqs.bean.SystemOption;


public interface SystemOptionDAO extends GenericDAO<SystemOption, Long> {
	int getLimitUserCount();

	int getCloseRegistrationHour();
	
	void saveLimitUserCount(int count);
	
	void saveCloseRegistrationHour(int hour);
}
