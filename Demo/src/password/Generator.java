package password;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Generator extends Application {
	
	int length = 20;
	int digits = 5;
	int symbols = 5;
	
	// Values used and changed to iterate through colors (rgb)
	int red = 50;
	int green = 210;
	
	// Number randomizer
	Random random = new Random();
	
	// The characters that will be used to generate the password
	final static byte[] NUMBERS = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	final static char[] OTHERS = "!#$%&()*+,-./;<=>?@[]^{}~".toCharArray();
	
	// A string array that will hold the 1000 most common passwords
	List<String> badPasswords = new ArrayList<String>();
	
	TextField textField;
	ProgressBar pwStrength;

	public static void main(String[] args) {
		
		// Launches the UI window
		launch(args);
	}
	
	// MAIN WINDOW
	@Override
	public void start(Stage window) throws Exception {
		
		final int sceneWidth = 400;
		final int sceneHeight = 360;
		
		Scanner sc = new Scanner(getClass().getResourceAsStream("most-common-passwords.txt"));
		
		while (sc.hasNextLine()) {
			badPasswords.add(sc.nextLine());
		}
		
		sc.close();

		window.setMinWidth(sceneWidth);
		window.setMinHeight(sceneHeight);
		
		textField = new TextField(randomizedString());
		textField.setFocusTraversable(false);
		textField.getStyleClass().add("text-field");
		
		textField.setOnKeyReleased(keyy -> {
			
			String passwordChecker = textField.getText();
			
			if (badPasswords.contains(passwordChecker)) {
				
				pwStrength.setProgress(0.04);
				pwStrength.setStyle("-fx-accent: #ff0000");
				
			} else {
				
				length = textField.getText().length();
				passwordStrength();
			}
		});
		
		pwStrength = new ProgressBar(1);
		pwStrength.getStyleClass().add("security-bar");
		pwStrength.setPrefWidth(Double.MAX_VALUE);
		
		final Label lengthName = new Label("length");
		lengthName.getStyleClass().add("label");
		
		final Label digitsName = new Label("digits");
		digitsName.getStyleClass().add("label");
		
		final Label symbolsName = new Label("symbols");
		symbolsName.getStyleClass().add("label");
		
		final Label lengthNumber = new Label("" + length);
		lengthNumber.setPrefWidth(17);
		lengthNumber.getStyleClass().add("label");
		
		final Label digitsNumber = new Label("" + digits);
		digitsNumber.setPrefWidth(17);
		digitsNumber.getStyleClass().add("label");
		
		final Label symbolsNumber = new Label("" + symbols);
		symbolsNumber.setPrefWidth(17);
		symbolsNumber.getStyleClass().add("label");
		
		
		final Slider lengthSlider = new Slider(4, 64, 20);
		lengthSlider.valueProperty().addListener((listener, oldValue, newValue) -> { 

			if (oldValue.intValue() != newValue.intValue()) {
				
				length = newValue.intValue();
				lengthNumber.setText("" + length);
				
				textField.setText(randomizedString());
				
				passwordStrength();
			}
		});
		
		final Slider digitsSlider = new Slider(0, 10, 5);
		digitsSlider.valueProperty().addListener((listener, oldValue, newValue) -> {
			
			if (oldValue.intValue() != newValue.intValue()) {
				
				digits = newValue.intValue();
				digitsNumber.setText("" + digits);
				
				textField.setText(randomizedString());
				
				passwordStrength();
			}
		});
		
		final Slider symbolsSlider = new Slider(0, 10, 5);
		symbolsSlider.valueProperty().addListener((listener, oldValue, newValue) -> {
			
			if (oldValue.intValue() != newValue.intValue()) {
				
				symbols = newValue.intValue();
				symbolsNumber.setText("" + symbols);
				
				textField.setText(randomizedString());
				
				passwordStrength();
			}
			
		});
		
		final Button button = new Button("   Regenerate Password   ");
		button.setOnAction(event -> {
			
			length = lengthSlider.valueProperty().intValue();
			
			textField.setText(randomizedString());
			
			passwordStrength();
		});
		
		
		final Button save = new Button("   Save   ");
		save.getStyleClass().add("save-button");
		save.setOnAction(event -> {
			
			File loginFile = new File("password.txt");
			
			try {
				FileWriter fw = new FileWriter(loginFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				
				bw.write("password");
				bw.newLine();
				bw.newLine();
				bw.write(textField.getText());
				bw.close();
				
				SuccessAlert.success();
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
			
		});

		
		final VBox sliderLayout = new VBox(20);
		sliderLayout.getChildren().addAll(lengthName, digitsName, symbolsName);
		sliderLayout.setAlignment(Pos.CENTER_RIGHT);
		
		final VBox sliderLayout2 = new VBox(20);
		sliderLayout2.getChildren().addAll(lengthSlider, digitsSlider, symbolsSlider);
		
		final VBox sliderLayout3 = new VBox(20);
		sliderLayout3.getChildren().addAll(lengthNumber, digitsNumber, symbolsNumber);
		sliderLayout3.setAlignment(Pos.CENTER_RIGHT);
		
		final HBox sliderHBox = new HBox(20);
		
		HBox.setHgrow(sliderLayout2, Priority.ALWAYS);
		HBox.setMargin(sliderLayout2, new Insets(2, 0, 0, 0));
		
		sliderHBox.getChildren().addAll(sliderLayout, sliderLayout2, sliderLayout3);
		
		final HBox textHBox = new HBox(20);
		HBox.setHgrow(textField, Priority.ALWAYS);
		textHBox.getChildren().addAll(textField, save);
		
		final VBox layout = new VBox(20);
		
		VBox.setMargin(textHBox, new Insets(0, 20, 0, 20));
		VBox.setMargin(pwStrength, new Insets(0, 20, 0, 20));
		VBox.setMargin(sliderHBox, new Insets(30, 20, 0, 20));
		
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(textHBox, pwStrength, button, sliderHBox);
		
		Scene scene = new Scene(layout, sceneWidth, sceneHeight);
		
		scene.getStylesheets().add("styling.css");
		scene.getRoot().applyCss();
		
		window.setScene(scene);
		window.setTitle("Safety Safe");
		window.show();
	}
	
	
	/* Determines the strength of the generated password and
	 * changes the progress bar length and color depending on how secure it is */
	private void passwordStrength() {
		
		pwStrength.setProgress((0.2 + ((length - 4) * 0.05)));
		
		if (length <= 12) {
			
			red = 210;
			green = 50 + ((length - 4) * 20);
			
		} else if (length < 20) {
			
			red = 50 + ((20 - length) * 20);
			green = 210;
			
			if (digits + symbols >= 4 && digits + symbols <= 8) {
				pwStrength.setProgress(pwStrength.getProgress() + 0.1);
				red -= 40;
			}
			
		} else {
			red = 50;
			green = 210;
		}

		pwStrength.setStyle("-fx-accent: rgb(" + red + "," + green + ",0);");
	}
	
	
	/* Generates the password and calls randomPositions() to determine 
	 * where the digits and symbols will go in the string */
	private String randomizedString() {
		
		final ArrayList<Integer> nonRepeating = new ArrayList<>();
		
		for (int i = 0; i < length; i++) {
			nonRepeating.add(i);
		}
		
		ArrayList<Integer> digitPositions = new ArrayList<>();
		ArrayList<Integer> symbolPositions = new ArrayList<>();
		
		if (digits > 0) {
			
			digitPositions = randomPositions(digits, nonRepeating);
			//System.out.println(digitPositions);
		}
		
		if (symbols > 0) {
			
			symbolPositions = randomPositions(symbols, nonRepeating);
			//System.out.println(symbolPositions);
		}
		
		final StringBuilder passwordBuilder = new StringBuilder();
		
		for (int checker = 0; checker < length; checker++) {
			
			if (digitPositions.contains(checker)) {
			
				passwordBuilder.append(NUMBERS[random.nextInt(NUMBERS.length)]);
				
			} else if (symbolPositions.contains(checker)) {
				
				passwordBuilder.append(OTHERS[random.nextInt(OTHERS.length)]);
				
			} else {
				
				passwordBuilder.append(ALPHABET[random.nextInt(ALPHABET.length)]);
			}
		}
		
		//System.out.println(length);
		return passwordBuilder.toString();
	}
	
	
	/* Determiness where the digits and symbols will go in the string */
	private ArrayList<Integer> randomPositions(int amount, ArrayList<Integer> numArray) {
		
		final ArrayList<Integer> randoms = new ArrayList<>();
		
		for (int i = 0; i < amount; i++) {
			
			if (numArray.size() == 0) {
				randoms.sort(null);
				return randoms;
			}
			
			final int randomInt = random.nextInt(numArray.size());
			
			randoms.add(numArray.get(randomInt));
			numArray.remove(randomInt);
		}
		
		randoms.sort(null);
		return randoms;
	}
}
