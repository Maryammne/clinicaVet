package controllers;

import models.Operador;
import play.mvc.Controller;

public class Logins extends Controller {

	public static void form(){
    render();
	}
	
	public static void logar (String login, String senha) {
		
		Operador operador = Operador.find("login = ?1 and senha = ?2", login,senha).first();
		
		if(operador == null) {
			flash.error("Login ou senha inválidos");
			form();
			
		}else {
			session.put("usuarioLogado", operador.email);
			session.put("usuarioPerfil", operador.perfil.name());
			flash.success("Logado com sucesso");
			Animais.listar(null);
		}
	
	}
	
	public static void logout() {
		session.clear();
		flash.success("Você saiu do sistema!");
		form();
	}
	
}
