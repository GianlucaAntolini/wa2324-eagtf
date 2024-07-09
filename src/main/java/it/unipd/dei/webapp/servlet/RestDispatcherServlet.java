
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.resource.Message;
// import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import it.unipd.dei.webapp.rest.*;

import java.io.IOException;
import java.io.OutputStream;

public final class RestDispatcherServlet extends AbstractDatabaseServlet {

	/**
	 * The JSON UTF-8 MIME media type
	 */
	private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse res) throws IOException {

		LOGGER.info("DISPACHER SERVLET");
		LogContext.setIPAddress(req.getRemoteAddr());
		final OutputStream out = res.getOutputStream();

		try {
			// checks if is something we care about
			// if the requested resource was an Employee, delegate its processing and return
			if (processResource(req, res)) {
				return;
			}

			// if none of the above process methods succeeds, it means an unknown resource
			// has been requested
			LOGGER.warn("Unknown resource requested: %s.", req.getRequestURI());

			final Message m = new Message("Unknown resource requested.", "E4A6",
					String.format("Requested resource is %s.", req.getRequestURI()));
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			res.setContentType(JSON_UTF_8_MEDIA_TYPE);
			m.toJSON(out);

		} catch (Throwable t) {

			LOGGER.error("Unexpected error while processing the REST resource.", t);
			final Message m = new Message("Unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(out);
		} finally {
			// ensure to always flush and close the output stream
			if (out != null) {
				out.flush();
				out.close();
			}

			LogContext.removeIPAddress();
		}
	}

	// actually checks if is something we care about
	private boolean processResource(final HttpServletRequest req, final HttpServletResponse res) throws Exception {

		final String method = req.getMethod();

		String path = req.getRequestURI();
		// Message m = null;

		// if it does not contain the rest/resorce it stops
		// the requested resource was not an employee
		if (path.lastIndexOf("rest/") <= 0) {
			return false;
		}

		// remove rest
		path = path.substring(path.lastIndexOf("rest/") + 5);

		// subdivided path
		String[] path_arg = path.split("/");

		// print the value of the obtained url
		StringBuilder pathLine = new StringBuilder();
		for (String arg : path_arg) {
			pathLine.append(arg).append(" ");
		}

		LOGGER.info("Array_args_len (%d) %s", path_arg.length, pathLine.toString().trim());

		// here we choose wich urls needs to go
		// the logic is path_arg[0]/ppath_arg[1]/path_arg[2]/.....
		// first we choose the method () than te if the argumeth checks we procede to
		// the xxxRR
		// if we need to handle the case in wich we have no arguments just check if
		// there are any more arguments (ex recipe is at depth 1
		//
		//
		// if(path_arg.length>1){
		// // with arguments
		// LOGGER.info("recipe with arguments \n got a request %s for the
		// %s",method,path_arg[0]);
		// }else{
		// // without arguments
		// LOGGER.info("recipe with no arguments \n got a request %s for the
		// %s",method,path_arg[0]);
		// //
		//
		//
		//

		switch (method) {
			case "GET":
				// first argument
				switch (path_arg[0]) {

					case "recipe":
						// handles rest/recipe
						if (path_arg.length == 1) {
							// with arguments
							LOGGER.info("recipe with no arguments \n got a request %s for the %s", method, path_arg[1]);
						}

						// handles rest/recipe/id
						// espanso ricette
						if (path_arg.length == 2) {
							switch (path_arg[1]) {
								case "search_recipe":
									LOGGER.info("recipe/search_recipe with  %s for the %s", method, path_arg[1]);
									new SearchRecipeRR(req, res, getConnection()).serve();
									break;
								case "ranked_recipe":
									LOGGER.info("recipe/ranked_recipe call with  %s for the %s", method, path_arg[1]);
									new RankedRecipeRR(req, res, getConnection()).serve();
									break;
								case "suggested_recipe":
									LOGGER.info("recipe/suggested_recipe call with  %s for the %s", method,
											path_arg[1]);
									new SuggestedRecipeRR(req, res, getConnection()).serve();
									break;
								default:
									LOGGER.info("recipe/{id} call with %s for the %s", method, path_arg[1]);
									try {
										LOGGER.info("recipe/{id} call with  %s for the %s", method, path_arg[1]);
										int id = Integer.parseInt(path_arg[1]);
										new RecipeDetailsRR(req, res, getConnection(), id).serve();
									} catch (NumberFormatException e) {
										LOGGER.error("Invalid id format: %s", path_arg[1]);
										// LOGGER.info("recipe/{id} call with %s for the %s",method,path_arg[1]);
										// Handle the case where the id is not a valid integer
										// For example, return an error response or log the error
									}
									break;
							}
						}

						// handles rest/recipe/id/*
						if (path_arg.length == 3) {
							// TODO fix
							// new RecipeDetailsRR(req, res, getConnection(),41).serve();
							LOGGER.info("recipe/{id}/like call with %s for the %s", method, path_arg[1]);
						}
						break;
					case "user":
						LOGGER.info("got a request %s for the %s", method, path_arg[0]);
						break;

					case "like":
						if (path_arg.length == 2) {
							HttpSession session;

							if ((session = req.getSession(false)) == null || session.getAttribute("user_id") == null) {
								res.sendRedirect(req.getContextPath());
								return false;
							}

							String recipe_id = path_arg[1];
							String user_id = Integer.toString((Integer) session.getAttribute("user_id"));

							req.setAttribute("recipe_id", recipe_id);
							req.setAttribute("user_id", user_id);

							new GetUserLikeRR(req, res, getConnection()).serve();
							LOGGER.info("recipe with arguments \n got a request %s for the %s", method, path_arg[0]);
						}
						break;
					default:
						LOGGER.info("got a request %s for the %s", method, path_arg[0]);
						break;
				}
				break;

			case "POST":
				switch (path_arg[0]) {
					case "like":
						if (path_arg.length == 2) {
							HttpSession session;

							if ((session = req.getSession(false)) == null || session.getAttribute("user_id") == null) {
								res.sendRedirect(req.getContextPath());
								return false;
							}

							String recipe_id = path_arg[1];
							String user_id = Integer.toString((Integer) session.getAttribute("user_id"));

							req.setAttribute("recipe_id", recipe_id);
							req.setAttribute("user_id", user_id);

							new ToggleUserLikeRR(req, res, getConnection()).serve();

							LOGGER.info("recipe with arguments \n got a request %s for the %s of user", method,
									path_arg[0]);
						}
						break;
					case "user":
						if (path_arg.length == 3) {
							HttpSession session;

							if ((session = req.getSession(false)) == null || session.getAttribute("user_id") == null
									|| (int) session.getAttribute("role_id") != 0) {
								res.sendRedirect(req.getContextPath());
								return false;
							}

							switch (path_arg[2]) {
								case "ban":
									req.setAttribute("user_id", path_arg[1]);
									req.setAttribute("role_id", "2");

									new BanUserRR(req, res, getConnection()).serve();

									LOGGER.info("recipe with arguments \n got a request %s for %s %s", method,
											path_arg[0], path_arg[2]);

									break;
								case "unban":
									req.setAttribute("user_id", path_arg[1]);
									req.setAttribute("role_id", "1");

									new BanUserRR(req, res, getConnection()).serve();

									LOGGER.info("recipe with arguments \n got a request %s for %s %s", method,
											path_arg[0], path_arg[2]);

									break;
								default:
									LOGGER.info("got a request %s for %s %s", method, path_arg[0], path_arg[2]);
									break;
							}
						}
						break;
					case "recipe":
						if (path_arg.length == 3) {
							HttpSession session;

							if ((session = req.getSession(false)) == null || session.getAttribute("user_id") == null
									|| (int) session.getAttribute("role_id") != 0) {
								res.sendRedirect(req.getContextPath());
								return false;
							}

							switch (path_arg[2]) {
								case "recover":
									req.setAttribute("recipe_id", path_arg[1]);
									new RecoverRecipeRR(req, res, getConnection()).serve();

									LOGGER.info("recipe with arguments \n got a request %s for %s %s", method,
											path_arg[0], path_arg[2]);

									break;
								case "remove":
									req.setAttribute("recipe_id", path_arg[1]);
									new RemoveRecipeRR(req, res, getConnection()).serve();

									LOGGER.info("recipe with arguments \n got a request %s for %s %s", method,
											path_arg[0], path_arg[2]);

									break;
								case "reject":
									req.setAttribute("recipe_id", path_arg[1]);
									req.setAttribute("approved", false);
									new RejectRecipeRR(req, res, getConnection()).serve();

									LOGGER.info("recipe with arguments \n got a request %s for %s %s", method,
											path_arg[0], path_arg[2]);

									break;
								case "approve":
									req.setAttribute("recipe_id", path_arg[1]);
									req.setAttribute("approved", true);
									new ApproveRecipeRR(req, res, getConnection()).serve();

									LOGGER.info("recipe with arguments \n got a request %s for %s %s", method,
											path_arg[0], path_arg[2]);

									break;
								default:
									LOGGER.info("got a request %s for %s %s", method, path_arg[0], path_arg[2]);
									break;
							}
						}
						break;
					default:
						LOGGER.info("got a request %s for the %s", method, path_arg[0]);
						break;
				}
				break;
			case "PUT":
				LOGGER.info("got a request %s for the %s", method, path_arg[0]);
				break;
			case "DELETE":
				LOGGER.info("got a request %s for the %s", method, path_arg[0]);
				break;
			default:
				LOGGER.info("Unsupported operation for URI ", method);
				// m = new Message("Unsupported operation for URI /"+path_arg[0]+"/{id}.",
				// "E4A5",String.format("Requested operation %s.", method));
				break;
		}

		// the request URI is: /resource
		// if method GET, list resources
		// if method POST, create resource

		// chack that is not empty
		// if (path.length() == 0 || path.equals("/")) {
		//
		// switch (method) {
		// case "GET":
		// new ExampleRR(req, res, getConnection()).serve();
		// LOGGER.info("Received a GET request for resources.");
		// break;
		// case "POST":
		// LOGGER.info("Received a POST request for creating a resource.");
		// break;
		// default:// PUT DELETE EXT
		// LOGGER.info("Unsupported operation for URI /resource: %s.", method);
		//
		// m = new Message("Unsupported operation for URI /resource.", "E4A5",
		// String.format("Requested operation %s.", method));
		// res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		// m.toJSON(res.getOutputStream());
		// break;
		// }
		// } else {
		// // the request URI is: /resource/{id}
		//
		// switch (method) {
		// case "GET":
		// new ExampleRR(req, res, getConnection()).serve();
		// LOGGER.info("Received a GET request for a specific resource.");
		// break;
		// case "PUT":
		// LOGGER.info("Received a PUT request for updating a resource.");
		// break;
		// case "DELETE":
		// LOGGER.info("Received a DELETE request for deleting a resource.");
		// break;
		// default:
		// LOGGER.info("Unsupported operation for URI /resource/{id}: %s.", method);
		//
		// m = new Message("Unsupported operation for URI /resource/{id}.", "E4A5",
		// String.format("Requested operation %s.", method));
		// res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		// m.toJSON(res.getOutputStream());
		// }
		// }

		return true;

	}
}
