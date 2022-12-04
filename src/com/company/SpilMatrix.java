package com.company;

import java.util.Random;
import java.util.Scanner;

public class SpilMatrix {

    int højde = 6;
    int bredde = 9;
    boolean xTur = true;
    boolean førsteRunde = true;
    boolean run = true;
    int rowNr;


    String[][] arrayMatrix;
    String[][] xArrayMatrix;
    String[][] yArrayMatrix;
    Random random = new Random();


    public void setup() {
        arrayMatrix = new String[højde][bredde];
        xArrayMatrix = arrayMatrix;
        yArrayMatrix = arrayMatrix;

        System.out.println("Velkommen til spillet. Handler om at få 5 på stribe");
        System.out.println("Player 1: X");
        System.out.println("Player 2: Y");
        System.out.println();
        while (run) {
            run = spilRunde(arrayMatrix, xArrayMatrix, yArrayMatrix); //spilRunde returnerer true eller false - fortsætter så længe true
        }


    }

    //Metode til at printe spil matrixen (array) ud.
    public int indtastBrik() {
        Scanner scanner = new Scanner(System.in);
        int rykBrik;

        //xTur er sat globalt til true, derfor player 1 starter.
        if (xTur) {
            System.out.println("(X)  - Player 1: Indtast et tal fra 1-7, hvor du ønsker at smide brikken");
        } else {
            System.out.println("(Y)  - Player 2: Indtast et tal fra 1-7, hvor du ønsker at smide brikken");
        }
        System.out.println();



        //try-catch så der skal skrives en int, og string modtages ikke
        try {
            rykBrik = Integer.parseInt(scanner.next()); //konverterer til en int
            System.out.println(rykBrik);

            if (rykBrik < 1 || rykBrik > 7) {
                System.out.println("Prøv igen, fra 1-7");
            }
        } catch (Exception e) {
            System.out.println("Du har indtastet forkert, start forfra");
            rykBrik = 11;
        }
        return rykBrik;
    }

    public boolean spilRunde(String[][] startArrayMatrix, String[][] xArrayMatrix, String[][] yArrayMatrix) {
        for (int row = 0; row < startArrayMatrix.length; row++) { //hver row kører igennem columns.
            for (int column = 0; column < startArrayMatrix[row].length; column++) { //for loop der kører igennem columns med det givne index row
                if (førsteRunde) { //førsterunde er en boolean sat til true globalt, hvis det første runde, skal O indsættes
                    startArrayMatrix[row][column] = "O";
                }

                if (xArrayMatrix[row][column].equals("X")) { //Hvis nuværende row og col i xArray indeholder "X", så tegner vi det
                    System.out.print(xArrayMatrix[row][column] + "  ");
                } else if (yArrayMatrix[row][column].equals("Y")) {
                    System.out.print(xArrayMatrix[row][column] + "  ");
                } else {
                    startArrayMatrix[row][column] = "O";

                    /**
                     * Prøv at rykke nedenstående ud i en metode, skal bliver kaldt med parameter column
                     *Metode: indsæt random Tokens
                     */
                    //Inspiration:https://www.tutorialspoint.com/how-to-populate-a-2d-array-with-random-alphabetic-values-from-a-range-in-java
                    if (column == 8 || column == 0) { //Hvis vi er på kollonne  0 eller 8, skal vi indsætte random char i stedet for
                        int num = random.nextInt(2); //bounds er 2, laver et random tal mellem 0 og 1
                        switch (num) { //hvis det random tal er 0 = X og hvis random tal er 1 = Y
                            case 0: {
                                startArrayMatrix[row][column] = "X";
                                break;
                            }
                            case 1: {
                                startArrayMatrix[row][column] = "Y";
                                break;
                            }
                        }
                    }
                    System.out.print(startArrayMatrix[row][column] + "  "); //Printer værdierne samt mellemrum mellem værdierne
                }
            }
            System.out.println(); //linje for at få 9 columns x 6 rows (konsol udseende)
        }
        System.out.println();

        return turErIgang();


       /* boolean turErIgang = true;
        if (xTur) {
            while (turErIgang) {
                int rykBrik = indtastBrik();
                turErIgang = placerBrik(rykBrik, xArrayMatrix); // TurErIgang skal have værdi fra placerBri

                if (tjekColumnsAndRows("X", xArrayMatrix)) {
                    return false;
                }
            }
        } else {
            while (turErIgang) {
                int rykBrik = indtastBrik();
                turErIgang = placerBrik(rykBrik, yArrayMatrix);

                if (tjekColumnsAndRows("Y", yArrayMatrix)) {
                    return false;
                }
            }
        }

        førsteRunde = false;
        return true;*/
    }

