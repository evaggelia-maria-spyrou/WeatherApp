package application.fileHandler;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CityFile {

	public static void addCity(String city) {
		FileWriter fWrite = null;
		BufferedWriter bWrite = null;
		String trimmedCity = city.trim();
		File myFile = new File("cities.txt");
		try {
			if (!(myFile.exists())) { // checking file exist or not
				myFile.createNewFile();
			}
			fWrite = new FileWriter(myFile, true); // true for appending content to the existing file
			bWrite = new BufferedWriter(fWrite);
			bWrite.write(trimmedCity + "\r\n");
			bWrite.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fWrite != null)
				try {
					fWrite.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (bWrite != null)
				try {
					bWrite.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static List<String> getCities() {
		BufferedReader br = null;
		List<String> cities = new ArrayList<>();
		File f = new File("cities.txt");
		try {
			if (!(f.exists())) { // checking file exist or not
				f.createNewFile();
			}
			FileReader myFile = new FileReader(f);
			br = new BufferedReader(myFile);
			String line = null;
			while ((line = br.readLine()) != null) {
				cities.add(line);
			}
			return cities;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static void deleteCity(String cityToRemove) {
		File temp = new File("temp.txt");
		File f = new File("cities.txt");
		FileWriter fWrite = null;
		FileReader fr;
		BufferedWriter bWrite = null;
		BufferedReader br = null;
		Boolean check = false;
		try {
			fr = new FileReader(f);
			fWrite = new FileWriter(temp);
			bWrite = new BufferedWriter(fWrite);
			br = new BufferedReader(fr);
			String currentLine;
			while ((currentLine = br.readLine()) != null) {    
				String trimmedLine = currentLine.trim();
				if (!trimmedLine.equals(cityToRemove)) {            //if the line does not equal to city we want to remove, it is copied to temp file
					bWrite.write(currentLine + System.getProperty("line.separator"));
				}
			}
			check = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bWrite != null)
				try {
					bWrite.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (check == true) {
				f.delete();              //delete cities file
			    temp.renameTo(f);        //rename temp to "cities.txt"
			}
			if (fWrite != null)
				try {
					fWrite.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

}
