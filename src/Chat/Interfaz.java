package Chat;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;


public class Interfaz implements Observer {

    String username;
    int destinatario;
    int puerto;
    Servidor servidor;
    JFrame ventana;
    JButton btn_enviar;
    JTextField txt_mensaje;
    JTextArea vista_chat;
    JPanel contenedor_vista_chat;
    JPanel contenedor_btntxt;
    JScrollPane scroll;


    /**
     * Main en el que se inicializa el programa, llama al constructor de la interfaz.
     * @param args
     */
    public static void main(String[] args) {

        new Interfaz();
    }

    /**
     * Constructor en el que se inicia el servidor, se crea la interfaz y se define el nombre del usuario.
     */
    public Interfaz(){
        username = JOptionPane.showInputDialog("Digite su nombre de usuario: ");
        IniciarServidor();
        crearInterfaz();
        destinatario = Integer.parseInt(JOptionPane.showInputDialog("Digite el puerto de su destinatario: "));
        System.out.println(destinatario);

    }

    /**
     * Crea el servidor y lo mantiene siempre a la espera de mensajes mediante un hilo.
     */
    public void IniciarServidor(){
        servidor = new Servidor();
        servidor.addObserver(this);
        Thread hilo_servidor = new Thread(servidor);
        hilo_servidor.start();

    }

    /**
     * Se crea todo lo referente a la interfaz gráfica.
     */
    public void crearInterfaz() {
        ventana = new JFrame("Chat");
        btn_enviar = new JButton("Enviar");
        txt_mensaje = new JTextField(4);
        vista_chat = new JTextArea(27, 3);
        vista_chat.setEditable(false);
        scroll = new JScrollPane(vista_chat);
        contenedor_vista_chat = new JPanel();
        contenedor_vista_chat.setLayout(new GridLayout(1, 1));
        contenedor_vista_chat.add(scroll);
        contenedor_btntxt = new JPanel();
        contenedor_btntxt.setLayout(new GridLayout(1, 2));
        contenedor_btntxt.add(txt_mensaje);
        contenedor_btntxt.add(btn_enviar);
        ventana.setLayout(new BorderLayout());
        ventana.add(contenedor_vista_chat, BorderLayout.NORTH);
        ventana.add(contenedor_btntxt, BorderLayout.SOUTH);
        ventana.setSize(500, 500);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        puerto = servidor.getPuerto();
        vista_chat.append("Mi puerto: " + String.valueOf (puerto) + "\n" + "\n");

        btn_enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviar_msg();
            }
        });
    }

    /**
     * Crea ina instancia de la clase Cliente para enviar un mensaje al presionar el botón de enviar mensaje
     */
    private void enviar_msg(){

        vista_chat.append("Tú: " + this.txt_mensaje.getText() + "\n");

        Cliente envia_mensaje = new Cliente(destinatario, username + ":  " + this.txt_mensaje.getText() + "\n");
        Thread hilo_cliente = new Thread(envia_mensaje);
        hilo_cliente.start();
    }


    /**
     * Observer que se activa cuando el servidor recibe un mensaje.
     * Este método muestra el mensaje recibido en la ventana de chat
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        this.vista_chat.append((String) arg);
    }
}