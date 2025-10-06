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
	public void form() {
		List<Tutor> tutores = Tutor.findAll();
		render(tutores);
	}

	public static void salvar(Animal animal) {
		animal.save();
		flash.success("Dados do " + animal.nome + " cadastrados!");
		detalhar(animal);
	}
      
	@Operador
	public static void excluir(Long id) {

		Animal animal = Animal.findById(id);
		animal.delete();
		listar(null);

	}
 
	@Operador
	public void editar(Long id) {
		Animal a = Animal.findById(id);
		List<Tutor> tutores = Tutor.findAll();

		renderTemplate("Animais/form.html", a, tutores);
	}
     
	
	public static void detalhar(Animal animal) {
		render(animal);
	}

	public static void listar(String termo) {
		List<Animal> animais = null;
		if (termo == null || termo.isEmpty()) {
			animais = Animal.findAll();

		} else {
			animais = Animal.find("lower(nome) like ?1 or lower(tutor.nome) like ?1","%" + termo.toLowerCase() + "%").fetch();
		}

		render(animais, termo);
	}

}
