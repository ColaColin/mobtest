package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Security.Authenticated(Secured.class)
public class Status extends Controller {
	
	public static Result status() {
		return ok(status.render(Application.getUser()));
	}
	
	public static Result listAvailableQueries() {
		ObjectNode resultA = Json.newObject();

		try {
			Thread.sleep(500);
		} catch (Exception ex) {
			
		}
		
		resultA.put("url", routes.Status.doDemoQuery().url());
		resultA.put("header", "Demo");
		
		ObjectNode resultB = Json.newObject();

		resultB.put("url", routes.Status.doDemoQuery().url());
		resultB.put("header", "Demo");
		
		return ok(Json.toJson(Arrays.asList(resultA, resultB)));
	}
	
	public static Promise<Result> doDemoQuery() {
		Promise<JsonNode> promise = Promise.promise(
			new Function0<JsonNode>() {
				public JsonNode apply() {
					try {
						Thread.sleep((int)(5000 + Math.random() * 5000));
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
					
					List<ObjectNode> nodes = new ArrayList<>();
					for (int i = 0; i < Math.random() * 5 + 3; i++) {
						ObjectNode result = Json.newObject();
						result.put("label", "label no."+i);
						result.put("value", "value no."+i);
						nodes.add(result);
					}
					
					return Json.toJson(nodes);
				}
			}
		);
		
		return promise.map(
			new Function<JsonNode, Result>() {
				public Result apply(JsonNode node) {
					return ok(node);
				}
			}
		);
	}
}