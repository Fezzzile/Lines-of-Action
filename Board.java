
import java.awt.Color;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.awt.Font;

/**
 * This class contains most of the code 1. The board is initialised here 2.
 * Moves are made here 3. Even the drawings are done here 3. This class calls
 * all the other classes, except Loa.class.
 *
 * @author 21631387
 */
public class Board {

    public static void board(int size, int mode, String ipAddress) {
        String move;
        int fromRow, fromCol, toRow, toCol;

        String newboard[][] = new String[size][size];
        Scanner input = new Scanner(System.in);

        // This is the initial board
        {
            // Put a dot on every space
            for (int a = 0; a < size; a++) {
                for (int b = 0; b < size; b++) {
                    newboard[a][b] = ".";
                }
            }

            // Whites and Blacks, except corners
            for (int piece = 1; piece < size - 1; piece++) {
                newboard[piece][0] = "W";
                newboard[piece][size - 1] = "W";
                newboard[0][piece] = "B";
                newboard[size - 1][piece] = "B";
            }
        }

        switch (mode) {
            case 0: // test mode
                while (true) {

                    DisplayBoard(newboard, size);
                    move = input.nextLine().toUpperCase();
                    humanMove(mode, newboard, size, move);
                    Winner(newboard, "W", "B");
                }

            case 1: // Single-player mode (play against the computer)
                System.out.println("\nYou have been assigned Black pieces\n");
                while (true) {
                    /**
                     * Display the board check winner Enter and make move
                     */
                    System.out.println("Your turn:\n");
                    DisplayBoard(newboard, size);
                    System.out.println();
                    Winner(newboard, "W", "B");
                    System.out.print("Enter your move: ");
                    move = input.nextLine().toUpperCase();
                    humanMove(mode, newboard, size, move);

                    // Repeat the above steps (for the Computer)
                    System.out.println("The computer has played:\n");
                    DisplayBoard(newboard, size);
                    System.out.println();
                    Winner(newboard, "W", "B");
                    ComputerMove(mode, newboard, size);
                }

            case 2: // Networking mode
                System.out.println("Multiplayer mode started!\nWaiting for the other player to connect...");

                int SERVER_MODE = 0;
                int CLIENT_MODE = 1;
                int player = 1;
                int c;

                c = Networking.connect(ipAddress);

                if (c == SERVER_MODE) {
                    player = 1;
                } else if (c == CLIENT_MODE) {
                    player = 2;
                } else {
                    System.out.println("Connection error");
                }

                boolean black;

                if (player == 1) {
                    black = false;
                } else {
                    black = true;
                }

                DisplayBoard(newboard, size);
                System.out.println("\nBLACK's turn: ");

                while (true) {
                    if (black) {

                        move = input.next().toUpperCase();
                        fromRow = (size - 1) - (move.charAt(0) - 'A');
                        fromCol = move.charAt(1) - 'A';
                        toRow = (size - 1) - (move.charAt(2) - 'A');
                        toCol = move.charAt(3) - 'A';

                        // M06: The program MUST allow the player in "client mode" to play black
                        if (c == CLIENT_MODE) {
                            if (move.equals("QUIT")) {
                                System.out.println("A player a quit");
                                System.exit(0);
                            }
                            while (!isMoveValid.checkValidity(newboard, fromRow, fromCol, size, toRow, toCol) || !newboard[fromRow][fromCol].equals("B")) {
                                System.out.print("ERROR. Enter a valid move: ");
                                move = input.nextLine().toUpperCase();
                                fromRow = (size - 1) - (move.charAt(0) - 'A');
                                fromCol = move.charAt(1) - 'A';
                                toRow = (size - 1) - (move.charAt(2) - 'A');
                                toCol = move.charAt(3) - 'A';
                            }
                        } // M07: The program MUST allow the player in "server mode" to play white
                        else if (c == SERVER_MODE) {
                            if (move.equals("QUIT")) {
                                Networking.write(move);
                                System.out.println("A player a quit");
                                //System.exit(0);
                            }
                            while (!isMoveValid.checkValidity(newboard, fromRow, fromCol, size, toRow, toCol) || !newboard[fromRow][fromCol].equals("W")) {
                                System.out.print("ERROR. Enter a valid move: ");
                                move = input.nextLine().toUpperCase();
                                fromRow = (size - 1) - (move.charAt(0) - 'A');
                                fromCol = move.charAt(1) - 'A';
                                toRow = (size - 1) - (move.charAt(2) - 'A');
                                toCol = move.charAt(3) - 'A';
                            }
                        }

                        makeMove(mode, newboard, fromRow, fromCol, toRow, toCol);
                        DisplayBoard(newboard, size);
                        Winner(newboard, "W", "B");

                        Networking.write(move);

                        black = false;

                    } else {

                        move = Networking.read();
                        if (move.equals("QUIT")) {
                            System.out.println("A player quit");
                            System.exit(0);
                        }

                        fromRow = (size - 1) - (move.charAt(0) - 'A');
                        fromCol = move.charAt(1) - 'A';
                        toRow = (size - 1) - (move.charAt(2) - 'A');
                        toCol = move.charAt(3) - 'A';

                        makeMove(mode, newboard, fromRow, fromCol, toRow, toCol);
                        black = true;
                        DisplayBoard(newboard, size);
                        Winner(newboard, "W", "B");
                    }
                }

            case 3: // Graphical mode

                while (true) {
                    Draw(newboard, size);

                    fromRow = 0;
                    fromCol = 0;
                    toRow = 0;
                    toCol = 0;

                    StdDraw.isMousePressed = true;

                    while (StdDraw.isMousePressed) {
                        fromCol = (int) (StdDraw.mouseX());
                        fromRow = (int) (StdDraw.mouseY());
                    }
                    StdAudio.play("MouseClick.wav");

                    StdDraw.isMousePressed = false;
                    while (!StdDraw.isMousePressed) {
                        toCol = (int) (StdDraw.mouseX());
                        toRow = (int) (StdDraw.mouseY());
                    }

                    StdDraw.isMousePressed = false;

                    // Human player
                    {
                        while (!isMoveValid.checkValidity(newboard, fromRow, fromCol, size, toRow, toCol)) {
                            StdAudio.play("no.wav");
                            JOptionPane.showMessageDialog(null, "ERROR: invalid move. Enter a valid move");

                            while (StdDraw.isMousePressed == false) {
                                fromCol = (int) (StdDraw.mouseX());
                                fromRow = (int) (StdDraw.mouseY());
                            }
                            StdAudio.play("MouseClick.wav");
                            StdDraw.isMousePressed = false;
                            while (StdDraw.isMousePressed == false) {
                                toCol = (int) (StdDraw.mouseX());
                                toRow = (int) (StdDraw.mouseY());
                            }
                        }

                        while (newboard[fromRow][fromCol].equals("W")) {
                            StdAudio.play("no.wav");
                            JOptionPane.showMessageDialog(null, "You can't move a White piece. Please move a Black piece");
                            StdDraw.isMousePressed = false;
                            while (StdDraw.isMousePressed == false) {
                                fromCol = (int) (StdDraw.mouseX());
                                fromRow = (int) (StdDraw.mouseY());
                                StdDraw.isMousePressed = true;
                            }
                            StdDraw.isMousePressed = false;
                            while (StdDraw.isMousePressed == false) {
                                toCol = (int) (StdDraw.mouseX());
                                toRow = (int) (StdDraw.mouseY());
                            }
                        }
                    }

                    makeMove(mode, newboard, fromRow, fromCol, toRow, toCol);
                    Draw(newboard, size);
                    ComputerMove(mode, newboard, size);
                }
        }
    }

