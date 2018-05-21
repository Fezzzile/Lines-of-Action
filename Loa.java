
/**
 * This is where everything starts, because this class holds the main method
 *Compiling this class also compiles evey class in the code
 */
public class Loa {

    public static void main(String args[]) {
        // R04: Your program MUST accept at least two command-line arguments
        if (args.length < 2) {
            System.out.println("ERROR: too few arguments");
            return;
        }

        int size = Integer.parseInt(args[0]);
        int mode = Integer.parseInt(args[1]);
        String ipAddress = "";

        if (args.length == 3) {
            ipAddress = args[2];
        }

        // M02: Your program MUST complain if the third argument is missing.
        if (mode == 2 && args.length < 3) {
            System.out.println("ERROR :( there is no IP Address.");
            System.exit(0);
        }

        // R06: Your program MUST complain and terminate immediately if the mode is something other than 0, 1, or 2 or 3.
        boolean legalMode = (mode == 0 || mode == 1 || mode == 2 || mode == 3);
        if (!legalMode) {
            System.out.println("ERROR: illegal mode");
            return;
        }

        /* R05: If the size is smaller than 4 or more than 16, your program must display "ERROR: illegal size" and terminate immediately */
        if (size < 4 || size > 16) {
            System.out.println("ERROR: illegal size");
            return;
        }

        Board.board(size, mode, ipAddress);
    }
}
