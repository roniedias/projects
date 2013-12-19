package br.com.breezeReport.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;



/* O método preHandle recebe a requisição e a resposta, além do controlador que está sendo interceptado. 
 * O retorno é um booleano que indica se queremos continuar com a requisição ou não. Portanto, a classe 
 * AutorizadorInterceptor só deve devolver true se o usuário estiver logado. Caso o usuário não esteja 
 * autorizado, o mesmo será redirecionado para o formulário de login.
*/

public class AutorizadorInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller) throws Exception {
		
		String uri = request.getRequestURI();
		
		if(uri.endsWith("loginForm") || uri.endsWith("efetuaLogin") || uri.contains("resources")) {
			return true;
		}
		
		if(request.getSession().getAttribute("usuarioLogado") != null) {
			return true;
		}
		
		response.sendRedirect("loginForm");
		return false;
		
	}

}
