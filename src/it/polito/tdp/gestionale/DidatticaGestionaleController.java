package it.polito.tdp.gestionale;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.gestionale.model.Corso;
import it.polito.tdp.gestionale.model.Model;
import it.polito.tdp.gestionale.model.Studente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DidatticaGestionaleController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtMatricolaStudente;

	@FXML
	private TextArea txtResult;


	public void setModel(Model model) {
		this.model = model;
		model.creagrafo();
	}
	
	@FXML
	void doCorsiFrequentati(ActionEvent event) {
		
		//quanti studenti sono iscritti ad un solo corso, quanti due, quanti tre, ecc.
		txtResult.clear();
		txtResult.setText("Numero di corsi frequentati per studente: ");
		
		for(Studente s: model.getStudenti()) {
			txtResult.appendText(String.format("\nStudente " +s.getMatricola()+" frequenta "+s.getNumCorsi()+" corsi"));
		}
	}
	
	@FXML
	void doVisualizzaCorsi(ActionEvent event) {		
		txtResult.clear();
		
		List<Corso> corsi = model.invitaCorsi();
		txtResult.setText("\nCorsi da invitare alla conferenza: ");
		for(Corso c: corsi) {
			txtResult.appendText("\n"+c.toString());
		}
	}

	@FXML
	void initialize() {
		assert txtMatricolaStudente != null : "fx:id=\"txtMatricolaStudente\" was not injected: check your FXML file 'DidatticaGestionale.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'DidatticaGestionale.fxml'.";

	}


}
