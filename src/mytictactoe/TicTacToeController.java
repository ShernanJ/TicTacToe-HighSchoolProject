
/*
	Name: TicTacToeController.java
	Author: Shernan Javier
	Date: May 2, 2019
	Version: 1.0
	Description: A simple Tic Tac Toe game created for my High School project.
	Player is able to play against CPU or against a human player.
	When program initially runs, it is set to Human versus Human. To set to play against CPU, press
	"Opponent" Menu, and click on either "CPU Easy" or "CPU Hard". Having different difficulties.
	CPU Easy is purely based on 'RNG'(Random Number Generator) while CPU Hard is also RNG but
	also blocks the user from being able complete the game in certain ways.
 */

package mytictactoe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

//TicTacToeController Class
public class TicTacToeController {
	
	//Initialize Buttons and other objects from FXML files.
	@FXML Button b1;
	@FXML Button b2;
	@FXML Button b3;
	@FXML Button b4;
	@FXML Button b5;
	@FXML Button b6;
	@FXML Button b7;
	@FXML Button b8;
	@FXML Button b9;
	@FXML Button howToButton;
	@FXML Button closeButton;
	@FXML Button quitButton;
	@FXML Text playerTwo;
	@FXML GridPane gameBoard;
	@FXML Text playerOneScore;
	@FXML Text playerTwoScore;
	@FXML Text playerTurn;
	
	//Initialize Stages
	static Stage secondaryStage;
	static Stage winnerStage;
	static Stage cpuStage;
	static Stage aboutStage;
	
	//Initialize Variables.
	public int p1Score;
	public int p2Score;
	public String array[] = new String [9]; 
	private boolean isFirstPlayer = true;
	private boolean gameActive = true;
	private boolean cpuPlayer = false;
	private boolean cpuTurn = false;
	private boolean inputting = true;
	public boolean hard = false;
	private boolean loop = true;
	private boolean randPlace = false;
	private boolean disabled = false;
	private boolean buttonClicked = false;

	//buttonClickHanlder method
	//Handles the main game's button clicks.
	//Differentiate which player is going and sets the proper
	//Symbol on the button.
	public void buttonClickHandler(ActionEvent evt) {
		if (gameActive == true) { //Blocks the placement of symbols and button clicks when disabled.
			//Get the text of the button clicked.
			Button clickedButton = (Button) evt.getTarget();
			String buttonLabel = clickedButton.getText();
			String buttonPressed;
			
			//First Player.
			//Checks if the button is empty, if meets requirements then sets button to X.
			if ("X".equals(buttonLabel) && isFirstPlayer && cpuTurn == false && inputting ==true) {
				clickedButton.setText("X");
				buttonPressed = "X";
				buttonClicked = true;
				isFirstPlayer = false;
				determineWinner(buttonPressed); //Checks if there is a winner.
				playerTurn.setText("Player 2 Turn");
				
				//Easy CPU.
				//Checks if player is playing against an Easy CPU.
				if (cpuPlayer == true && inputting == true && !hard) {
					buttonPressed = "O";
					randomNumberGeneratorCPU(); //Runs the easyCPU method.
					playerTurn.setText("Player 1 Turn");
				}//End Easy CPU. 
				
				//Hard CPU.
				//Checks if player is playing against an Hard CPU.
				if (cpuPlayer == true && inputting == true && hard == true) {
					buttonPressed = "O";
					hardCPU(); //Runs the hardCPU method.
					playerTurn.setText("Player 1 Turn");
				}//End Hard CPU.
				
				//If disabled is true, blocks the method from being called.
				if(disabled == false) {
				determineWinner(buttonPressed); //Checks if there is a winner.
				}//Close disabled If statement.
			
			//Second Player.
			//Checks if the button is empty, and if the opponent isn't a cpu. Therefore it is a human player.
			} else if ("O".equals(buttonLabel) && !isFirstPlayer && cpuPlayer == false) {
				clickedButton.setText("O");
				isFirstPlayer = true;
				buttonPressed = "O";
				buttonClicked = true;
				determineWinner(buttonPressed);	//Checks if there is a winner.
				playerTurn.setText("Player 1 Turn");
			}//Close player 2 if statement.
		}//Close gameActive statement.
	}//Close buttonClickHandler method.
	
