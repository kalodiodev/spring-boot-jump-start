package eu.kalodiodev.springjumpstart.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for {@link UserUtils}
 * 
 * @author Athanasios Raptodimos
 */
public class UserUtilsTest {
	
	@Mock
	HttpServletRequest httpServletRequestMock;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
		
	@Test
	public void shouldCreatePasswordResetURL() {
		String scheme = "http";
		String serverName = "example.com";
		int serverPort = 8080;
		String contextPath = "";
		String token = "mytoken";
		Long userId = 1L;
		
		String url = scheme + "://" + serverName + ":" + String.valueOf(serverPort) + 
				contextPath + "/changepassword" + "?id=1&token=" + token;
		
		when(httpServletRequestMock.getScheme()).thenReturn(scheme);
		when(httpServletRequestMock.getServerName()).thenReturn(serverName);
		when(httpServletRequestMock.getServerPort()).thenReturn(serverPort);
		when(httpServletRequestMock.getContextPath()).thenReturn(contextPath);
		
		String result = UserUtils.passwordResetUrl(httpServletRequestMock, userId, token);

		assertThat(result).isEqualTo(url);
	}
}
