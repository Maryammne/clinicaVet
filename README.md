# clinicaVet
Trabalho referente a disciplina de PDS E PI 

# Documentação das Classes Controladoras

Este documento descreve as classes controladoras do sistema **Clínica Veterinária**, explicando suas responsabilidades, métodos principais e comportamentos de autenticação/autorização.

---

## 1. Application

```java
package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

}
```

### Descrição
Classe principal padrão do Play Framework. Responsável por exibir a página inicial do sistema.

#### Métodos
- `index()`: Método padrão que renderiza a página inicial. Não recebe parâmetros.

---

## 2. Seguranca

```java
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
```

### Descrição
Classe de segurança utilizada como interceptor para autenticação e autorização de usuários.

#### Métodos
- `verificarautenticacao()`: Antes de cada ação, verifica se há um usuário logado. Se não houver, redireciona para o formulário de login.
- `verificarOperador()`: Antes de ações anotadas com `@Operador`, verifica se o usuário possui perfil de operador, bloqueando acesso indevido.

---

## 3. Consultas

```java
package controllers;

import java.util.List;

import models.Animal;
import models.Consulta;
import models.Tutor;
import models.Veterinario;
import play.mvc.Controller;
import play.mvc.With;
import security.Operador;

@With(Seguranca.class)
public class Consultas extends Controller {

	@Operador
	public void form(Long animalId) { ... }

	public static void salvar(Consulta consulta) { ... }

	public static void detalhar(Long id) { ... }

	public static void listar(String termo) { ... }

	@Operador
	public void editar(Long id) { ... }
}
```

### Descrição
Controlador responsável pelo gerenciamento de consultas veterinárias.

#### Métodos
- `form(Long animalId)`: Exibe formulário de cadastramento de consulta para um animal e lista veterinários.
- `salvar(Consulta consulta)`: Salva uma nova consulta no banco de dados e redireciona para sua página de detalhes.
- `detalhar(Long id)`: Mostra detalhes de uma consulta específica.
- `listar(String termo)`: Lista todas as consultas, podendo filtrar por nome do animal ou veterinário.
- `editar(Long id)`: Exibe formulário de edição para uma consulta existente.

#### Segurança
Todas as ações requerem autenticação via `Seguranca`. As ações de formulário e edição exigem perfil de operador.

---

## 4. Animais

```java
package controllers;

import java.util.List;

import models.Animal;
import models.Tutor;
import play.mvc.Controller;
import play.mvc.With;
import security.Operador;

@With(Seguranca.class)
public class Animais extends Controller {

	@Operador
	public void form() { ... }

	public static void salvar(Animal animal) { ... }

	@Operador
	public static void excluir(Long id) { ... }

	@Operador
	public void editar(Long id) { ... }
	
	public static void detalhar(Animal animal) { ... }

	public static void listar(String termo) { ... }
}
```

### Descrição
Controlador para cadastro, exclusão, edição, detalhamento e listagem de animais da clínica.

#### Métodos
- `form()`: Exibe formulário de cadastro de animal, listando possíveis tutores.
- `salvar(Animal animal)`: Salva um novo animal e redireciona para detalhes.
- `excluir(Long id)`: Remove um animal do sistema.
- `editar(Long id)`: Exibe formulário de edição de animal.
- `detalhar(Animal animal)`: Exibe detalhes do animal.
- `listar(String termo)`: Lista todos os animais ou filtra por nome/tutor.

#### Segurança
A maioria das ações exige operador autenticado.

---

## 5. Logins

```java
package controllers;

import models.Operador;
import play.mvc.Controller;

public class Logins extends Controller {

	public static void form(){ ... }
	
	public static void logar (String login, String senha) { ... }
	
	public static void logout() { ... }
	
}
```

### Descrição
Controlador responsável pelo fluxo de autenticação.

#### Métodos
- `form()`: Exibe a tela de login.
- `logar(String login, String senha)`: Realiza autenticação, salvando usuário e perfil na sessão. Em caso de falha, exibe mensagem de erro.
- `logout()`: Limpa a sessão do usuário e retorna à tela de login.

---

## Observações Gerais

- Todas as classes utilizam o padrão MVC do Play Framework.
- O sistema faz controle de sessão e perfil para garantir a segurança das operações.
- O uso de anotações (`@Before`, `@With`, `@Operador`) facilita a separação de preocupações e a proteção de rotas sensíveis.