	//buttonHover Method. It is called when user hovers over a button.
	public void buttonHover(MouseEvent evt) {
		Button hoveredButton = (Button)evt.getTarget();
		String buttonLabel = hoveredButton.getText();
		
		if(isFirstPlayer && "".equals(buttonLabel) && gameActive == true) { //Checks if player 1 is going and if the button is empty.
			hoveredButton.setText("X"); //Sets the button label to X.
			buttonClicked = false; //Button isn't clicked yet therefore it is set to false.
		}
		if(isFirstPlayer == false && "".equals(buttonLabel) && gameActive == true) { //Checks if player 2 is going and if the button is empty.
			hoveredButton.setText("O"); //Sets the button label to O.
			buttonClicked = false; //Button isn't clicked yet therefore it is set to false.
		}
		if(!"".equals(buttonLabel) && gameActive == true) { //Checks if the button is not empty.
			buttonClicked = true; //If button is not empty, therefore it was set already therefore not being able to be cleared.
		}	
	}//Close buttonHover method.
	
	//buttonHoverOff Method. Is called when user hovers off button. 
	public void buttonHoverOff(MouseEvent evt) {
		Button hoveredButton = (Button)evt.getTarget();
		String buttonLabel = hoveredButton.getText();
		
		//Checks if player 1 is going, if buttonHover sets the button to X, and button isn't clicked, it clears it.
		if(isFirstPlayer == true && "X".equals(buttonLabel) && buttonClicked == false && gameActive == true) {
			hoveredButton.setText("");
		}
		//Checks if player 2 is going, if buttonHover sets the button to O, and button isn't clicked, it clears it.
		if(isFirstPlayer == false && "O".equals(buttonLabel) && buttonClicked == false && gameActive == true) {
			hoveredButton.setText("");
		}
	}//Close buttonHoverOff.
	
	//Menu button click handler. Gets the button's text and compares to certain requirements.
	public void menuClickHandler(ActionEvent evt) {
		MenuItem clickedMenu = (MenuItem) evt.getTarget();//Grabs the menu button value.
		String menuLabel = clickedMenu.getText(); //Sets the menu button value into a String.
		
		//If button is Restart, it clears the board game.
		if("Restart".equals(menuLabel)) {
			gameActive = true; //Disables Game.
			inputting = true; //Disables Inputting.
			disabled = false; //Disables game from winning twice.
			isFirstPlayer = true; //Sets the turn to first player.
			playerTurn.setText("Player 1 Turn");
			clearGame(); //Calls clear game method.
		 }//Close if Restart.
		
		//If button is How to Play, it opens up the How to Window / Instructions.
		if("How to Play".equals(menuLabel)) {
			openHowToWindow(); //Calls openHowToWindow method.
		}//Close if How To Play
		
		//If button is About, it opens the About Window.
		if("About".equals(menuLabel)) {
			aboutWindow(); //Calls aboutWindow method.
		}//Close if About.
		
		//If button is Quit, it quits the game.
		if("Quit".equals(menuLabel)) {
			System.out.println("QUIT");
			Platform.exit();				// Learned from "https://stackoverflow.com/questions/13567019/close-fxml-window-by-code-javafx"
		}//Close if Quit.
		
		//If button is Reset Score, it resets the score of the players.
		if("Reset Score".equals(menuLabel)) {
			playerOneScore.setText("0");
			playerTwoScore.setText("0");
		}//Close reset score.
		
		//If button is Human Player, sets the Opponent as a second player used by the user.
		if("Human Player".equals(menuLabel)) {
			playerTwo.setText("Player 2"); //Sets the Opponent text as Player on the game.
			cpuPlayer = false;
			gameActive = true;
			inputting = true;
			disabled = false;
			isFirstPlayer = true;
			playerTurn.setText("Player 1 Turn");
			clearGame();
		}//Close if Human Player
		
		//If button is CPU Easy, sets the Opponent as an Easy CPU.
		if("CPU Easy".equals(menuLabel)) {
			playerTwo.setText("CPU Easy"); //Sets the Opponent text as CPU Easy on the game.
			cpuPlayer = true;
			gameActive = true;
			inputting = true;
			disabled = false;
			isFirstPlayer = true;
			hard = false;
			playerTurn.setText("Player 1 Turn");
			clearGame(); //Clears the game.
		}//Close if CPU Easy
		
		//If button is CPU Hard, sets the Opponent as a Hard CPU.
		if("CPU Hard".equals(menuLabel)) {
			playerTwo.setText("CPU Hard"); //Sets the Opponent text as CPU Hard on the game.
			cpuPlayer = true;
			gameActive = true;
			inputting = true;
			disabled = false;
			isFirstPlayer = true;
			hard = true;
			playerTurn.setText("Player 1 Turn");
			clearGame();
		}//Close if CPU Hard.
	}//Close menuClickHandler method.
	
