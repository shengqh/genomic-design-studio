package edu.vanderbilt.cqs.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.jboss.logging.Logger;
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

import edu.vanderbilt.cqs.RegistrationType;
import edu.vanderbilt.cqs.Role;
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
		if (currentUser().getRole() >= Role.OBSERVER) {
			model.put("days", service.listScheduleDay());
		} else {
			model.put("days", service.listComingScheduleDay());
		}
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
	@Secured("ROLE_MANAGER")
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
		form.setRegType(RegistrationType.rtOnsite);

		model.put("scheduleUserForm", form);

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
			model.put("message", "Day with id " + form.getDayId().toString()
					+ " not exists");
			return "redirect:/showlist";
		}

		ScheduleUser user = new ScheduleUser();
		BeanUtils.copyProperties(form, user);
		user.setEmail(user.getEmail().toLowerCase());
		user.setDay(day);
		user.setRegisterTime(new Date());

		ScheduleUser oldUser = service.findScheduleUser(day.getId(),
				user.getEmail());
		if (oldUser != null) {
			model.put("message", "User " + user.getEmail()
					+ " has already registered for genomic design studio at "
					+ day.getDate());
			return "redirect:/showlist";
		}

		service.addScheduleUser(user);

		logger.info("add user " + user.getEmail() + " to schedule day "
				+ new SimpleDateFormat().format(day.getScheduleDate()));

		return getDayRedirect(day.getId());
	}

	@RequestMapping("/deletescheduleuser")
	@Secured("ROLE_MANAGER")
	public String deleteScheduleUser(@RequestParam("dayid") Long dayid,
			@RequestParam("userid") Long userid, ModelMap model) {
		service.removeScheduleUser(userid);
		return getDayRedirect(dayid);
	}

	private String getDayRedirect(Long dayid) {
		return "redirect:/showday?dayid=" + dayid.toString();
	}
}