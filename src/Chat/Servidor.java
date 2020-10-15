package Chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Observable implements Runnable {


    private int puerto;
    private final static Logger logger = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ); // usa el logger configurado en la clase Interfaz

    /**
     * Constructor
     */
    public Servidor() {
        puerto = 5000;
    }

    /**
     * Hilo en el que el se crea el servidor y se espera la llegada de un mensaje
     * Al recibir un mensaje, lo pasa a la interfaz mediante el observable.
     */
    @Override
    public void run() {

        ServerSocket servidor = null;
        Socket socket = null;
        DataInputStream in;


        while (puerto < 10000){

        //Este primer try busca un puerto libre (del 5000 al 10000) para establecer el servidor
        try {
            //Crea el socket del servidor
            servidor = new ServerSocket(puerto);
            logger.info("Servidor iniciado");

            //Este segundo try se encarga de estar siempre esperando un mensaje
            while (true) {

                //Espero a que un cliente se conecte
                socket = servidor.accept();
                logger.info("Mensaje entrante detectado");

                in = new DataInputStream(socket.getInputStream());

                //Leo el mensaje que me envia
                String mensaje = in.readUTF();


                this.setChanged();
                this.notifyObservers(mensaje);
                this.clearChanged();

                //Cierro el socket
                socket.close();

            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Fallo al recibir un nuevo mensaje ó iniciarlizar servidor");
            puerto++;
        }
        }

    }

    /**
     * Método para obtener el puerto en el que se encuentra el servidor
     * @return
     */
    public int getPuerto() {
        return puerto;
    }

}