	//Button Handler for Winner Screen.
	public void winnerButtonHandler(ActionEvent evt) {
		//Gets the clicked button value/text.
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();
		
		//If button is Close, closes winner window.
		if("Close".equals(buttonLabel)) {
			gameActive = false;
			inputting = false;
			cpuTurn = false;
			winnerStage.close();
		}//Close if Close.
		
		//If button is Quit, Quits game.
		if("Quit".equals(buttonLabel)) {
			winnerStage.close();
			Platform.exit();
		}//Close if Quit.
	}//Close winnerButtonHandler method	
	
	//closeWindowButtonClickHandler Method, close How to Window.
	public void closeWindowButtonClickHandler(ActionEvent evt) {
		//Make sure the name of the stage is same as in openHowToWindow()
		secondaryStage.close(); //Closes How to Window.
	}//Close closeWindowButtonClickHandler Method.
	
	//Close about window method.
	public void closeAboutHandler(ActionEvent evt) {
		aboutStage.close(); //Closes about Window.
	}//Close closeAboutHandler method.
	
	//DetermineWinner Method, determines winner.
	private boolean determineWinner(String buttonLabel) {
		getButtons(); //calls getButtons method.
		//Goes through each individual element in the array. Set up in a way to go through each column before moving on to the next column to check the next set
		//of columns.
		
		//For loop to check each row.
		for(int i = 0; i <= 6; i = i + 3) {
			//For loop to check each column.
			for(int j = 0; j <= 2; j++) {
				int a = j+3; //a is the button directly underneath the button being checked in the same column.
				int b = a+3; //b is 2 buttons directly underneath the button being checked in the same column.
				int k = i+1; //k is the button directly to the right of the button being checked in the same row.
				int l = k+1; //l is 2 buttons directly to the right of the button being checked in the same row. 
				
				//Checks the row on column 'i'. If all buttons match with the same text except for empty. Declares Winner.
				if(buttonLabel.equals(array[i]) && array[i] == array[k] && array[k] == array[l]) {
					switch(i) { //Checks which column the row is in.
						case 0:
							highlightWinningCombo(b1,b2,b3); //Highlights row in first column.
						break;
						case 3:
							highlightWinningCombo(b4,b5,b6); //Highlights row in second column.
						break;
						case 6:
							highlightWinningCombo(b7,b8,b9); //Highlights row in third column.
						break;
					}
					//[X][X][X]
					if("X".equals(buttonLabel)) { //If the rows are filled with X.
						gameActive = false;
						cpuTurn = false;
						inputting = false;
						disabled = true;
						p1Score++; //Add extra point to player one score
						playerOneScore.setText(String.valueOf(p1Score)); //Sets playerOneScore text to new score.
							//Ends 'For Loop'.
							i = 6; 
							j = 2;
						xIsWinner(); //Calls xIsWinner Method.
						return true;
					}//Closes if X.
					
					//[O][O][O]
					if("O".equals(buttonLabel)) { //If the rows are filled with O.
						gameActive = false;
						cpuTurn = false;
						inputting = false;
						disabled = true;
						p2Score++; //Add extra point to player two score
						playerTwoScore.setText(String.valueOf(p2Score)); //Sets playerTwoScore text to new score.
						//Ends 'For Loop'.
						i = 6;
						j = 2;
						oIsWinner(); //Calls oIsWinner Method.
						return true;
					}//Closes if O.
				}
				
				//Checks the Column on Row 'j'. If all buttons match with the same text except for empty. Declares Winner.
				if(buttonLabel.equals(array[j]) && array[j] == array[a] && array[a] == array[b]) {
					switch(j) { //Checks which row the column is in.
						case 0:
							highlightWinningCombo(b1,b4,b7); //Highlights column in first row.
						break;
						case 1:
							highlightWinningCombo(b2,b5,b8); //Highlights column in second row.
						break;
						case 2:
							highlightWinningCombo(b3,b6,b9); //Highlights column in third row.
						break;
					}
					//[X]
					//[X]
					//[X]
					if("X".equals(buttonLabel)) { //If the columns are filled with X.
						gameActive = false;
						cpuTurn = false;
						inputting = false;
						disabled = true;
						p1Score++; //Add extra point to player one score
						playerOneScore.setText(String.valueOf(p1Score)); //Sets playerOneScore text to new score.
						
						//Ends 'For Loops'.
						i = 6;
						j = 2;
	
						xIsWinner(); //Calls xIsWinner method.
						return true;
					}
					//[O]
					//[O]
					//[O]
					if("O".equals(buttonLabel)) { //If the columns are filled with O.
						gameActive = false;
						cpuTurn = false;
						inputting = false;
						disabled = true;
						p2Score++; //Add extra point to player two score
						playerTwoScore.setText(String.valueOf(p2Score)); //Sets playerTwoScore text to new score.
						//Ends 'For Loops'.
						i = 6;
						j = 2;
						
						oIsWinner(); //Calls oIsWinner method.
						return true;
					} //Closes if O.
				}//Closes check Columns.
			}//Closes For j Loop. 
		}//Closes For i loop.
		
		//Checks for top left to bottom right diagonal.
		if (buttonLabel == b1.getText() && b1.getText() == b5.getText() && b5.getText() == b9.getText()) {
			highlightWinningCombo(b1,b5,b9); //Highlights Top Left, Middle, and Bottom Right buttons.
			//[X]
			//	 [X]
			//	    [X]
			if("X".equals(buttonLabel)) { //Checks if button label is X.
				gameActive = false;
				cpuTurn = false;
				inputting = false;
				disabled = true;
				p1Score++; //Add extra point to player one score
				playerOneScore.setText(String.valueOf(p1Score)); //Sets playerOneScore text to new score.
				xIsWinner(); //Calls xIsWinner method.
				return true;
			}//Closes if X.
			
			//[O]
			//	 [O]
			//	    [O]
			if("O".equals(buttonLabel)) {//Checks if button label is O.
				gameActive = false;
				cpuTurn = false;
				inputting = false;
				disabled = true;
				p2Score++; //Add extra point to player two score
				playerTwoScore.setText(String.valueOf(p2Score)); //Sets playerTwoScore text to new score.
				oIsWinner(); //Calls oIsWinner method.
				return true;
			}//Closes if O.
		}//Close diagonal.
		
		//Checks for top right to bottom left diagonal.
		if(buttonLabel == b3.getText() && b3.getText() == b5.getText() && b5.getText() == b7.getText()) {
			highlightWinningCombo(b3,b5,b7); //Highlights Top Right, Middle, and Bottom Left buttons.
			
			//		[X]
			//	 [X]
			//[X]
			if("X".equals(buttonLabel)) { //Checks if button label is X.
				gameActive = false;
				cpuTurn = false;
				inputting = false;
				disabled = true;
				p1Score++; //Add extra point to player one score
				playerOneScore.setText(String.valueOf(p1Score)); //Sets playerOneScore text to new score.
				xIsWinner(); //Calls xIsWinner Method.
				return true;
			}//Closes if X.
			
			//		[O]
			//	 [O]
			//[O]
			if("O".equals(buttonLabel)) { //Checks if button label is O.
				gameActive = false;
				cpuTurn = false;
				inputting = false;
				disabled = true;
				p2Score++; //Add extra point to player two score
				playerTwoScore.setText(String.valueOf(p2Score)); //Sets playerTwoScore text to new score.
				oIsWinner(); //Calls oIsWinner Method.
				return true;
			}//Closes if O.
		}//Closes diagonal.
		
		//If All are Filled = no winner.
		//Checks all buttons if they're not empty.
		if(""!=b1.getText() && ""!=b2.getText() && ""!=b3.getText() && ""!=b4.getText() && 
				""!=b5.getText() && ""!=b6.getText() && ""!=b7.getText() && ""!=b8.getText() && ""!=b9.getText()) {
			gameActive = false;
			disabled = true;
			inputting = false;
			cpuTurn = false;
			isDraw(); //calls isDraw Method.
			return true;
		}//Close all button check.
		return false;
	}//Close determineWinner method.
	
