package edu.vanderbilt.cqs.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.vanderbilt.cqs.Config;
import edu.vanderbilt.cqs.RegistrationType;
import edu.vanderbilt.cqs.Role;
import edu.vanderbilt.cqs.bean.ScheduleDay;
import edu.vanderbilt.cqs.bean.ScheduleUser;
import edu.vanderbilt.cqs.form.ScheduleUserForm;

@Controller
public class ScheduleController extends RootController {
	@Autowired
	private Validator validator;

	@RequestMapping("/showlist")
	public String listSchedule(
			@RequestParam(value = "message", required = false) String message,
			ModelMap model) {
		List<ScheduleDay> days;
		days = service.listScheduleDay();
		for (ScheduleDay day : days) {
			day.setCanRegister(canRegister(day));
		}
		if (message != null) {
			model.put("message", message);
		}
		model.put("days", days);
		return "/schedule/list";
	}

	@RequestMapping("/showday")
	public String showScheduleDay(@RequestParam("dayid") Long dayid,
			ModelMap model) {
		ScheduleDay day = service.findScheduleDay(dayid);
		if (day == null) {
			return "redirect:/showlist?message=Day with id " + dayid.toString()
					+ " not exists";
		}

		model.addAttribute("day", day);
		return "/schedule/show";
	}

	@RequestMapping("/deleteday")
	@Secured(Role.ROLE_ADMIN)
	public String deleteScheduleDay(@RequestParam("dayid") Long dayid,
			ModelMap model) {
		ScheduleDay day = service.findScheduleDay(dayid);
		if (day != null) {
			service.removeScheduleDay(day);
			addUserLogInfo("delete schedule day + " + day.getDate());
		}
		return "redirect:/showlist";
	}

	private boolean canRegister(ScheduleDay day) {
		if (currentUser().getRole() >= Role.ADMIN) {
			return true;
		}

		if (!Config.hasPassed(day.getScheduleDate())
				&& day.getRegisteredNumber() < Config.LimitCountPerDay) {
			return true;
		}

		return false;
	}

	@RequestMapping("/addscheduleuser")
	public String addScheduleUser(@RequestParam("dayid") Long dayid,
			ModelMap model) {
		ScheduleDay day = service.findScheduleDay(dayid);
		if (day == null) {
			String error = "Day with id " + dayid.toString() + " not exists";
			return "redirect:/showlist?message=" + error;
		} else if (!canRegister(day)) {
			String error = day.getDate() + " can not register anymore";
			return "redirect:/showlist?message=" + error;
		}

		ScheduleUserForm form = new ScheduleUserForm();
		form.setDay(day.getDate());
		form.setDayId(day.getId());
		form.setRegType(RegistrationType.rtOnsite);

		model.put("scheduleUserForm", form);

		addUserLogInfo("try to add schedule user", false);

		return "/schedule/adduser";
	}

	@RequestMapping(value = "/savescheduleuser", method = RequestMethod.POST)
	public String saveScheduleUser(
			@ModelAttribute("scheduleUserForm") @Valid ScheduleUserForm form,
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("scheduleUserForm", form);
			return "/schedule/adduser";
		}

		ScheduleDay day = service.findScheduleDay(form.getDayId());
		if (day == null) {
			String error = "Day with id " + form.getDayId().toString()
					+ " not exists";
			return "redirect:/showlist?message=" + error;
		} else if (!canRegister(day)) {
			String error = day.getDate() + " can not register anymore";
			return "redirect:/showlist?message=" + error;
		}

		ScheduleUser user = new ScheduleUser();
		BeanUtils.copyProperties(form, user);
		user.setEmail(user.getEmail().toLowerCase());
		user.setDay(day);
		user.setRegisterTime(new Date());
		user.setIpaddress(getIpAddress());

		ScheduleUser oldUser = service.findScheduleUser(day.getId(),
				user.getEmail());
		if (oldUser != null) {
			String error = "User " + user.getEmail()
					+ " has already registered for genomic design studio at "
					+ day.getDate();
			return "redirect:/showlist?message=" + error;
		}

		service.addScheduleUser(user);

		addUserLogInfo("add user " + user.getEmail() + " to " + day.getDate());

		return getDayRedirect(day.getId());
	}

	@RequestMapping("/checkinscheduleuser")
	@Secured(Role.ROLE_VANGARD)
	public String checkinScheduleUser(@RequestParam("dayid") Long dayid,
			@RequestParam("userid") Long userid, ModelMap model) {
		ScheduleUser oldUser = service.findScheduleUser(userid);
		if (oldUser != null) {
			oldUser.setCheckIn(true);
			service.updateScheduleUser(oldUser);
			ScheduleDay day = service.findScheduleDay(dayid);
			if (day != null) {
				addUserLogInfo("checkin user " + oldUser.getEmail() + " for "
						+ day.getDate());
			}
		}
		return getDayRedirect(dayid);
	}

	@RequestMapping("/uncheckinscheduleuser")
	@Secured(Role.ROLE_VANGARD)
	public String uncheckinScheduleUser(@RequestParam("dayid") Long dayid,
			@RequestParam("userid") Long userid, ModelMap model) {
		ScheduleUser oldUser = service.findScheduleUser(userid);
		if (oldUser != null) {
			oldUser.setCheckIn(false);
			service.updateScheduleUser(oldUser);
			ScheduleDay day = service.findScheduleDay(dayid);
			if (day != null) {
				addUserLogInfo("uncheckin user " + oldUser.getEmail() + " for "
						+ day.getDate());
			}
		}
		return getDayRedirect(dayid);
	}

	@RequestMapping("/deletescheduleuser")
	@Secured(Role.ROLE_VANGARD)
	public String deleteScheduleUser(@RequestParam("dayid") Long dayid,
			@RequestParam("userid") Long userid, ModelMap model) {
		ScheduleUser oldUser = service.findScheduleUser(userid);
		if (oldUser != null) {
			service.removeScheduleUser(oldUser);
			ScheduleDay day = service.findScheduleDay(dayid);
			if (day != null) {
				addUserLogInfo("delete user " + oldUser.getEmail()
						+ " from " + day.getDate());
			}
		}
		return getDayRedirect(dayid);
	}

	private String getDayRedirect(Long dayid) {
		return "redirect:/showday?dayid=" + dayid.toString();
	}

	@RequestMapping("/showlog")
	@Secured(Role.ROLE_VANGARD)
	public String listLog(ModelMap model) {
		model.put("logs", service.listLog());
		return "/log/list";
	}
}