package it.unipd.dei.webapp.resource;

/**
 * Contains constants for the actions performed by the application.
 */
public final class Actions {

	/**
	 * The creation of a Recipe
	 */
	public static final String CREATE_RECIPE = "CREATE_RECIPE";

	/**
	 * The search of the recipes created by the user
	 */
	public static final String SEARCH_CREATED_RECIPES = "SEARCH_CREATED_RECIPES";

	/**
	 * The search of the recipes liked by the user
	 */
	public static final String SEARCH_LIKED_RECIPES = "SEARCH_LIKED_RECIPES";

	/**
	 * The search of the Recipes to be approved
	 */
	public static final String SEARCH_RECIPES_TO_BE_APPROVED = "SEARCH_RECIPES_TO_BE_APPROVED";

	/**
	 * The search of the Recipes to be approved
	 */
	public static final String SEARCH_RECIPES_TO_BE_RECOVERED_OR_REMOVED = "SEARCH_RECIPES_TO_BE_RECOVERED_OR_REMOVED";

	/**
	 * The search of the Recipes to be approved
	 */
	public static final String APPROVE_OR_REJECT_RECIPES = "APPROVE_OR_REJECT_RECIPES";

	/**
	 * The search of the Recipes to be approved
	 */
	public static final String REMOVE_ALL_REJECTED_RECIPES = "REMOVE_ALL_REJECTED_RECIPES";

	/**
	 * The login of a User
	 */
	public static final String LOGIN_USER = "LOGIN_USER";

	/**
	 * The logout of a User
	 */
	public static final String LOGOUT_USER = "LOGOUT_USER";

	/**
	 * The ban of the user from the webapp
	 */
	public static final String BAN_USER = "BAN_USER";

	/**
	 * The login of a User
	 */
	public static final String VERIFY_USER = "VERIFY_USER";

	/**
	 * The access to the verification page
	 */
	public static final String ACCESS_VERIFICATION_PAGE = "ACCESS_VERIFICATION_PAGE";

	/**
	 * The changing of the password of a user
	 */
	public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";

	/**
	 * The creation of a User
	 */
	public static final String CREATE_USER = "CREATE_USER";

	/**
	 * The access to the registration page
	 */
	public static final String ACCESS_REGISTRATION_PAGE = "ACCESS_REGISTRATION_PAGE";

	/**
	 * The search of user by their email
	 */
	public static final String SEARCH_USER_BY_EMAIL = "SEARCH_USER_BY_EMAIL";

	/**
	 * The view of the user's data
	 */
	public static final String VIEW_USER_DATA = "VIEW_USER_DATA";

	/**
	 * The view of the user's data
	 */
	public static final String VIEW_ABOUT_US_INFO = "VIEW_ABOUT_US_INFO";

	/**
	 * The view of the user's data
	 */
	public static final String VIEW_RECIPE_DETAILS = "VIEW_RECIPE_DETAILS";

	/**
	 * The update of the user's data
	 */
	public static final String UPDATE_USER_DATA = "UPDATE_USER_DATA";
	/**
	 * The request for a search
	 */
	public static final String RQ_FOR_SEARCH = "RQ_FOR_SEARCH";

	/**
	 * This class can be neither instantiated nor sub-classed.
	 */
	private Actions() {
		throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
	}

}
