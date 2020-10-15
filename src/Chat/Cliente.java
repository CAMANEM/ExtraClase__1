package Chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase se encarga de realizar el envío del mensaje
 */
public class Cliente implements Runnable {

    private int puerto;
    private String mensaje;
    private final static Logger logger = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ); // usa el logger configurado en la clase Interfaz

    /**
     * Constructor de la clase
     * @param puerto
     * @param mensaje
     */
    public Cliente(int puerto, String mensaje) {
        this.puerto = puerto;
        this.mensaje = mensaje;
    }

    /**
     * Hilo con el que se envía el mensaje
     */
    @Override
    public void run() {
        //Host del servidor
        final String HOST = "127.0.0.1";
        //Puerto del servidor
        DataOutputStream out;

        try {
            //Creo el socket para conectarme con el cliente
            Socket socket = new Socket(HOST, puerto);

            out = new DataOutputStream(socket.getOutputStream());

            //Envio un mensaje al cliente
            out.writeUTF(mensaje);

            socket.close();

        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Fallo al enviar mensaje");
        }

    }
}