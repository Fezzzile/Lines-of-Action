
/**
 * This class holds all the methods that check if a move is valid
 *
 * @author 21631387
 */
public class isMoveValid {

    /**
     * This method is like the "main" method in this class. It first checks the
     * kind of move that a player makes. If, for example, the player moves
     * horizontally, the method "Horizontal" will be called.
     */
    public static boolean checkValidity(String newboard[][], int fromRow, int fromCol, int size, int toRow, int toCol) {
        /* This checks if your move is not out of bounds */
        boolean invalidMove1 = fromRow >= size || fromCol >= size || toRow >= size || toCol >= size;
        boolean invalidMove2 = fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0;
        if (invalidMove1 || invalidMove2) {
            return false;
        }

        // You are not allowed to move an empty space
        if (newboard[fromRow][fromCol].equals(".")) {
            return false;
        }

        // checks if the move is a straight line. If it's not, the move is invalid.
        double gradient = (fromRow - toRow) / (1.0 * (fromCol - toCol));
        boolean slope = gradient == 0 || gradient == 1 || gradient == -1 || gradient == 1 / 0.0 || gradient == -1 / 0.0;
        if (!slope) {
            return false;
        }

        // You are not allowed to land on your pieces
        if (newboard[fromRow][fromCol].equals(newboard[toRow][toCol])) {
            return false;
        }

        // Horizontal move
        if (fromRow == toRow) {
            return Horizontal(newboard, fromRow, fromCol, size, toRow, toCol);
        } // Vertical move
        else if (fromCol == toCol) {
            return Vertical(newboard, fromRow, fromCol, size, toRow, toCol);
        }

        // diagonal move
        if (Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol) && fromRow != toRow && !newboard[fromRow][fromCol].equals(".")) {	// South West
            if (toRow > fromRow && toCol < fromCol) {
                return SouthWest(newboard, fromRow, fromCol, size, toRow, toCol);
            } //North East
            else if (toRow < fromRow && toCol > fromCol) {
                return NorthEast(newboard, fromRow, fromCol, size, toRow, toCol);
            } //South East
            else if (toRow > fromRow && toCol > fromCol) {
                return SouthEast(newboard, fromRow, fromCol, size, toRow, toCol);
            } else {
                return NorthWest(newboard, fromRow, fromCol, size, toRow, toCol);
            }

        }

