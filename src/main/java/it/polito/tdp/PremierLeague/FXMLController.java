/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Battuto;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String s=this.txtGoals.getText().trim();
    	if(s.isBlank()) {
    		this.txtResult.setText("Soglia non inserita!");
    		return;
    	}
    	
    	Double x;
    	try {
    		x=Double.parseDouble(s);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Quello inserito non è un numero");
    		return;
    	}
    	
    	if(x<0) {
    		this.txtResult.setText("Inserita una soglia negativa!");
    		return;
    	}
    	
    	String result=this.model.creaGrafo(x);
    	this.txtResult.setText(result);
    	this.btnTopPlayer.setDisable(false);
    	this.btnDreamTeam.setDisable(false);
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	this.txtResult.clear();
    	String s=this.txtK.getText().trim();
    	if(s.isBlank()) {
    		this.txtResult.setText("K non inserito");
    		return;
    	}
    	
    	Integer k;
    	try {
    		k=Integer.parseInt(s);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Quello inserito non è un numero");
    		return;
    	}
    	
    	if(k<0) {
    		this.txtResult.setText("è stato inserito un numero negativo, ma una dimensione deve essere positiva");
    		return;
    	}
    	
    	if(k>=5) {
    		this.txtResult.setText("Si consiglia di usare valori <5");
    		return;
    	}
    	
    	List<Player> result=this.model.avviaRicorsione(k);
    	if(result==null) {
    		this.txtResult.setText("DreamTeam non trovato");
    		return;
    	}
    	this.txtResult.setText("Dream Team ha grado di totalirità totale pari a: " +this.model.getMaxDreamTeam()+"\n");
    	for(Player p: result) {
    		this.txtResult.appendText(p.toString()+"\n");
    		
    	}
    		
    	return;
    	
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	this.txtResult.clear();
    	Player migliore=this.model.getMigliore();
    	
    	
    		this.txtResult.appendText("Top player: "+migliore.toString()+"\n");
    		List<Battuto> battuti=this.model.getBattuti(migliore);
    		for(Battuto b: battuti) {
    			this.txtResult.appendText(b.toString()+"\n");
    			
    		}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.btnTopPlayer.setDisable(true);
    	this.btnDreamTeam.setDisable(true);
    }
}
