package edu.vanderbilt.cqs.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
class ScheduleController extends RootController {
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
				addUserLogInfo("delete user " + oldUser.getEmail() + " from "
						+ day.getDate());
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

	@RequestMapping("/export")
	@Secured({ Role.ROLE_VANGARD, Role.ROLE_ADMIN })
	public void export(
			@RequestParam(value = "dayid", required = false) Long dayid,
			HttpServletResponse response) throws IOException {
		List<ScheduleDay> days;
		String filename;
		String loginfo;
		if (dayid == null) {
			days = service.listScheduleDay();
			filename = "Vangard_Schedule_All.xls";
			loginfo = "export all schedule events";
		} else if (dayid == -1) {
			days = service.listPassedScheduleDay();
			String daystr = new SimpleDateFormat("yyyyMMdd").format(new Date()); 
			filename = "Vangard_Schedule_To_"
					+ daystr
					+ ".xls";
			loginfo = "export schedule until " + daystr;
		} else {
			days = new ArrayList<ScheduleDay>();
			ScheduleDay aday = service.findScheduleDay(dayid);
			if (aday != null) {
				days.add(aday);
			}
			String daystr = new SimpleDateFormat("yyyyMMdd").format(aday.getScheduleDate()); 
			filename = "Vangard_Schedule_"
					+ daystr + ".xls";
			loginfo = "export schedule of " + daystr;
		}

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "inline; filename="
				+ filename);
		response.setHeader("extension", "xls");

		OutputStream out = response.getOutputStream();

		Workbook workbook = new HSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();

		Font headerFont = workbook.createFont();
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(headerFont);

		// Create cell style for the body
		Font bodyFont = workbook.createFont();
		bodyFont.setFontHeightInPoints((short) 10);

		CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setFont(bodyFont);
		bodyStyle.setAlignment(CellStyle.ALIGN_LEFT);
		bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		bodyStyle.setWrapText(false);

		CellStyle purposeStyle = workbook.createCellStyle();
		purposeStyle.setFont(bodyFont);
		purposeStyle.setAlignment(CellStyle.ALIGN_LEFT);
		purposeStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		purposeStyle.setWrapText(true);

		Sheet sheet = workbook.createSheet();

		sheet.setColumnWidth(0, 18 * 256);
		sheet.setColumnWidth(1, 15 * 256);
		sheet.setColumnWidth(2, 15 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 20 * 256);
		sheet.setColumnWidth(5, 30 * 256);
		sheet.setColumnWidth(6, 60 * 256);

		Row row = sheet.createRow(0);
		createCell(createHelper, headerStyle, row, 0, "Studio date");
		createCell(createHelper, headerStyle, row, 1, "First Name");
		createCell(createHelper, headerStyle, row, 2, "Last Name");
		createCell(createHelper, headerStyle, row, 3, "Email");
		createCell(createHelper, headerStyle, row, 4, "Department/Division");
		createCell(createHelper, headerStyle, row, 5, "Study PI");
		createCell(createHelper, headerStyle, row, 6, "Purpose");

		int rowNo = 1;
		for (ScheduleDay day : days) {
			boolean bFirst = true;
			for (ScheduleUser user : day.getUsers()) {
				row = sheet.createRow(rowNo);
				row.setHeight((short) -1);

				if (bFirst) {
					createCell(createHelper, bodyStyle, row, 0, day.getDate());
					bFirst = false;
				} else {
					createCell(createHelper, bodyStyle, row, 0, "");
				}

				createCell(createHelper, bodyStyle, row, 1, user.getFirstname());
				createCell(createHelper, bodyStyle, row, 2, user.getLastname());
				createCell(createHelper, bodyStyle, row, 3, user.getEmail());
				createCell(createHelper, bodyStyle, row, 4,
						user.getDepartment());
				createCell(createHelper, bodyStyle, row, 5, user.getStudyPI());
				createCell(createHelper, purposeStyle, row, 6,
						user.getPurpose());

				rowNo++;
			}
		}

		sheet.autoSizeColumn(6);

		workbook.write(out);

		out.close();
		
		addUserLogInfo(loginfo);
	}

	private void createCell(CreationHelper createHelper, CellStyle bodyStyle,
			Row row, int colNo, String value) {
		Cell c;
		c = row.createCell(colNo);
		c.setCellStyle(bodyStyle);
		c.setCellValue(createHelper.createRichTextString(value));
	}
}