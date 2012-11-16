package edu.vanderbilt.cqs;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Role {
	public static final Integer ADMIN = 1000;
	public static final Integer VANGARD = 100;
	public static final Integer USER = 1;
	public static final Integer NONE = 0;
	
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_VANGARD = "ROLE_VANGARD";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_NONE = "ROLE_NONE";
	
	private static Map<Integer, String> roleMap;

	public static Map<Integer, String> getRoleMap() {
		if (roleMap == null) {
			roleMap = new LinkedHashMap<Integer, String>();
			roleMap.put(USER, ROLE_USER);
			roleMap.put(VANGARD, ROLE_VANGARD);
			roleMap.put(ADMIN, ROLE_ADMIN);
		}
		return roleMap;
	}
}
