package application;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;

import java.util.Collections;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * This program animates bubble sort to help the user visualize how it works.
 * 
 * @author Daniel.Xu
 * @version 1.0
 */
public class Main extends Application {
	// Instance variables
	Random rand = new Random();

	int rectWidth = 40; // width of rectangle

	Duration duration = Duration.millis(300); // Duration of animations

	Timeline timeline; // Animation timeline

	// Color of the rectangle
	Color c1 = Color.web("#FFED65");
	Color c2 = Color.web("#59FFA0");

	// Creates array lists to store the data and rectangles
	ArrayList<Integer> array;
	ArrayList<Rectangle> rectangleList;
	ArrayList<Integer> rectanglePosList;

	int currentNumber;
	boolean endSort = false;
	boolean firstRun = true;

	/**
	 * This methods runs the program. It displays everything and controls all action
	 * events.
	 */
	@Override
	public void start(Stage primaryStage) {
		currentNumber = 0;
		// Generates a list upon the start of the program, and store rectangle based on
		// the values into another array list.
		rectangleList = new ArrayList<Rectangle>();
		rectanglePosList = new ArrayList<Integer>();
		array = new ArrayList<Integer>();
		for (int i = 0; i < rand.nextInt((8 - 5) + 1) + 5; i++) {
			array.add(rand.nextInt((10 - 5) + 1) + 5);
			rectangleList.add(new Rectangle(0, 50, rectWidth, array.get(i) * 50));
			rectanglePosList.add(0);
		}

		try {

			// Title
			HBox title = new HBox();
			Text titleText = new Text("Sorting Visualization");
			titleText.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
			titleText.setFill(Color.WHITE);

			title.setStyle("-fx-border-color: black; -fx-border-width: 0; -fx-background-color: #50514F");
			title.setAlignment(Pos.CENTER);
			title.setPadding(new Insets(80, 50, 50, 50));

			title.getChildren().addAll(titleText);

			// Visual Display
			HBox visual = new HBox();
			visual.setAlignment(Pos.CENTER);
			visual.setPadding(new Insets(50));
			visual.setSpacing(50);
			visual.setStyle(
					"-fx-border-color: black; -fx-border-width: 0; -fx-border-style: hidden solid solid hidden; -fx-background-color: #FDFFF7");

			// Displays the rectangles
			for (int i = 0; i < rectangleList.size(); i++) {
				Rectangle rect = rectangleList.get(i);
				rect.setFill(c2);
				rect.setArcHeight(30);
				rect.setArcWidth(30);
				visual.getChildren().add(rect);
			}

			// User controls
			VBox userControl = new VBox();
			userControl.setPadding(new Insets(50, 50, 50, 80));
			userControl.setSpacing(50);
			userControl.setAlignment(Pos.CENTER);
			userControl.setStyle(
					"-fx-border-color: black; -fx-border-width: 0; -fx-border-style: hidden solid solid solid; -fx-background-color: #B4ADEA");

			Button nextStep = new Button("Next Step"); // Creates a button that sort one step at a time
			Button autoSort = new Button("Auto Sort"); // Creates a button that auto sorts
			Button instantSort = new Button("Instant Sort"); // Creates a button that instantly sorts the list
			Button generateList = new Button("Generate a new list"); // Creates a button that produces a new list

			instantSort.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					instantSort();
				}
			});

			nextStep.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					nextStep();
				}
			});

			autoSort.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					// Disable the buttons during animation
					nextStep.setDisable(true);
					instantSort.setDisable(true);
					generateList.setDisable(true);
					autoSort.setDisable(true);

					// Plays the animation until the list is sorted fully
					Timeline autoSorttl = new Timeline(new KeyFrame(duration, new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							nextStep();
						}
					}));
					autoSorttl.setCycleCount(count() + 1);
					autoSorttl.play();

					// Enable the buttons again after animation finishes
					autoSorttl.setOnFinished(e -> {
						nextStep.setDisable(false);
						instantSort.setDisable(false);
						generateList.setDisable(false);

					});
				}
			});

			generateList.setOnAction(e -> {
				firstRun = true;

				autoSort.setDisable(false);

				// Clear previous visuals
				visual.getChildren().clear();

				// Generate new list
				rectangleList = new ArrayList<Rectangle>();
				rectanglePosList = new ArrayList<Integer>();
				array = new ArrayList<Integer>();
				for (int i = 0; i < rand.nextInt((8 - 5) + 1) + 5; i++) {
					array.add(rand.nextInt((10 - 5) + 1) + 5);
					rectangleList.add(new Rectangle(500, 50, rectWidth, array.get(i) * 50));
					rectanglePosList.add(0);
				}

				// Display the new rectangles
				for (int x = 0; x < rectangleList.size(); x++) {
					Rectangle rect = rectangleList.get(x);
					rect.setFill(c2);
					rect.setArcHeight(30);
					rect.setArcWidth(30);
					visual.getChildren().add(rect);
				}

			});

			userControl.getChildren().addAll(nextStep, autoSort, instantSort, generateList);

			// Overall Layout
			BorderPane root = new BorderPane();
			root.setTop(title);
			root.setLeft(userControl);
			root.setCenter(visual);

			Scene scene = new Scene(root, 1200, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("BubbleSortVisualizer");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method performs a single step of the bubble sort.
	 * 
	 */
	private void nextStep() {

		// If program first start or a new list is generated, sort from the beginning
		if (firstRun) {
			currentNumber = 0;
			firstRun = false;
		}

		// If the current number is over bound, reset it
		if (currentNumber >= array.size() - 1) {
			currentNumber = 0;
		}

		// Resets color
		for (int x = 0; x < array.size(); x++) {
			rectangleList.get(x).setFill(c2);
		}

		int i = currentNumber;

		timeline = new Timeline();

		// Color in the rectangles
		rectangleList.get(i).setFill(c1);
		rectangleList.get(i + 1).setFill(c1);

		// Check if swaps need to be made
		if ((array.get(i) > array.get(i + 1))) {

			// Animates the swaps
			rectanglePosList.set(i, rectanglePosList.get(i) + 90);
			KeyValue trans1 = new KeyValue(rectangleList.get(i).translateXProperty(), rectanglePosList.get(i));

			rectanglePosList.set(i + 1, rectanglePosList.get(i + 1) - 90);

			KeyValue trans2 = new KeyValue(rectangleList.get(i + 1).translateXProperty(), rectanglePosList.get(i + 1));

			KeyFrame kf = new KeyFrame(duration, trans1, trans2);
			timeline.getKeyFrames().add(kf);

			Collections.swap(array, i, i + 1);
			Collections.swap(rectangleList, i, i + 1);
			Collections.swap(rectanglePosList, i, i + 1);
		}

		timeline.play();

		currentNumber += 1;
	}

	/**
	 * This method instantly sorts the list sorted
	 * 
	 */
	private void instantSort() {

		endSort = false;

		// Continue looping until everything is sorted
		while (!endSort) {

			endSort = true;

			if (currentNumber >= array.size() - 1) {
				currentNumber = 0;
			}

			int i = currentNumber;

			if (array.get(i) > array.get(i + 1)) {

				Translate trans1 = new Translate();
				Translate trans2 = new Translate();
				trans1.setX(90);
				trans2.setX(-90);
				rectangleList.get(i).getTransforms().add(trans1);
				rectangleList.get(i + 1).getTransforms().add(trans2);

				Collections.swap(array, i, i + 1);
				Collections.swap(rectangleList, i, i + 1);

			}
			currentNumber += 1;

			// Check if the array is sorted, is so exit the loop
			for (int e = 0; e < array.size() - 1; e++) {
				if (array.get(e) > array.get(e + 1)) {
					endSort = false;
				}
			}
		}
	}

	/**
	 * This method calculates the number of steps required to sort the list and
	 * return the value as count
	 * 
	 * @return count
	 */
	private int count() {
		int count = 0;
		endSort = false;
		ArrayList<Integer> arrayClone = (ArrayList<Integer>) array.clone();

		// Continue looping until everything is sorted
		while (!endSort) {

			endSort = true;

			if (firstRun) {
				currentNumber = 0;
				firstRun = false;
			}

			// If the current number is over bound, reset it
			if (currentNumber >= array.size() - 1) {
				currentNumber = 0;
			}

			int i = currentNumber;

			// Check if swaps need to be made
			if ((arrayClone.get(i) > arrayClone.get(i + 1))) {
				Collections.swap(arrayClone, i, i + 1);
			}

			currentNumber += 1;

			// Check if the array is sorted, is so exit the loop
			for (int e = 0; e < arrayClone.size() - 1; e++) {
				if (arrayClone.get(e) > arrayClone.get(e + 1)) {
					endSort = false;
				}
			}
			count++;
		}
		firstRun = true;
		return count;
	}

	/**
	 * This method launches the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
