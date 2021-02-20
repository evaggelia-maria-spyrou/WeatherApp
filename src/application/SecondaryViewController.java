package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.cloudmersive.client.invoker.ApiException;

import application.fileHandler.CityFile;
import exception.WeatherAPIException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.DailyWeatherInfo;
import model.HourlyWeatherInfo;
import model.WeatherAPI;
import services.WeatherAPIService;

public class SecondaryViewController implements Initializable{

	// for City List
    @FXML
    private ComboBox<String> cb_city;
    
    // for forecast type choice
    @FXML
    private ComboBox<String> cb_forecast;
    
    ObservableList<String> forecastTypeList = FXCollections.observableArrayList("Current", "Hourly", "Daily"); 
    
    // for Units
    @FXML
    private Label lbl_unit;
    
    @FXML
    private ToggleGroup tg_unit;
    
    @FXML
    private RadioButton rb_celsius;

    @FXML
    private RadioButton rb_fahrenheit;
    
    //for all results
    @FXML
    private Label lbl_results;
    
    final WeatherAPIService weatherAPIService  = WeatherAPI.getWeatherAPIService();
    

	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		loadCityList(); 
		loadForecastTypeList();
	}
    
	
    // for City List
	private static ObservableList<String> getCityList() {  
		List<String> cityList = CityFile.getCities();
		ObservableList<String> list = FXCollections.observableArrayList(cityList);                                                
	    return list;
	 }
    
	private void loadCityList() {
		cb_city.setItems(getCityList());
	} 
	
	@FXML
	public void addCity(ActionEvent event) {
		String city = cb_city.getValue();
		if (validateCityInput() && checkCityExists()) {
			CityFile.addCity(city);
			loadCityList();
			JOptionPane.showMessageDialog(null, "City added succesfully");
		}	
	}
	
	@FXML
	public void deleteCity(ActionEvent event) {
	   String city = cb_city.getValue();
		if (validateCityInput()) {
			CityFile.deleteCity(city);
			loadCityList();
			JOptionPane.showMessageDialog(null, "City deleted succesfully");
		}	
	}
	
	// for Forecast Type List
	private void loadForecastTypeList() {
		cb_forecast.setItems(forecastTypeList);
	}
	
	
    // get selected unit 
    public String getUnit() {
    	String unit = null;
    	if (tg_unit.getSelectedToggle().equals(rb_celsius)) {
    		lbl_unit.setText("Temperature: Celsius , Wind Speed: metre/sec");
    		unit = "metric";
    	}
    	if (tg_unit.getSelectedToggle().equals(rb_fahrenheit)) {
    		lbl_unit.setText("Temperature: Fahrenheit , Wind Speed: miles/hour");
    		unit = "imperial";
    	}
    	return unit;
    }
    

    
    // get Weather Results
    private void getCurrentWeatherResults(String city, String unit) throws ApiException {
	String result;
	try {
		result = weatherAPIService.getCurrentWeatherForCity(city, unit);
		lbl_results.setText(result);
	} catch (WeatherAPIException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} 
    }
    
    
    private void getHourlyWeatherResults(String city, String unit) throws ApiException, IOException {
	try {
		List<HourlyWeatherInfo> results = weatherAPIService.getHourlyWeatherForCity(city, unit);

		lbl_results.setText(results.toString());
	} catch (WeatherAPIException e) {
		e.printStackTrace();
	}
    }
    
    private void getDailyWeatherResults(String city, String unit) throws ApiException, IOException {
	try {
		List<DailyWeatherInfo> results = weatherAPIService.getDailyWeatherForCity(city, unit);
		
		lbl_results.setText(results.toString());
	} catch (WeatherAPIException e) {
		e.printStackTrace();
	}
    }
    
    // search for user's request
    @FXML
    public void search(ActionEvent event) throws Exception {
    	String city = cb_city.getValue();
		String unit = getUnit();
		String forecast = cb_forecast.getValue();
    	if(validateCityInput() && validateForecastTypeInput()) {
    		try {
    			if(forecast == "Current") {
    				getCurrentWeatherResults(city, unit);
    			} else if (forecast == "Hourly") {
    				getHourlyWeatherResults(city, unit);
    			} else {
    				getDailyWeatherResults(city, unit);
    			}	
    		} catch (IOException e) {
    			e.printStackTrace();
    		} catch (ApiException e) {
    			e.printStackTrace();
    		} catch (Exception e) {
    			errorMessageCity();
    		}
    	}	
    }
    
	// return to previews window
	public void goBack(ActionEvent event) {
    	try {
    	// this line hides previous window
		((Node)event.getSource()).getScene().getWindow().hide(); 
		Stage primaryStage = new Stage();
		// this line redirects to PrimaryView window
		Parent root = FXMLLoader.load(getClass().getResource("/application/PrimaryView.fxml")); 
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
    	} catch (Exception e) {
    		
    	}
	}
	
    //validations and errors
    private boolean validateCityInput() {
    	String city = cb_city.getValue();
    	if (city.isEmpty() | city.isBlank()) {
    		Alert alert = new Alert (AlertType.WARNING);
       	    alert.setTitle("validate fields");
       	    alert.setHeaderText(null);
       	    alert.setContentText("Please select a city");
       	    alert.showAndWait();
    	    return false;
    	} 
    	return true;
    }
    
    private boolean checkCityExists() {
    	String city = cb_city.getValue();
		try {
			weatherAPIService.getCurrentWeatherForCity(city, "metric");
			return true;
		} catch (Exception e) {
			errorMessageCity();
		return false;
		}	 
	}
    
    private boolean validateForecastTypeInput() {
    	String type = cb_forecast.getValue();
    	if (type == null) {
    		Alert alert = new Alert (AlertType.WARNING);
       	    alert.setTitle("validate fields");
       	    alert.setHeaderText(null);
       	    alert.setContentText("Please select the Forecast type");
       	    alert.showAndWait();
    	    return false;
    	} 
    	 return true;
    } 
    	 
    private void errorMessageCity() {
    		Alert alert = new Alert (AlertType.ERROR);
       	    alert.setTitle("Invalidate City");
       	    alert.setHeaderText("Enter a valide City");
       	    alert.setContentText("The input must be in the form:  City, CountryCode" + "\r\n" + "e.g. London, GB");
       	    alert.showAndWait();
    	}
    
	
	
	
	
}
