package hangman;

import static hangman.Game.gameOver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GameController {

	private final ExecutorService executorService;
	private final Game game;
	int count =1;
	private String text;
	
	public GameController(Game game) {
		this.game = game;
		executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				return thread;
			}
		});
	}

	@FXML
	private VBox board ;
	@FXML
	private Label statusLabel ;
	@FXML
	private Label enterALetterLabel ;
        @FXML
        private Label letterLabel; //Ayrin: This label is where the program shows the inputs
        @FXML
	private TextField textField ;
        @FXML
        private GridPane wrongLettersGrid; //Ayrin
        @FXML
        private GridPane goodLettersGrid; //Ayrin
        @FXML
        private MenuItem HangmanButton; /* Ayrin: INJECTED THE HangmanButton DEFINED IN 
                                           THE FXML FILE SO AN EVENTHANDLER CAN BE ADDED TO IT */
        
    public void initialize() throws IOException {
		System.out.println("in initialize");
		drawHangman();
		addTextBoxListener();
		setUpStatusLabelBindings();
                game.setHints(goodLettersGrid); /*Ayrin: call to set the hints of the word to guess
                                                 The layout is handled by the goodLettersGrid GRIDPANE*/
                HangmanButton.addEventHandler(ActionEvent.ACTION, (ActionEvent actionEvent) ->{ 
                	//Ayrin: AN EVENTHANDLER IS ADDED IN ORDER TO CALLE THE reset AND setHints METHODS.
					try {
						game.reset(wrongLettersGrid,goodLettersGrid);
						board.getChildren().clear();
						drawHangman();
						count=1;
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					game.setHints(goodLettersGrid);
                });
	}

	private void addTextBoxListener() {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {

				if(newValue.length() > 0) 
				{//Ayrin: added these 3 lines
                                    textField.clear();
   
                                        if(!gameOver){
                                        	//Ayrin: WITH THIS WE MAKE SURE THE makeMove METHOD IS CALLED ONLY IF THE GAME IS NOT OVER.
                                            System.out.print(newValue);                    
                                            game.makeMove(newValue.toLowerCase(),letterLabel,wrongLettersGrid,goodLettersGrid);
                                            //HAZIM:
											System.out.print("haha "+game.gameStatusProperty().getValue());
											if(game.gameStatusProperty().getValue().toString()=="Bad guess..."){
												board.getChildren().clear();
												draw();
												count++;
											}
											//HAZIM:
											else if(game.gameStatusProperty().getValue().toString()=="Game over!"){
												board.getChildren().clear();
												draw2();
											}
                                            // Ayrin: ADDED THE toLowerCase METHOD IN ORDER TO HANDLE THE CASE OF A BLOQ MAYUS ACTIVATED
                                        }
				}
			}
		});
	}

	private void setUpStatusLabelBindings() {

		System.out.println("in setUpStatusLabelBindings");
		statusLabel.textProperty().bind(Bindings.format("%s", game.gameStatusProperty()));
		enterALetterLabel.textProperty().bind(Bindings.format("%s", "Enter a letter:"));
		/*	Bindings.when(
					game.currentPlayerProperty().isNotNull()
			).then(
				Bindings.format("To play: %s", game.currentPlayerProperty())
			).otherwise(
				""
			)
		);
		*/
	}
	//HAZIM
	private void draw2() {
		ImageView imgview = new ImageView();
		imgview.setFitWidth(350);
		imgview.setFitHeight(350);
		Image image6 = new Image("/pics/hangman6.png");
		imgview.setImage(image6);
		board.getChildren().add(imgview);
	}
	//HAZIM
	private void draw() {
		ImageView imgview = new ImageView();
		imgview.setFitWidth(350);
		imgview.setFitHeight(350);
		Image image1 = new Image("/pics/hangman1.png");
		Image image2 = new Image("/pics/hangman2.png");
		Image image3 = new Image("/pics/hangman3.png");
		Image image4 = new Image("/pics/hangman4.png");
		Image image5 = new Image("/pics/hangman5.png");
		Image image6 = new Image("/pics/hangman6.png");



		switch (count) {
			case 1:
				imgview.setImage(image1);
				board.getChildren().add(imgview);
				break;
			case 2:
				imgview.setImage(image2);
				board.getChildren().add(imgview);
				break;
			case 3:
				imgview.setImage(image3);
				board.getChildren().add(imgview);
				break;
			case 4:
				imgview.setImage(image4);
				board.getChildren().add(imgview);
				break;
			case 5:
				imgview.setImage(image5);
				board.getChildren().add(imgview);
				break;

		}
	}
	//HAZIM
	public void drawHangman() {
		ImageView imgview = new ImageView();
		imgview.setFitWidth(350);
		imgview.setFitHeight(350);
		Image image = new Image("/pics/hangman0.png");
		imgview.setImage(image);
		board.getChildren().add(imgview);


	}
		


	@FXML
	private void quit() {
		board.getScene().getWindow().hide();
	}


}