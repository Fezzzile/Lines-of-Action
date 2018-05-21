
public class Util {

    public static boolean isConnected(String[][] matrix, String value) {

        int rows = matrix.length;
        int columns = matrix[0].length;
        int minR = -1, maxR = -1;
        int minC = -1, maxC = -1;
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (matrix[r][c].equals(value)) {
                    if ((minR == -1) || (r < minR)) {
                        minR = r;
                    }
                    if ((maxR == -1) || (r > maxR)) {
                        maxR = r;
                    }
                    if ((minC == -1) || (c < minC)) {
                        minC = c;
                    }
                    if ((maxC == -1) || (c > maxC)) {
                        maxC = c;
                    }
                    count++;
                }
            }
        }

        if (count == 0) {
            return true;
        }

        boolean[][] visited = new boolean[rows][columns];
        int found = connectedExplore(minR, minC, visited, matrix, value, minR, maxR, minC, maxC);
        return (found == count);
    }

    private static int connectedExplore(int r, int c, boolean[][] visited, String[][] matrix, String value, int minR, int maxR, int minC, int maxC) {
        if ((r < minR) || (r > maxR)) {
            return 0;
        }
        if ((c < minC) || (c > maxC)) {
            return 0;
        }
        if (visited[r][c]) {
            return 0;
        }
        visited[r][c] = true;
        if (!matrix[r][c].equals(value)) {
            return 0;
        }
        int reachableCount = 1;
        reachableCount += connectedExplore(r, c + 1, visited, matrix, value, minR, maxR, minC, maxC);
        reachableCount += connectedExplore(r, c - 1, visited, matrix, value, minR, maxR, minC, maxC);
        reachableCount += connectedExplore(r + 1, c, visited, matrix, value, minR, maxR, minC, maxC);
        reachableCount += connectedExplore(r - 1, c, visited, matrix, value, minR, maxR, minC, maxC);
        reachableCount += connectedExplore(r + 1, c + 1, visited, matrix, value, minR, maxR, minC, maxC);
        reachableCount += connectedExplore(r + 1, c - 1, visited, matrix, value, minR, maxR, minC, maxC);
        reachableCount += connectedExplore(r - 1, c + 1, visited, matrix, value, minR, maxR, minC, maxC);
        reachableCount += connectedExplore(r - 1, c - 1, visited, matrix, value, minR, maxR, minC, maxC);
        return reachableCount;
    }
}
