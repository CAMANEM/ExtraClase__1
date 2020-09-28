package Chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class Servidor extends Observable implements Runnable {


    private int puerto;

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
            System.out.println("Servidor iniciado");

            //Este segundo try se encarga de estar siempre esperando un mensaje
            while (true) {

                //Espero a que un cliente se conecte
                socket = servidor.accept();

                System.out.println("Cliente conectado");

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
            System.out.println("error");
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