	//Easy CPU, a random number generator which chooses where to place the buttons where ever it is empty.
	private void randomNumberGeneratorCPU() {
		cpuTurn = true; //When method is called, sets cpuTurn on in order to enable the loop for the random number generator.
		
		//Random number generator, finds a number. Checks if that button of that random number is empty.
				int randChoose; //Initialize random number generator variable.
				while(cpuTurn == true) {
					randChoose = (int) (Math.random() * 9) + 1; //Random Number Generator.	
					switch(randChoose) { //Switch case for random numbers.
					case 1:	 //If random number is 1.
						if("".equals(b1.getText())) { //Set button 1 to O.
							b1.setText("O");
							cpuTurn = false;
							isFirstPlayer = true;
							randPlace = false;
						} //Close if random number is 1.
						break;
					case 2: //If random number is 2.
						if("".equals(b2.getText())) { //Set button 2 to O.
							b2.setText("O");
							cpuTurn = false;
							isFirstPlayer = true;
							randPlace = false;
						}//Close if random number is 2.
						break;
					case 3: //If random number is 3.
						if("".equals(b3.getText())) { //Set button 3 to O.
							b3.setText("O");
							cpuTurn = false;
							isFirstPlayer = true;
							randPlace = false;
						}//Close if random number is 3.
						break;
					case 4: //If random number is 4.
						if("".equals(b4.getText())) { //Set button 4 to O.
							b4.setText("O");
							cpuTurn = false;
							isFirstPlayer = true;
							randPlace = false;
						}//Close if random number is 4
						break;
					case 5: //If random number is 5.
						if("".equals(b5.getText())) { //Set button 5 to O.
							b5.setText("O");
							cpuTurn = false;
							isFirstPlayer = true;
							randPlace = false;
						}//Close if random number is 5
						break;
					case 6: //If random number is 6.
						if("".equals(b6.getText())) { //Set button 6 to O.
							b6.setText("O");
							cpuTurn = false;
							isFirstPlayer = true;
							randPlace = false;
						}//Close if random number is 6
						break;
					case 7: //If random number is 7.
						if("".equals(b7.getText())) { //Set button 7 to O.
							b7.setText("O");
							cpuTurn = false;
							isFirstPlayer = true;
							randPlace = false;
						}//Close if random number is 7
						break;
					case 8: //If random number is 8.
						if("".equals(b8.getText())) { //Set button 8 to O.
							b8.setText("O");
							cpuTurn = false;
							isFirstPlayer = true;
							randPlace = false;
						}//Close if random number is 8
						break;
					case 9: //If random number is 9.
						if("".equals(b9.getText())) { //set button 9 to O.
							b9.setText("O");
							cpuTurn = false;
							isFirstPlayer = true;
							randPlace = false;
						}//Close if random number is 9
						break;
					} //End randChoose switch case
				}//End While loop for cpuTurn
	}//randomNumberGeneratorCPU method.
	