    public boolean turErIgang() {
        SpilMatrix spilMatrix = new SpilMatrix();
        boolean turErIgang = true;

        if (xTur) { //Tjekker hvis xTur er true (X's tur er igang)
            while (turErIgang) {
                int rykBrikX = indtastBrik();
                turErIgang = placerBrik(rykBrikX, xArrayMatrix); // TurErIgang skal have værdi fra placerBrik

                if (gennemløbRows("X", xArrayMatrix)
                        || gennemløbColumns("X", xArrayMatrix, spilMatrix.højde, spilMatrix.bredde)
                        || gennemløbDiagonal1("X", xArrayMatrix, spilMatrix.højde, spilMatrix.bredde)
                        || diagonalVenstreOpTilNed("X", xArrayMatrix, rykBrikX)
                        || diagonalVenstreNedTilOp("X", xArrayMatrix, rykBrikX)){
                    return false;
                }
            }
        } else { // Ellers er det Y's tur
            while (turErIgang) {
                int rykBrikY = indtastBrik();
                turErIgang = placerBrik(rykBrikY, yArrayMatrix);

                if( gennemløbRows("Y", yArrayMatrix)
                        || gennemløbColumns("Y", yArrayMatrix, spilMatrix.højde, spilMatrix.bredde)
                        || gennemløbDiagonal1("Y", yArrayMatrix, spilMatrix.højde, spilMatrix.bredde)
                        || diagonalVenstreOpTilNed("Y", yArrayMatrix, rykBrikY)
                        || diagonalVenstreNedTilOp("Y", yArrayMatrix, rykBrikY)){
                    return false;
                }
            }
        }

        førsteRunde = false; //Sætter første runde til false efter det
        return true;
    }

    //Metode retunerer enten en true eller false, alt efter om brikken kan placeres
    public boolean placerBrik(int brikColumnNummer, String[][] array) {
        for (int row = 0; row < array.length; row++) {
            String brik = array[row][brikColumnNummer];

            //Tjekker hvis der er fyldt op (om der er en på index 0)
            if (!brik.equals("O") && row == 0) {
                return true; //Turen fortsætter - Der kan ikke tegnes en brik
            }

            //Tjekker hvis der allerede står X eller Y på placeringen
            //Tjekker om pladsen ikke har "O" som værdi, så står der X eller Y
            if (!brik.equals("O")) { //Hvis der allerede står X eller Y
                tegnBrik(array, row - 1, brikColumnNummer);
                this.rowNr = row-1;
                return false; //Turen slutter og brik er tegnet
            }

            //Tjekker om det er den sidste row
            if (row == array.length - 1) { //length tæller fra 1, derfor -1
                tegnBrik(array, row, brikColumnNummer);
                this.rowNr = row;
                return false; //Turen slutter og brik er tegnet
            }

            /**
             * Metode til hvis matrixen er fyldt helt op og ingen har vundet
             * Skal tjekke om alle værdierne på row 0 er enten X eller Y -> hvis dette er tilfældet
             * skal den gå ud af programmet og sige "ingen har vundet"
             */
           /* for (int colum = 0; colum < array[row].length ; colum++) {
                if (row == 0 && colum == colum){
                }
            }*/


        }
        return true;
    }

