/**
 * Created by aaronsmith on 6/19/15.
 */
import java.util.Random; //imports random class

import javax.swing.JOptionPane; //imports JOPtionPane


public class ShipCaptainCrew { //initiates class ShipCaptainCrew
    // Start of game
    public static void main(String[] args) { //initiates class main.  void because it doesn't return a value


        // Initially we haven't acquired any of the three
        boolean shipAcquired = false; //declares boolean shipAcquired; defaults as false
        boolean captainAcquired = false; //declares boolean captainAcquired; defaults as false
        boolean crewAcquired = false; //declares boolean crewAcquired; defaults as false

        // The player has a maximum of 3 rolls
        int rollsLeft = 3; //declares rollsLeft as an integer with value of three

        // Java has a library for generating random numbers, we use this to generate random number 1 to 6.
        Random dice = new Random(); //initiates new object of class random

        // declares variable score as an integer with a default value of 0
        int score = 0;

        // There are a maximum of 5 dice to be thrown for a roll (first roll)
        // Later on the numberOfDiceToThrow decreases for each roll as we acquire the ship, captain, and crew
        int nextNumberOfDiceToThrow = 5;

        // While there are are more rolls available, we simulate a round.
        while(rollsLeft > 0) {
            JOptionPane.showMessageDialog(null, "You have  " + rollsLeft + " rolls left..."); //displays message of number of rolls left
            //uses variable rollsleft

            int numberOfDiceToThrow = nextNumberOfDiceToThrow; // sets variable numberOfDiceToThrow as nextNumberofDiceToThrow

            // rolls dice
            for(int i = 0; i < numberOfDiceToThrow; i++) { //for loop
                // Throw 1 die and get the value
                int dieValue = (int) (6*dice.nextDouble() +1); //random method

                //displays message
                JOptionPane.showMessageDialog(null, "Rolled dice #" + (i + 1) + " of " + numberOfDiceToThrow + "... you got a " + dieValue);

                // Checks to see if you got a ship, captain, or crew
                if(dieValue == 6 && shipAcquired == false) {
                    // If we got a ship and we haven't acquired it yet, then we acquire it
                    shipAcquired = true; //changes the value of shipAcquired to true
                    JOptionPane.showMessageDialog(null, "Ship acquired!"); //displays message

                    nextNumberOfDiceToThrow = nextNumberOfDiceToThrow - 1; //records the roll of dice
                } else if(shipAcquired == true && dieValue == 5 && captainAcquired == false) {
                    // Otherwise, if we already have a ship, then we get a captain, but we haven't acquied it yet, then acquire it
                    captainAcquired = true;
                    JOptionPane.showMessageDialog(null, "Captain acquired!");

                    nextNumberOfDiceToThrow = nextNumberOfDiceToThrow - 1;
                } else if(captainAcquired == true && dieValue == 4 && crewAcquired == false) {
                    // Otherwise, if we already have a ship and a captain, then we get a crew, but haven't acquired it yet, then acquire it
                    crewAcquired = true;
                    JOptionPane.showMessageDialog(null, "Crew acquired!");

                    nextNumberOfDiceToThrow = nextNumberOfDiceToThrow - 1;
                } else if(shipAcquired == true && captainAcquired == true && crewAcquired == true) {
                    score = score + dieValue; //takes the current score and and adds it back to the variable score with the remaning dice
                }
            }

            // So after rolling a number of dice, the game ends if you got a ship, captain, and crew
            if(shipAcquired == true && captainAcquired == true && crewAcquired == true) {


                // This break statement, will force the while loop to stop instead of waiting for the rollsLeft to become 0
                JOptionPane.showMessageDialog(null, "You win, you got a score of " + score); //displays score message
                break; //ends loop
            } else {
                // The player will have no score after rolling them and never got it in the right order.
                score = 0;  //assigns score of 0
                JOptionPane.showMessageDialog(null, "You lost this round.."); //loser message
            }

            // Move to the next roll
            rollsLeft = rollsLeft - 1;

        }
    }
}