        // If the move passes all the above tests, we return true (valid)
        return true;
    }

    private static boolean Vertical(String newboard[][], int fromRow, int fromCol, int size, int toRow, int toCol) {
        int countPieces = 0;
        int numberOfPieces;

        for (int j = 0; j < size; j++) {
            if (!newboard[j][toCol].equals(".")) {
                countPieces++;
            }
        }

        numberOfPieces = countPieces;

        // If the distance from fromRow to toRow is not equal to the number of pieces, the move is invalid.
        if (Math.abs(fromRow - toRow) != numberOfPieces) {
            return false;
        }

        //You are not allowed to jump over your opponents pieces
        {
            // Downwards
            for (int i = fromRow + 1; i < toRow; i++) {
                if (!newboard[fromRow][fromCol].equals(newboard[i][fromCol]) && !newboard[i][fromCol].equals(".")) {
                    return false;
                }
            }
            // Upwards
            for (int i = fromRow - 1; i > toRow; i--) {
                if (!newboard[fromRow][fromCol].equals(newboard[i][fromCol]) && !newboard[i][fromCol].equals("."))
                    return false;
            }
        }

        return true;
    }

    private static boolean Horizontal(String newboard[][], int fromRow, int fromCol, int size, int toRow, int toCol) {
        int countPieces = 0;
        int numberOfPieces;

        for (int i = 0; i < size; i++) {
            if (!newboard[toRow][i].equals(".")) {
                countPieces++;
            }
        }
        numberOfPieces = countPieces;

        // If the distance from fromCol to toCol is not equal to the number of pieces, the move is deemed invalid.
        if (Math.abs(fromCol - toCol) != numberOfPieces) {
            return false;
        }

        // You are not allowed to jump over your opponents pieces
        {
            // to the Right
            for (int i = fromCol + 1; i < toCol; i++) {
                if (!newboard[fromRow][fromCol].equals(newboard[fromRow][i]) && !newboard[fromRow][i].equals(".")) {
                    return false;
                }
            }
            // to the Left
            for (int i = fromCol - 1; i > toCol; i--) {
                if (!newboard[fromRow][fromCol].equals(newboard[fromRow][i]) && !newboard[fromRow][i].equals(".")) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean SouthWest(String newboard[][], int fromRow, int fromCol, int size, int toRow, int toCol) {
        int count = 0;
        int min = Math.min(fromRow, (size - 1) - fromCol);
        int y = fromRow - min;
        int x = fromCol + min;
        int diff = Math.abs((size - 1) - x - y);
        int change = Math.abs(fromRow - toRow);

        for (int i = 0; i < size - diff; i++) {
            if (!newboard[x - i][y + i].equals(".")) {
                count++;  // counting the number of black or white pieces
            }
        }

        if (change != count) {
            return false;
        }

        // You are not allowed to move an empty space
        if (newboard[fromRow][fromCol].equals(".")) {
            return false;
        }

        // You are not allowed to land on your pieces
        if (newboard[fromRow][fromCol].equals(newboard[toRow][toCol])) {
            return false;
        }

        // You are not allowed to jump over your opponent's piece
        for (int i = 0; i < count; i++) {
            if (newboard[fromRow][fromCol].equals("W") && newboard[fromRow + i][fromCol - i].equals("B")) return false;

            if (newboard[fromRow][fromCol].equals("B") && newboard[fromRow + i][fromCol - i].equals("W")) return false;
        }

        return true;
    }

    private static boolean SouthEast(String newboard[][], int fromRow, int fromCol, int size, int toRow, int toCol) {
        int count = 0;
        int min = Math.min(fromRow, fromCol);
        int y = fromRow - min;
        int x = fromCol - min;
        int diff = Math.abs(x - y);
        int change = Math.abs(fromRow - toRow);

        for (int i = 0; i < size - diff; i++) {
            if (!newboard[y + i][x + i].equals(".")) {
                count++;
            }
        }

        if (change != count) {
            return false;
        }

        // You are not allowed to move an empty space
        if (newboard[fromRow][fromCol].equals(".")) {
            return false;
        }

        // You are not allowed to land on your pieces
        if (newboard[fromRow][fromCol].equals(newboard[toRow][toCol])) {
            return false;
        }

        // You are not allowed to jump over your opponent's pieces
        for (int i = 0; i < count; i++) {
            if (newboard[fromRow][fromCol].equals("W") && newboard[fromRow + i][fromCol + i].equals("B")) return false;

            if (newboard[fromRow][fromCol].equals("B") && newboard[fromRow + i][fromCol + i].equals("W")) return false;
        }

        return true;
    }

    private static boolean NorthEast(String newboard[][], int fromRow, int fromCol, int size, int toRow, int toCol) {
        int count = 0;
        int min = Math.min(fromRow, (size - 1) - fromCol);
        int y = fromRow - min;
        int x = fromCol + min;
        int diff = Math.abs((size - 1) - x - y);
        int change = Math.abs(fromRow - toRow);

        for (int i = 0; i < size - diff; i++) {
            if (!newboard[x - i][y + i].equals(".")) {
                count++; // counting the number of black or white pieces
            }
        }

        if (change != count) {
            return false;
        }

        // You are not allowed to move an empty space
        if (newboard[fromRow][fromCol].equals(".")) {
            return false;
        }

        // You are not allowed to land on your pieces
        if (newboard[fromRow][fromCol].equals(newboard[toRow][toCol])) {
            return false;
        }

        return true;
    }

    private static boolean NorthWest(String newboard[][], int fromRow, int fromCol, int size, int toRow, int toCol) {
        int count = 0;
        int min = Math.min(fromRow, fromCol);
        int y = fromRow - min;
        int x = fromCol - min;
        int diff = Math.abs(x - y);
        int change = Math.abs(fromRow - toRow);

        for (int i = 0; i < size - diff; i++) {
            if (!newboard[y + i][x + i].equals(".")) {
                count++;
            }
        }

        if (change != count) {
            return false;
        }

        // You are not allowed to move an empty space
        if (newboard[fromRow][fromCol].equals(".")) {
            return false;
        }

        // You are not allowed to land on your pieces
        if (newboard[fromRow][fromCol].equals(newboard[toRow][toCol])) {
            return false;
        }

        // You are not allowed to jump over your opponent's pieces
        for (int i = 0; i < count; i++) {
            if (newboard[fromRow][fromCol].equals("W") && newboard[fromRow - i][fromCol - i].equals("B")) {
                return false;
            }
            if (newboard[fromRow][fromCol].equals("B") && newboard[fromRow - i][fromCol - i].equals("W")) {
                return false;
            }
        }

        return true;
    }
}
