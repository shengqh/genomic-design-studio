package edu.vanderbilt.cqs.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import edu.vanderbilt.cqs.Role;
import edu.vanderbilt.cqs.Utils;
import edu.vanderbilt.cqs.bean.User;
import edu.vanderbilt.cqs.form.ChangePasswordForm;
import edu.vanderbilt.cqs.form.UserForm;
import edu.vanderbilt.cqs.validator.ChangePasswordValidator;

@Controller
public class UserController extends RootController {
	@Autowired
	private Validator validator;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private ChangePasswordValidator passwordValidator;

	protected boolean sendMail = true;

	@RequestMapping("/user")
	@Secured(Role.ROLE_VANGARD)
	public String listUsers(ModelMap model) {
		model.addAttribute("validUserList", service.listValidUser());
		return "user/list";
	}

	@RequestMapping("/alluser")
	@Secured(Role.ROLE_ADMIN)
	public String listAllUsers(ModelMap model) {
		model.addAttribute("validUserList", service.listValidUser());
		model.addAttribute("invalidUserList", service.listInvalidUser());
		return "user/listall";
	}

	@RequestMapping("/adduser")
	@Secured(Role.ROLE_ADMIN)
	public String addUser(ModelMap model) {
		UserForm form = new UserForm();
		User user = new User();
		BeanUtils.copyProperties(user, form);
		form.setRole(Role.VANGARD);
		form.setRoles(Role.getRoleMap());
		model.addAttribute("userForm", form);

		addUserLogInfo("try to add user ...");

		return "user/edit";
	}

	@RequestMapping("/edituser")
	@Secured(Role.ROLE_ADMIN)
	public String editUser(@RequestParam("userid") Long userid, ModelMap model) {
		User user = service.findUser(userid);
		if (user != null) {
			UserForm form = new UserForm();
			BeanUtils.copyProperties(user, form);
			form.setRoles(Role.getRoleMap());
			model.addAttribute("userForm", form);

			addUserLogInfo("try to edit user " + user.getEmail() + " ...");
			return "user/edit";
		} else {
			return "redirect:/userall";
		}
	}

	@RequestMapping("/saveuser")
	@Secured(Role.ROLE_ADMIN)
	public String saveUser(@ModelAttribute("userForm") UserForm form,
			BindingResult result, SessionStatus status) {
		validator.validate(form, result);

		if (result.hasErrors()) {
			form.setRoles(Role.getRoleMap());
			return "user/edit";
		}

		if (form.getId() == null) {
			String password = RandomStringUtils.randomAlphanumeric(8);

			User user = new User();
			BeanUtils.copyProperties(form, user);
			user.setEmail(user.getEmail().toLowerCase());
			user.setPassword(Utils.md5(password));
			user.setCreateDate(new Date());
			service.addUser(user);

			addUserLogInfo("add user " + user.getEmail() + " as "
					+ user.getRoleName());

			if (sendMail) {
				sendConfirmationEmail(user, password);
			}
		} else {
			User user = service.findUser(form.getId());
			BeanUtils.copyProperties(form, user);

			service.updateUser(user);
			addUserLogInfo("update user " + user.getEmail() + " as "
					+ user.getRoleName());
		}
		return "redirect:/alluser";
	}

	private String setUserEnabled(Long userid, Boolean value) {
		User user = service.findUser(userid);
		if (user != null) {
			user.setEnabled(value);
			service.updateUser(user);
			addUserLogInfo("set user " + user.getEmail() + " enabled="
					+ value.toString());
		}

		return "redirect:/alluser";
	}

	private String setUserLocked(Long userid, Boolean value) {
		User user = service.findUser(userid);
		if (user != null) {
			user.setLocked(value);
			service.updateUser(user);
			addUserLogInfo("set user " + user.getEmail() + " locked="
					+ value.toString());
		}

		return "redirect:/alluser";
	}

	private String setUserDeleted(Long userid, Boolean value) {
		User user = service.findUser(userid);
		if (user != null) {
			user.setDeleted(value);
			service.updateUser(user);
			addUserLogInfo("set user " + user.getEmail() + " deleted="
					+ value.toString());
		}

		return "redirect:/alluser";
	}

	@RequestMapping("/enableuser/{userid}")
	@Secured(Role.ROLE_ADMIN)
	public String enableUser(@PathVariable Long userid) {
		return setUserEnabled(userid, true);
	}

