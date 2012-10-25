package edu.vanderbilt.cqs.controller;

import java.text.SimpleDateFormat;

import org.jboss.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.vanderbilt.cqs.bean.ScheduleDay;
import edu.vanderbilt.cqs.bean.ScheduleUser;
import edu.vanderbilt.cqs.form.ScheduleUserForm;
import edu.vanderbilt.cqs.service.ScheduleService;

@Controller
public class ScheduleController extends RootController {
	private static final Logger logger = Logger
			.getLogger(ScheduleController.class);

	@Autowired
	private ScheduleService service;

	@Autowired
	private Validator validator;

	@RequestMapping("/showlist")
	public String listSchedule(ModelMap model) {
		model.put("days", service.listScheduleDay());
		return "/schedule/list";
	}

	@RequestMapping("/showday")
	public String showScheduleDay(@RequestParam("dayid") Long dayid,
			ModelMap model) {
		ScheduleDay day = service.findScheduleDay(dayid);
		if (day == null) {
			model.put("message", "Day with id " + dayid.toString()
					+ " not exists");
			return "redirect:/showlist";
		}

		model.addAttribute("day", day);
		return "/schedule/show";
	}

	@RequestMapping("/deleteday")
	public String deleteScheduleDay(@RequestParam("dayid") Long dayid,
			ModelMap model) {
		service.removeScheduleDay(dayid);
		return "redirect:/showlist";
	}

	@RequestMapping("/addscheduleuser")
	public String addScheduleUser(@RequestParam("dayid") Long dayid,
			ModelMap model) {
		ScheduleDay day = service.findScheduleDay(dayid);
		if (day == null) {
			model.put("message", "Day with id " + dayid.toString()
					+ " not exists");
			return "redirect:/showlist";
		}

		ScheduleUserForm form = new ScheduleUserForm();
		form.setDay(day.getDate());
		form.setDayId(day.getId());

		model.put("scheduleUserForm", form);

		return "/schedule/adduser";
	}

	@RequestMapping(value = "/savescheduleuser", method = RequestMethod.POST)
	public String saveScheduleUser(
			@ModelAttribute("scheduleUserForm") ScheduleUserForm form,
			ModelMap model) {
		ScheduleDay day = service.findScheduleDay(form.getDayId());
		if (day == null) {
			model.put("message", "Day with id " + form.getDayId().toString()
					+ " not exists");
			return "redirect:/showlist";
		}

		ScheduleUser user = new ScheduleUser();
		BeanUtils.copyProperties(form, user);
		user.setDay(day);
		service.addScheduleUser(user);

		logger.info("add user " + user.getEmail() + " to schedule day "
				+ new SimpleDateFormat().format(day.getScheduleDate()));

		return getDayRedirect(day.getId());
	}

	private String getDayRedirect(Long dayid) {
		return "redirect:/showday?dayid=" + dayid.toString();
	}
}