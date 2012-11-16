package edu.vanderbilt.cqs.dao;

import org.springframework.stereotype.Repository;

import edu.vanderbilt.cqs.Config;
import edu.vanderbilt.cqs.bean.SystemOption;

@Repository
public class SystemOptionDAOImpl extends GenericDAOImpl<SystemOption, Long>
		implements SystemOptionDAO {
	@Override
	public int getLimitUserCount() {
		SystemOption option = findInstanceByName("optionKey", "LimitUserCount");
		if (option == null) {
			return Config.DefaultLimitCountPerDay;
		}
		return Integer.parseInt(option.getOptionValue());
	}

	@Override
	public int getCloseRegistrationHour() {
		SystemOption option = findInstanceByName("optionKey",
				"CloseRegistrationHour");
		if (option == null) {
			return Config.DefaultCloseRegistrationHour;
		}
		return Integer.parseInt(option.getOptionValue());
	}

	public void saveOption(String key, String value) {
		SystemOption option = findInstanceByName("optionKey", key);
		if (option == null) {
			option = new SystemOption();
			option.setOptionKey(key);
		}
		option.setOptionValue(value);
		saveOrUpdate(option);
	}

	@Override
	public void saveLimitUserCount(int count) {
		saveOption("LimitUserCount", String.valueOf(count));
		Config.LimitCountPerDay = count;
	}

	@Override
	public void saveCloseRegistrationHour(int hour) {
		saveOption("CloseRegistrationHour", String.valueOf(hour));
		Config.CloseRegistrationHour = hour;
	}

}