    private static void DisplayBoard(String newboard[][], int size) {
        // For the letters on the left
        int N = 0;
        for (int k = 0; k < size; k++) {
            N = 16 - size;
        }

        char letterLeft = (char) (80 - N);

        for (int i = 0; i < size; i++) {
            letterLeft--;
        }

        int Number = 0;
        for (int k = 0; k < size; k++) {
            Number = 16 - size;
        }

        letterLeft = (char) (80 - Number);

        // This is for the letters at the top
        char letterTop = 'A';
        System.out.print(" ");
        for (int alpha = 0; alpha < size; alpha++) {
            System.out.print(" " + letterTop);
            letterTop++;
        }
        System.out.println();

        for (int row = 0; row < size; row++) {
            System.out.print(letterLeft + " ");
            letterLeft--;
            for (int col = 0; col < size; col++) {
                System.out.print(newboard[row][col] + " "); // prints the board
            }
            System.out.println();
        }
    }

    /**
     * For simplicity, I decided to create three move-methods.
     * Combining all the methods is possible, but it becomes hard to read. It's even worse to edit...
     */
    private static void humanMove(int mode, String newboard[][], int size, String move) {
        int fromRow, fromCol, toRow, toCol;

        if (mode == 0) {

            // The program will be terminated if the user types "QUIT" instead of a valid move
            if (move.equals("QUIT")) {
                System.out.println("player quit");
                System.exit(0);
            }

            fromRow = (size - 1) - (move.charAt(0) - 'A');
            fromCol = move.charAt(1) - 'A';
            toRow = (size - 1) - (move.charAt(2) - 'A');
            toCol = move.charAt(3) - 'A';

            // This calls the isMoveValid class and checks if the move is valid. If it's invalid, it quits the program
            if (!isMoveValid.checkValidity(newboard, fromRow, fromCol, size, toRow, toCol)) {
                System.out.println("ERROR: invalid move");
                System.exit(0);
            }

            // After checking the validity of a move, I move the piece from one point to another
            makeMove(mode, newboard, fromRow, fromCol, toRow, toCol);

        } else if (mode == 1) {
            System.out.println();
            Scanner input = new Scanner(System.in);

            // The program will be terminated if the user types "QUIT" instead of a valid move
            if (move.equals("QUIT")) {
                System.out.println("player quit");
                System.exit(0);
            }

            fromRow = (size - 1) - (move.charAt(0) - 'A');
            fromCol = move.charAt(1) - 'A';
            toRow = (size - 1) - (move.charAt(2) - 'A');
            toCol = move.charAt(3) - 'A';

            // Human player
            {
                while (!isMoveValid.checkValidity(newboard, fromRow, fromCol, size, toRow, toCol) || newboard[fromRow][fromCol].equals("W")) {
                    System.out.print("ERROR: invalid move. Enter a valid move: ");
                    move = input.nextLine().toUpperCase();

                    if (move.equals("QUIT")) {
                        System.out.println("player quit");
                        System.exit(0);
                    }
                    System.out.println();
                    fromRow = (size - 1) - (move.charAt(0) - 'A');
                    fromCol = move.charAt(1) - 'A';
                    toRow = (size - 1) - (move.charAt(2) - 'A');
                    toCol = move.charAt(3) - 'A';
                }
            }

            // After checking the validity of a move, I move the piece from one point to another
            makeMove(mode, newboard, fromRow, fromCol, toRow, toCol);
        }

    }