    public void tegnBrik(String[][] array, int row, int column) {
        if (xTur) {
            array[row][column] = "X";
            xArrayMatrix = array;
        } else {
            array[row][column] = "Y";
            yArrayMatrix = array;
        }
        xTur = !xTur; //Sætter xtur til det modsatte af hvad den er nu, for at skifte mellem player 1 og 2
    }

    /**
     * Skal de næste metoder rykkes ud i en klasse der hedder GennemløbMatrix
     */

    //Gennemløber vandret
    public boolean gennemløbRows(String findValue, String[][] array) {
        int tempMax = 0;

        //rows er true, tjekker rows (vandret)
            for (int row = 0; row < array.length; row++) {
                int count = 0;

                String indexValue;

                for (int column = 1; column < array[row].length-1 ; column++) {
                    indexValue = array[row][column];
                    if (indexValue == findValue) { // find value er X eller Y, alt om hvad værdien fra parameteren er angivet.
                        count++;
                        if (count > tempMax) {
                            tempMax = count;
                        }
                    } else {
                        count = 0; //sætter count til 0 for at starte count forfra
                    }
                }
            }

        return tjekVundetSpil(tempMax);
    }

    //Gennemløber lodret
    public boolean gennemløbColumns (String findValue, String[][] array, int højde, int bredde) {
        int tempMax = 0;
        for (int i = 1; i < bredde-1; i++) {
            int count = 0;
            for (int j = 0; j < højde; j++) {
                String indexValue = array[j][i];
                if (indexValue.equals(findValue)) { //Hvis indexValue som er værdien på det givne indeks, er lig med værdien fra parameteren
                    count++;
                    if (count > tempMax) {
                        tempMax = count;
                    }
                } else {
                    count = 0; //sætter count til 0 for at starte count forfra
                }

            }
        }
        return tjekVundetSpil(tempMax);
    }

    //Gennemløber diagonalt retning
    public boolean gennemløbDiagonal1 (String findValue, String [][] array, int højde, int bredde){
        int tempMax = 0;
        for (int k = 0; k < (højde + bredde) * 2; k++) {
            int count = 0;
            for (int j = 1; j <= k && j<bredde-1; j++) { //Starter på 1 da colum 0 ikke skal loopes igennem, kører indtil bredde-1 for ikke få sidste colum med
                int i = k - j;
                if (i < højde && j < bredde) {
                    String indexValue =  array[i][j];
                    if (indexValue == findValue) { // find value er om det er -1,1 eller 0, alt efter hvad argumentet er
                        count++;
                        if (count > tempMax) {
                            tempMax = count;
                        }
                    } else {
                        count = 0; //sætter count til 0 for at starte count forfra
                    }
                }
            }

        }
        return tjekVundetSpil(tempMax);
    }


    //Ud fra placering, kigger metoden på det næste index der er en kolonne større samt en row større: tjekker fra placering op til ned
    public boolean diagonalVenstreOpTilNed(String findValue, String [][] array, int columnNr) {
         int column = columnNr;
         int row = rowNr;
         int tempMax  = 1;
        boolean run = true;
        while (run){
            column = column + 1;
            row = row + 1;

            if (column >= 9 || row >=6){
                break;
            }
            if (array[row][column] == findValue){
                tempMax++;
            }else {
                break;
            }
        }

        return tjekVundetSpil(tempMax);

    }

    public boolean diagonalVenstreNedTilOp(String findValue, String [][] array, int columnNr) {
        int column = columnNr;
        int row = rowNr;
        int tempMax  = 1;
        boolean run = true;
        while (run){
            column = column - 1;
            row = row - 1;

            if (column <= 0 || row <=0){
                break;
            }
            if (array[row][column] == findValue){
                tempMax++;
            }else {
                break;
            }
        }

        return tjekVundetSpil(tempMax);

    }


    public boolean tjekVundetSpil(int tempMax){
        if (tempMax >= 5) {
            System.out.println("Du har vundet");
            return true;
        } else {
            return false;
        }
    }


}
