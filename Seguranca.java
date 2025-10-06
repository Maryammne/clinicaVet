package controllers;

import models.Perfil;
import play.mvc.Before;
import play.mvc.Controller;
import security.Operador;

public class Seguranca extends Controller{
	
	@Before
	static void verificarautenticacao() {
		if(!session.contains("usuarioLogado")) {
			flash.error("Você deve logar no sistema");
			Logins.form();
		}
	}
	
	@Before 
	static void verificarOperador() {
		String perfil = session.get("usuarioPerfil");
		Operador operadorAnnotation =  getActionAnnotation(Operador.class);
		
		if(operadorAnnotation != null && 
				!Perfil.OPERADOR.name().equals(perfil)) {
			forbidden("Acesso restrito a operadores do sistema!");
		}
		
	}

}
