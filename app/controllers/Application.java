package controllers;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import play.*;
import play.data.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	
	public static class Task {
		public String link;
		public String label;
		
		public Task(String link, String label) {
			this.link = link;
			this.label = label;
		}
	}
	
	public static List<Task> TASK_LIST = Arrays.asList(
			new Task(routes.Status.status().url(), "Leitungsinfo"),
			new Task(routes.Application.inventory().url(), "Inventarverwaltung"));
	
	public static class Login {
		public String user;
		public String domain;
		public String password;
		
		public String validate() {
			if (Secured.MAGIC.equals(password) && !StringUtils.isBlank(user) && !StringUtils.isBlank(domain)) {
				return null;
			} else {
				return "Login fehlerhaft";
			}
		}
	}
	
	public static String getUser() {
		return session(Secured.USER_KEY);
	}
	
	@Security.Authenticated(Secured.class)
    public static Result index() {
		return ok(index.render(TASK_LIST, getUser()));
    }

	// This should be put into it's own controller
	@Security.Authenticated(Secured.class)
	public static Result inventory() {
		return ok(main.render("TODO", "dummyPage", getUser(), null));
	}
	
    public static Result login() {
    	if (session(Secured.USER_KEY) != null) {
    		return redirect(routes.Application.index());
    	} else {
    		return ok(login.render(Form.form(Login.class)));
    	}
    }
    
    public static Result handleLogin() {
    	Form<Login> loginReq = Form.form(Login.class).bindFromRequest();
    	
    	if (loginReq.hasErrors()) {
    		// sending a http 400 would result in an error message by jquerymobile here
    		return ok(login.render(loginReq));
    	} else {
    		session().clear();
    		session(Secured.USER_KEY, loginReq.get().user);
    		session(Secured.DOMAIN_KEY, loginReq.get().domain);
    		return redirect(routes.Application.index());
    	}
    }
    
    public static Result logout() {
    	session().clear();
    	return redirect(routes.Application.login());
    }
}