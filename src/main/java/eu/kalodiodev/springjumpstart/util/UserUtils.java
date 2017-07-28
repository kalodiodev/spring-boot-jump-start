package eu.kalodiodev.springjumpstart.util;

import javax.servlet.http.HttpServletRequest;

import eu.kalodiodev.springjumpstart.controller.ForgotPasswordController;

/**
 * User helper utils
 * 
 * @author Athanasios Raptodimos
 */
public class UserUtils {
	
	/**
	 * Create password reset url for user
	 * 
	 * @param request The Http Servlet Request
	 * @param userId User's id
	 * @param token Password reset token
	 * @return the URL to reset user password
	 */
	public static String passwordResetUrl(HttpServletRequest request, Long userId, String token) {
		return getAppUrl(request) + 
				ForgotPasswordController.CHANGE_PASSWORD_URL + 
				"?id=" + userId + 
				"&token=" + token; 
	}
	
	
	//-------> Private methods
	
	private static String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
