package br.com.breezeReport.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;



/* O m�todo preHandle recebe a requisi��o e a resposta, al�m do controlador que est� sendo interceptado. 
 * O retorno � um booleano que indica se queremos continuar com a requisi��o ou n�o. Portanto, a classe 
 * AutorizadorInterceptor s� deve devolver true se o usu�rio estiver logado. Caso o usu�rio n�o esteja 
 * autorizado, o mesmo ser� redirecionado para o formul�rio de login.
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