	@RequestMapping("/disableuser/{userid}")
	@Secured(Role.ROLE_ADMIN)
	public String disableUser(@PathVariable Long userid) {
		return setUserEnabled(userid, false);
	}

	@RequestMapping("/lockuser/{userid}")
	@Secured(Role.ROLE_ADMIN)
	public String lockUser(@PathVariable Long userid) {
		return setUserLocked(userid, true);
	}

	@RequestMapping("/unlockuser/{userid}")
	@Secured(Role.ROLE_ADMIN)
	public String unlockUser(@PathVariable Long userid) {
		return setUserLocked(userid, false);
	}

	@RequestMapping("/deleteuser/{userid}")
	@Secured(Role.ROLE_ADMIN)
	public String deleteUser(@PathVariable Long userid) {
		return setUserDeleted(userid, true);
	}

	@RequestMapping("/undeleteuser/{userid}")
	@Secured(Role.ROLE_ADMIN)
	public String undeleteUser(@PathVariable Long userid) {
		return setUserDeleted(userid, false);
	}

	@RequestMapping("/deleteuserforever/{userid}")
	@Secured(Role.ROLE_ADMIN)
	public String deleteUserForever(

	@PathVariable Long userid) {
		User user = service.findUser(userid);
		if (user != null) {
			service.removeUser(user);
			addUserLogInfo("delete user " + user.getEmail() + " foever");
		}

		return "redirect:/alluser";
	}

	@RequestMapping("/changeownpassword")
	@Secured(Role.ROLE_USER)
	public String changeownpassword(ModelMap model) {
		ChangePasswordForm form = new ChangePasswordForm();
		model.put("changeOwnPasswordForm", form);

		return "user/changeownpassword";
	}

	@RequestMapping("/saveownpassword")
	@Secured(Role.ROLE_USER)
	public String saveownpassword(
			@ModelAttribute("changeOwnPasswordForm") ChangePasswordForm form,
			ModelMap model, BindingResult result, SessionStatus status) {
		User cuser = service.findUser(currentUser().getId());

		form.setOldPassword(cuser.getPassword());

		passwordValidator.validate(form, result);

		if (result.hasErrors()) {
			return "user/changeownpassword";
		} else {
			String newPassword = Utils.md5(form.getNewPassword());
			service.updatePassword(currentUser().getId(), newPassword);
			addUserLogInfo("changed own password");
			return "home";
		}
	}

	@RequestMapping("/resetpassword/{userid}")
	@Secured(Role.ROLE_ADMIN)
	public String resetpassword(
			@ModelAttribute("currentuser") User currentUser,
			@PathVariable Long userid, ModelMap model) {
		User user = service.findUser(userid);
		if (user != null) {
			String password = RandomStringUtils.randomAlphanumeric(8);

			user.setPassword(Utils.md5(password));
			service.updateUser(user);

			addUserLogInfo("reseted user " + user.getEmail() + " password.");

			if (sendMail) {
				sendPasswordChangedEmail(user, password);
			}
		}
		return "redirect:/alluser";
	}

	private String sendConfirmationEmail(final User user, final String password) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setFrom("noreply@vanderbilt.edu");
				message.setTo(user.getEmail());
				message.setSubject("Registration confirmation");
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("user", user);
				model.put("password", password);
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"edu/vanderbilt/cqs/vm/registration-confirmation.vm",
						model);
				message.setText(text, true);
			}
		};
		addSystemLogInfo("Sending mail to " + user.getEmail() + " ...");
		try {
			this.mailSender.send(preparator);
			return null;
		} catch (MailException ex) {
			addSystemLogError("Sending mail error " + ex.getMessage());
			return ex.getMessage();
		}
	}

	private String sendPasswordChangedEmail(final User user,
			final String password) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setFrom("noreply@vanderbilt.edu");
				message.setTo(user.getEmail());
				message.setSubject("Password reset confirmation");
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("user", user);
				model.put("password", password);
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"edu/vanderbilt/cqs/vm/password-reset-confirmation.vm",
						model);
				message.setText(text, true);
			}
		};
		addSystemLogInfo("Sending mail to " + user.getEmail() + " ...");
		try {
			this.mailSender.send(preparator);
			return null;
		} catch (MailException ex) {
			addSystemLogError("Sending mail error " + ex.getMessage());
			return ex.getMessage();
		}
	}
}
