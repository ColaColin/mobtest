package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class Secured extends Security.Authenticator{
	
	public static final String MAGIC = "test";
	
	public static final String USER_KEY = "user";
	public static final String DOMAIN_KEY = "domain";
	
	@Override
	public String getUsername(Context ctx) {
		return ctx.session().get(USER_KEY);
	}
	
	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Application.login());
	}
}