package Chat;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.zip.ZipEntry;


public class Interfaz implements Observer {

    String username;
    int destinatario = 0;
    JList chats;
    Servidor servidor;
    JFrame ventana;
    JTextField txt_mensaje;
    JTextArea vista_chat;


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
        crearInterfazGrafica();
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
    public void crearInterfazGrafica() {

        //BorderLayout North
        JPanel zona_superior = new JPanel();
        zona_superior.setLayout(new BorderLayout());
        JButton btn_destinatario = new JButton("Agregar Destinatario");
        btn_destinatario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarDestinatario();
            }
        });
        zona_superior.add(btn_destinatario, BorderLayout.NORTH);//agregar aqui el comando para cambiar puerto

        //BorderLayout West
        JPanel zona_izquierda = new JPanel();
        zona_izquierda.setLayout(new BorderLayout());
        chats = new JList();
        chats.setFont(new Font("Arial", Font.ITALIC,20));
        String[] data = {"Hernan(5000)","Julio(6000)"};
        chats.setListData(data);
        JScrollPane scroll_chats = new JScrollPane(chats);
        zona_izquierda.add(scroll_chats);

        //BorderLayout Center
        JPanel zona_Central = new JPanel();
        zona_Central.setLayout(new BorderLayout());
        vista_chat = new JTextArea(10, 1);
        vista_chat.setEditable(false);
        JScrollPane scroll = new JScrollPane(vista_chat); //quitar de argumentos
        zona_Central.add(scroll);

        //BorderLayout South
        JPanel zona_inferior = new JPanel();
        zona_inferior.setLayout(new GridLayout());
        txt_mensaje = new JTextField(1);
        JButton btn_enviar = new JButton("Enviar");
        zona_inferior.add(txt_mensaje);
        zona_inferior.add(btn_enviar);

        ventana = new JFrame("Chat");
        ventana.setLayout(new BorderLayout());
        ventana.add(zona_superior, BorderLayout.NORTH);
        ventana.add(zona_izquierda, BorderLayout.WEST);
        ventana.add(zona_Central, BorderLayout.CENTER);
        ventana.add(zona_inferior, BorderLayout.SOUTH);
        ventana.setSize(500, 500);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        vista_chat.append("Mi puerto: " + servidor.getPuerto() + "\n" + "\n");

        btn_enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviar_msg();
            }
        });
    }

    /**
     * Agrega un nuevo puerto para enviar mensajes
     */
    public void agregarDestinatario(){
        destinatario = Integer.parseInt(JOptionPane.showInputDialog("Digite el puerto de su destinatario: "));
    }

    /**
     * Crea ina instancia de la clase Cliente para enviar un mensaje al presionar el botón de enviar mensaje
     */
    private void enviar_msg(){

        if (destinatario != 0) {

            vista_chat.append("Tú: " + this.txt_mensaje.getText() + "\n");

            Cliente envia_mensaje = new Cliente(destinatario, username + ":  " + this.txt_mensaje.getText() + "\n");
            Thread hilo_cliente = new Thread(envia_mensaje);
            hilo_cliente.start();
        }
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