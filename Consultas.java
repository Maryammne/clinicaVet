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
	public void form(Long animalId) {
		Animal animal = Animal.findById(animalId);
		List<Veterinario> veterinarios = Veterinario.findAll();
		render(animal, veterinarios);
	}

	public static void salvar(Consulta consulta) {
		consulta.save();
		flash.success("Cunsulta do " + consulta.animal.nome + " cadastrada!");
		detalhar(consulta.id);
	}

	public static void detalhar(Long id) {
		Consulta consulta = Consulta.findById(id);
		render(consulta);
	}

	public static void listar(String termo) {
		List<Consulta> consultas = null;
		if (termo == null || termo.isEmpty()) {
			consultas = Consulta.findAll();

		} else {
			consultas = Consulta.find("lower(animal.nome) like ?1 or lower(veterinario.nome) like ?1",
					"%" + termo.toLowerCase() + "%").fetch();
		}

		render(consultas, termo);
	}

	@Operador
	public void editar(Long id) {
		Consulta c = Consulta.findById(id);
		List<Veterinario> veterinarios = Veterinario.findAll();

		renderTemplate("Consultas/form.html", c, veterinarios);
	}

}