    private static void ComputerMove(int mode, String newboard[][], int size) {
        int fromRow = 0, fromCol = 0, toRow = 0, toCol = 0;

        for (int a = 1; a < 2; a++) {
            fromRow = (int) (Math.random() * size);
        }
        for (int b = 1; b < 2; b++) {
            fromCol = (int) (Math.random() * size);
        }
        for (int c = 1; c < 2; c++) {
            toRow = (int) (Math.random() * size);
        }
        for (int d = 1; d < 2; d++) {
            toCol = (int) (Math.random() * size);
        }

        while (!isMoveValid.checkValidity(newboard, fromRow, fromCol, size, toRow, toCol) || !newboard[fromRow][fromCol].equals("W")) {

            for (int a = 1; a < 2; a++) {
                fromRow = (int) (Math.random() * size);
            }
            for (int b = 1; b < 2; b++) {
                fromCol = (int) (Math.random() * size);
            }
            for (int c = 1; c < 2; c++) {
                toRow = (int) (Math.random() * size);
            }
            for (int d = 1; d < 2; d++) {
                toCol = (int) (Math.random() * size);
            }
        }
        makeMove(mode, newboard, fromRow, fromCol, toRow, toCol);
    }

    private static void makeMove(int mode, String newboard[][], int fromRow, int fromCol, int toRow, int toCol) {
        if (mode == 3) {
            if ((newboard[fromRow][fromCol].equals("W") && newboard[toRow][toCol].equals("B")) || (newboard[fromRow][fromCol].equals("B") && newboard[toRow][toCol].equals("W"))) {
                StdAudio.play("handslap.wav");
            } else {
                StdAudio.play("pop.wav");
            }
        }

        newboard[toRow][toCol] = newboard[fromRow][fromCol];
        newboard[fromRow][fromCol] = ".";

    }

