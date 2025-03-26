import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/*
 * IOHandler
 * handles the input and outpput
 */
public class IOHandler {
    OutputStream out = null;
    InputStream in = null;
    Socket socket = null;

    /*
     * Setting up the socket and streams
     */
    public IOHandler(int port) {
        try { // open socket to Game Engine
            this.socket = new Socket("localhost", port);
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Could not bind to port: " + port);
            System.exit(-1);
        }
    }

    /* printToTerminal
     * prints the action to terinal to initaite the move
     */
    public void printToTerminal(char action) throws IOException {
        this.out.write(action);
    }

    
    public char[][] read() throws IOException {
        char view[][] = new char[5][5];
        char ch;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!((i == 2) && (j == 2))) {
                    ch = (char) in.read();
                    view[i][j] = ch;
                    if (ch == -1) {
                        System.exit(-1);
                    }
                }
            }
        }
        view[2][2] = ' ';
        return view;
    }

    public static void print_view(char view[][]) {
        int i, j;
        System.out.println("\n+-----+");
        for (i = 0; i < 5; i++) {
            System.out.print("|");
            for (j = 0; j < 5; j++) {
                if ((i == 2) && (j == 2)) {
                    System.out.print('^');
                } else {
                    System.out.print(view[i][j]);
                }
            }
            System.out.println("|");
        }
        System.out.println("+-----+");
    }

}