	//Hard CPU method. Blocks the player from winning in certain ways. Mainly based on random number placement as well but blocks player in some ways.
	private void hardCPU() {
		randPlace = true; //Initialize random number if it doesn't meet any of the requirements.
		getButtons(); //Calls getButtons method in order to gather all the buttons and put them in separate elements in an array.
		
		//Same as the determineWinner method, except blocks the user before they can win the game.
		//For loop for Columns.
		for(int i = 0; i <= 6; i = i + 3) {
			//For loop for Rows.
			for(int j = 0; j <= 2; j++) {
				
			//
				int a = j+3; //a is the button directly underneath the button being checked in the same column.
				int b = a+3; //b is 2 buttons directly underneath the button being checked in the same column.
				int k = i+1; //k is the button directly to the right of the button being checked in the same row.
				int l = k+1; //l is 2 buttons directly to the right of the button being checked in the same row.
				
				// Checks if [X][X][ ]
				//Checks the row on column 'i'. If the first and second buttons match and the third button is empty, place O.
				if("X".equals(array[i]) && "X".equals(array[k]) && "".equals(array[l])) {
					switch(i) { //Switch Case for column i.
					case 0: //Third Row on First Column.
						b3.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					case 3: //Third Row on Second Column.
						b6.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					case 6: //Third Row on Third Column.
						b9.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					}//Closes switch case for columns.
				} // Closes with [X][X][O] 
				
				//Checks if [ ][X][X]
				//Checks the row on column 'i'. If the second and third buttons match and the first button is empty, place O
				if("".equals(array[i]) && "X".equals(array[k]) && array[l] == array[k]) {
					switch(i) { //Switch Case for Column i.
					case 0://Third Row on first Column.
						b1.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					case 3://First Row on Second Column.
						b4.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					case 6://First Row on Third Column.
						b7.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					}//Close Switch Case for Column i.
				} //Close with [O][X][X]
				
				
				//Check if
				//[ ]
				//[X]
				//[X]
				//Checks the columns on row 'j'. If the second and third columns match and the first column is empty, place O.
				if("".equals(array[j]) && "X".equals(array[a]) && array[a] == array[b]) {
					switch(j) {//Switch case for Row j.
					case 0://First Column on First Row.
						b1.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					case 1://First Column on Second Row
						b2.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					case 2://First Column on Third Row.
						b3.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					}//Close Switch Case
				}//Close with
				 //[O]
				 //[X]
				 //[X]
				
				//Check for
				//[X]
				//[X]
				//[ ]
				//Checks the Columns on Row 'j'. If the First and Second Columns match and the Third Column is empty, place O.
				if("".equals(array[b]) && "X".equals(array[a]) && array[a] == array[j]) {
					switch(j) {//Switch case for Row j.
					case 0://Third Column on First Row.
						b7.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					case 1://Third Column on Second Row.
						b8.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					case 2://Third Column on Third Row.
						b9.setText("O");
						cpuTurn = false;
						isFirstPlayer = true;
						randPlace = false;
					break;
					}//Close switch case.
				}//Close with
				 //[X]
				 //[X]
				 //[O]
			}//Close for loop j.
		}//Close for loop i.
		
		//If no moves blocked, execute random number generator.
		while(randPlace == true) {
			randomNumberGeneratorCPU(); //Calls randomNumberGeneratorCPU method.
		}//close while loop.
	}//Close hardCPU method.
	
