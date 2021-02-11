package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.cloudmersive.client.invoker.ApiException;

import exception.WeatherAPIException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.WeatherAPI;
import services.WeatherAPIService;

public class PrimaryViewController implements Initializable{
	
    @FXML
    private Label lbl_results;
    
    @FXML
    private Label lbl_unit;

    @FXML
    private ToggleGroup tg_unit;
    
    @FXML
    private RadioButton rb_celsius;

    @FXML
    private RadioButton rb_fahrenheit;
    
    
    final WeatherAPIService weatherAPIService  = WeatherAPI.getWeatherAPIService();
    
	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	loadResultsByUnit();
		
	}

    // set Current Weather Results By IP for given unit
    private void getResults(String unit) {
	String result;
	try {
		result = weatherAPIService.getCurrentWeatherByIP(unit);
		lbl_results.setText(result);
	} catch (WeatherAPIException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ApiException e) {
		e.printStackTrace();
	}
    }
    
    // get selected unit and load Current Weather Results
    public void loadResultsByUnit() {
    	if (tg_unit.getSelectedToggle().equals(rb_celsius)) {
    		lbl_unit.setText("Temperature: Celsius , Wind Speed: metre/sec");
    		getResults("metric");
    	}
    	if (tg_unit.getSelectedToggle().equals(rb_fahrenheit)) {
    		lbl_unit.setText("Temperature: Fahrenheit , Wind Speed: miles/hour");
    		getResults("imperial");
    	}
    }
    
    // redirect to new window to search results by city
    @FXML
    public void goToSearchByCityWindow(ActionEvent event) throws Exception {
			// this line hides previous window
			((Node)event.getSource()).getScene().getWindow().hide();          
			Stage primaryStage = new Stage();
			// this line redirects to Secondary window
			Parent root = FXMLLoader.load(getClass().getResource("/application/SecondaryView.fxml")); 
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
    }
    
}
