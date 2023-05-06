/**
 * @author Loshan Sundaramoorthy
 * @version 1.0
 * */


package model;

import java.util.Arrays;
import java.util.Random;

public class YatzyDice {

    private int[] values = new int[5];
    private int throwCount = 0;

    /**
     * Returns the 5 face values of the dice.
     */
    public int[] getValues() {
        return values;
    }

    /**
     * Set the 5 face values of the dice.<br/>
     * Pre: 1 <= values[i] <= 6 for i in [0..4].<br/>
     * Note: This method is only to be used in tests.
     */
    public void setValues(int[] values) {
        this.values = values;
    }

    /**
     * Returns the number of times the 5 dice has been thrown.
     */
    public int getThrowCount() {
        return throwCount;
    }

    /**
     * Resets the throw count.
     */
    public void resetThrowCount() {
        throwCount = 0;
    }

    /**
     * Roll the 5 dice. Only roll dice that are not hold.<br/>
     * Note: holdStatus[index] is true, if die no. index is hold (for index in [0..4]).
     */
    public void throwDice(boolean[] holdStatus) {
        Random rand = new Random();
        for (int i = 0; i < holdStatus.length; i++) {
            if (!holdStatus[i]){
                values[i] = rand.nextInt(6) + 1;
            }
        }
        throwCount++;
    }

    // -------------------------------------------------------------------------

    /**
     * Returns all results possible with the current face values.<br/>
     * The order of the results is the same as on the score board.<br/>
     */
    public int[] getResults() {
        int[] results = new int[15];
        for (int i = 0; i <= 5; i++) {
            results[i] = this.sameValuePoints(i + 1);
        }
        results[6] = this.onePairPoints();
        results[7] = this.twoPairPoints();
        results[8] = this.threeSamePoints();
        results[9] = this.fourSamePoints();
        results[10] = this.fullHousePoints();
        results[11] = this.smallStraightPoints();
        results[12] = this.largeStraightPoints();
        results[13] = this.chancePoints();
        results[14] = this.yatzyPoints();


        return results;
    }

    // -------------------------------------------------------------------------

    // Returns an int[7] containing the frequency of face values.
    // Frequency at index v is the number of dice with the face value v, 1 <= v <= 6.
    // Index 0 is not used.

    public int[] frequency() {
        int[] frequencies = new int[7];
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j < values.length; j++) {
                if (values[j] == i) {
                    frequencies[i]++;
                }
            }
        }
        return frequencies;
    }

    /**
     * Returns same-value points for the given face value.<br/>
     * Returns 0, if no dice has the given face value.<br/>
     * Pre: 1 <= value <= 6;
     */
    public int sameValuePoints(int value) {
        int points = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == value) {
                points += value;
            }
        }
        return points;
    }

    /**
     * Returns points for one pair (for the face value giving the highest points).<br/>
     * Returns 0, if there aren't 2 dice with the same face value.
     */

    public int onePairPoints() {
        int sum = 0;
        for (int i = 0; i < frequency().length; i++) {
            if (frequency()[i] >= 2) {
                sum = i * 2;
            }
        }
        return sum;
    }

    /**
     * Returns points for two pairs<br/>
     * (for the 2 face values giving the highest points).<br/>
     * Returns 0, if there aren't 2 dice with the same face value<br/>
     * and 2 other dice with the same but different face value.
     */
    public int twoPairPoints() {
        int pairs = 0;
        int sum = 0;
        for (int i = 0; i < frequency().length; i++) {
            if (frequency()[i] >= 2) {
                sum += i * 2;
                pairs++;
            }
        }
        if (pairs == 2) {
            return sum;
        }
        return 0;
    }

    /**
     * Returns points for 3 of a kind.<br/>
     * Returns 0, if there aren't 3 dice with the same face value.
     */
    public int threeSamePoints() {
        int sum = 0;
        for (int i = 0; i < frequency().length; i++) {
            if (frequency()[i] >= 3) {
                sum += i * 3;
            }
        }
        return sum;
    }

    /**
     * Returns points for 4 of a kind.<br/>
     * Returns 0, if there aren't 4 dice with the same face value.
     */
    public int fourSamePoints() {
        int pairs = 0;
        int sum = 0;
        for (int i = 0; i < frequency().length; i++) {
            if (frequency()[i] >= 4) {
                sum += i * 4;
            }
        }
        return sum;
    }

    /**
     * Returns points for full house.<br/>
     * Returns 0, if there aren't 3 dice with the same face value<br/>
     * and 2 other dice with the same but different face value.
     */
    public int fullHousePoints() {
        int sum = 0;
        boolean foundThree = false;
        boolean foundTwo = false;

        for (int i = 0; i < frequency().length; i++) {
            if (frequency()[i] == 3) {
                foundThree = true;
            }
            if (frequency()[i] == 2) {
                foundTwo = true;
            }
        }

        if (foundThree && foundTwo) {
            for (int i = 0; i < values.length; i++) {
                sum += values[i];
            }
        }
        return sum;
    }

    /**
     * Returns points for small straight.<br/>
     * Returns 0, if the dice aren't showing 1,2,3,4,5.
     */

    public int smallStraightPoints() {
        int duplicates = 0;
        int sum = 0;
        for (int i = 0; i < frequency().length; i++) {
            if(frequency()[i] == 2){
                duplicates++;
            }
            sum = sum + (frequency()[i] * i);
        }
        if(sum == 15 && duplicates < 2){
            return sum;}
        else{
            return 0;
        }
    }

    /**
     * Returns points for large straight.<br/>
     * Returns 0, if the dice aren't showing 2,3,4,5,6.
     */

    public int largeStraightPoints() {
        int duplicates = 0;
        int sum = 0;
        for (int i = 0; i < frequency().length; i++) {
            if (frequency()[i] == 2){
                duplicates++;
            }
            sum = sum + (frequency()[i] * i);

        }
        if(sum == 20 && duplicates < 2){
            return sum;}
        else{
            return 0;
        }
    }

    /**
     * Returns points for chance (the sum of face values).
     */

    public int chancePoints() {
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }

    /**
     * Returns points for yatzy (50 points).<br/>
     * Returns 0, if there aren't 5 dice with the same face value.
     */
    public int yatzyPoints() {
        int sum = 0;
        int first = values[0];
        boolean yatzy = false;
        for (int i = 0; i < values.length; i++) {
            if (values[i] != first) {
                yatzy = false;
            }
            else {
                yatzy = true;
            }
        }

        if (yatzy) {
            sum = 50;
        }

        return sum;
    }
}
