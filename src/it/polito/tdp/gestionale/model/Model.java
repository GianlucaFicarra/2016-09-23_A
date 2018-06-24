package it.polito.tdp.gestionale.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.gestionale.db.DidatticaDAO;

public class Model {

	private List<Corso> corsi;
	private List<Studente> studenti;
	private Map<Integer, Studente> mapStudenti; //matricola + studente
	private List<Nodo> vertici;
	private int studentiFrequentanti;
	
	private DidatticaDAO dao;
	private Graph<Nodo, DefaultEdge> grafo;

	
	public Model() {
		dao= new DidatticaDAO();
		grafo= new SimpleGraph<>(DefaultEdge.class);
		mapStudenti= new HashMap<Integer, Studente>();
		
		corsi= dao.getTuttiICorsi();
		studenti = dao.getTuttiStudenti();
		System.out.println("corsi "+corsi.size()+" studenti " +studenti.size());
		for(Studente s: studenti) {  //mappa per oggenere oggetto studente dal dao
			mapStudenti.put(s.getMatricola(), s);
		}
		
		vertici = new LinkedList<>();
		vertici.addAll(corsi);
		vertici.addAll(studenti);
		/*debug per stampare tutti i vertici
		for(Nodo s: vertici) {
			System.out.println("\n"+s.toString());
		}*/
		
		studentiFrequentanti= dao.getFrequentanti(); //num studenti iscritti ad almeno un corso
	}

	public void creagrafo() {
		
		//vertici
		Graphs.addAllVertices(grafo, vertici);
		
		//arco= studente collegato a corso a cui è iscritto
		for(Corso c: corsi) {
			
			//per ogni corso salvo gli studenti a lui inscritti e li ritorno per creare arco
			List<Studente> studentiIscrittiAlCorso = dao.getStudentiIscrittiAlCorso(c, mapStudenti);
			c.addStudenti(studentiIscrittiAlCorso);
			
			if(!studentiIscrittiAlCorso.isEmpty()) {
				for(Studente s: studentiIscrittiAlCorso) {
					grafo.addEdge(c, s); //creo arco
					s.aggiungiCorso(); //memorizzo nello studente il corso seguito
					
				}
			}
			
		}
		
	}

	public List<Studente> getStudenti() {
		return studenti;
	}

	public List<Corso> invitaCorsi() {
		List<Corso> invitati= new LinkedList<>();
		List<Corso> parziale= new LinkedList<>();
		List<Studente>studentiRaggunti= new LinkedList<>();
		
		/*Sviluppare un algoritmo ricorsivo per individuare quale sia l’insieme minimo di corsi
		 *(e quindi di interventi in aula), tali per cui la comunicazione raggiunga tutti gli 
		 *studenti che frequentano almeno un corso.
		 *
		 *Soluzione parziale= insieme di corsi
		 *Soluzione finale= insieme corsi che raggiunge tutti gli studenti
		 *Passo: aggiungo un corso
		 *Cond terminazione: studenti dei corsi(senza ripetizioni)= num studenti frequantanti
		 */
		
		cerca(invitati, parziale, studentiRaggunti);
		
		return invitati;
	}

	private void cerca(List<Corso> invitati, List<Corso> parziale, List<Studente> studentiRaggunti) {
		
		
		for(Corso c: corsi) {
			parziale.add(c);
			
			for(Studente s: c.getStudenti()) {
				if(!studentiRaggunti.contains(s))
					studentiRaggunti.add(s);
			}
			
			//con questo corso li ho raggiunti tutti?
			if(studentiRaggunti.size()==studentiFrequentanti) {
				invitati.addAll(parziale);
				return;
			}
			
		}
		
		
		
		/*METODO ALTERNATICO DA SOLUZ--DATI TUTTI GLI STUDENTI LI RIMUOVO
		HashSet<Studente> hashSetStudenti = new HashSet<Studente>(studenti);
		for (Corso corso : parziale) {  //rimuovo gli studenti dei corsi della parziale
			hashSetStudenti.removeAll(corso.getStudenti());
		}
		
		if (hashSetStudenti.isEmpty()) {
			if (invitati.isEmpty()) //al primo giro 
				invitati.addAll(parziale);
				
			if (parziale.size() < invitati.size()){
				invitati.clear();
				invitati.addAll(parziale);
			}
		}
		
		for (Corso corso : corsi) {
			if (parziale.isEmpty() || corso.compareTo(parziale.get(parziale.size()-1)) > 0) {
				parziale.add(corso);
				cerca(parziale, invitati);
				parziale.remove(corso);
			}
		}*/
		
		
		
		
	}
	
	
	
	
	
}