    private static void Winner(String newboard[][], String whitePiece, String blackPiece) {
        if (Util.isConnected(newboard, whitePiece) && Util.isConnected(newboard, blackPiece)) {
            System.out.println("DRAW");
            System.exit(0);
        } else if (Util.isConnected(newboard, blackPiece) && !Util.isConnected(newboard, whitePiece)) {
            System.out.println("Winner: black");
            System.exit(0);
        } else if (!Util.isConnected(newboard, blackPiece) && Util.isConnected(newboard, whitePiece)) {
            System.out.println("Winner: white");
            System.exit(0);
        }
    }

    // This method is used only when the mode is 3 (graphical)
    private static void Draw(String newboard[][], int size) {
        StdDraw.setXscale(0, size);
        StdDraw.setYscale(size, 0);

        // checkers board
        for (int a = 0; a < size; a++) {
            for (int b = 0; b < size; b++) {
                if ((a + b) % 2 == 0) {
                    StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                } else {
                    StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                }
                StdDraw.filledSquare(b + 0.5, a + 0.5, 0.5);
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                switch (newboard[i][j]) {
                    case "B":
                        StdDraw.setPenColor(StdDraw.BLACK);
                        StdDraw.filledCircle(j + 0.5, i + 0.5, 0.45);
                        break;
                    case "W":
                        StdDraw.setPenColor(StdDraw.WHITE);
                        StdDraw.filledCircle(j + 0.5, i + 0.5, 0.45);
                        break;
                }
            }
        }

        if (Util.isConnected(newboard, "B") || Util.isConnected(newboard, "W")) {
            StdAudio.play("ba-da-dum.wav");
            StdDraw.show(2000);

            Font font = new Font("Serif", Font.BOLD, 40);
            StdDraw.setFont(font);

            if (Util.isConnected(newboard, "B") && !Util.isConnected(newboard, "W")) {
                StdDraw.clear(StdDraw.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(size / 2, size / 2, "BLACK won!");
            } else if (Util.isConnected(newboard, "W") && Util.isConnected(newboard, "B")) {
                StdDraw.clear(StdDraw.RED);
                StdDraw.text(size / 2, size / 2, "DRAW!");
            } else if (!Util.isConnected(newboard, "B") && Util.isConnected(newboard, "W")) {
                StdDraw.clear(StdDraw.WHITE);
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.text(size / 2, size / 2, "WHITE won!");
            }

            StdDraw.show(2000);
            System.exit(0);
        }

        //StdDraw.filledRectangle(xCentre, yCentre, xradius, yRadius);
        StdDraw.show(500);
    }
}