	//When winner is determined, change the color of the winning buttons to be highlighted.
	private void highlightWinningCombo(Button first, Button second, Button third) {
		first.getStyleClass().add("winning-button");
		second.getStyleClass().add("winning-button");
		third.getStyleClass().add("winning-button");
	}//Close highlightWinningCombo method.
	
	//openHowToWindow method, opens 'HowToPlay.fxml' when method is called.
	private void openHowToWindow() {
		try {
			//Load the pop up you created
			Pane howTo = (Pane)FXMLLoader.load(getClass().getResource("HowToPlay.fxml"));
			
			//Create a new scene
			Scene howToScene = new Scene(howTo,250,250);
			
			//Add CSS to the new scene
			howToScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//Create new stage to put scene in
			secondaryStage = new Stage();
			secondaryStage.setScene(howToScene);
			secondaryStage.setResizable(false);
			secondaryStage.showAndWait();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//Closes openHowToWindow method.
	
	//aboutWindow method, opens 'About.fxml' when method is called.
	private void aboutWindow() {
		try {
			//Load the pop up you created
			Pane aboutWindow = (Pane)FXMLLoader.load(getClass().getResource("About.fxml"));
			
			//Create a new scene
			Scene aboutScene = new Scene(aboutWindow,600,400);
			
			//Add CSS to the new scene
			aboutScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//Create new stage to put scene in
			aboutStage = new Stage();
			aboutStage.setScene(aboutScene);
			aboutStage.setResizable(false);
			aboutStage.showAndWait();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//close aboutWindow method.
	
	//xIsWinner method, opens 'xWinner.fxml' when method is called.
	private void xIsWinner() {
		try {
			//Load the pop up you created
			Pane winnerWindow = (Pane)FXMLLoader.load(getClass().getResource("XWinner.fxml"));
			
			//Create a new scene
			Scene winnerScene = new Scene(winnerWindow,200,150);
			
			//Add CSS to the new scene
			winnerScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//Create new stage to put scene in
			winnerStage = new Stage();
			winnerStage.setScene(winnerScene);
			winnerStage.setResizable(false);
			winnerStage.showAndWait();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//Close xIsWinner method.
	
	//oIsWinner method, opens 'oWinner.fxml' when method is called.
	private void oIsWinner() {
		try {
			//Load the pop up you created
			Pane winnerWindow = (Pane)FXMLLoader.load(getClass().getResource("OWinner.fxml"));
			
			//Create a new scene
			Scene winnerScene = new Scene(winnerWindow,200,150);
			
			//Add CSS to the new scene
			winnerScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//Create new stage to put scene in
			winnerStage = new Stage();
			winnerStage.setScene(winnerScene);
			winnerStage.setResizable(false);
			winnerStage.showAndWait();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//Close oIsWinner method.
	
	//isDraw method, opens 'Draw.fxml' when method is called.
	private void isDraw() {
		try {
			//Load the pop up you created
			Pane winnerWindow = (Pane)FXMLLoader.load(getClass().getResource("Draw.fxml"));
			
			//Create a new scene
			Scene winnerScene = new Scene(winnerWindow,200,150);
			
			//Add CSS to the new scene
			winnerScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//Create new stage to put scene in
			winnerStage = new Stage();
			winnerStage.setScene(winnerScene);
			winnerStage.setResizable(false);
			winnerStage.showAndWait();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//Close isDraw method.
	
	//getButtons method. Gathers the values of each button in the game and sets it to separate elements in an array when method is called, 
	private void getButtons() {
		loop = true; //Sets the loop to true when method is called.
		while(loop == true) { //While loop is true, gather all the button's labels and set to a separate element in an array.
			
			String button1 = b1.getText(); String button2 = b2.getText(); String button3 = b3.getText();
			String button4 = b4.getText(); String button5 = b5.getText(); String button6 = b6.getText();
			String button7 = b7.getText(); String button8 = b8.getText(); String button9 = b9.getText();
			array[0] = button1; array[1] = button2; array[2] = button3;
			array[3] = button4; array[4] = button5; array[5] = button6;
			array[6] = button7; array[7] = button8; array[8] = button9;
			
			loop = false; //Set the loop to false.
		}//Close while loop.
	}//Close getButtons method.
	
	//clearGame method. Sets all the button labels in the game to "" when method is called. 
	private void clearGame() {
		ObservableList<Node> buttons = 
				gameBoard.getChildren();
	
		buttons.forEach(btn -> {
			((Button) btn).setText("");
	 
			btn.getStyleClass().remove("winning-button");
		});
		gameActive = true; //Sets the game to active.
		isFirstPlayer = true; //New game starts with X
	}//Close clearGame method.
}//Close TicTacToeController.