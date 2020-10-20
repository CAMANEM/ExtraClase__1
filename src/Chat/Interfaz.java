package Chat;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.logging.*;



public class Interfaz implements Observer {

    private final static Logger logger = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );


    String username;
    int mi_puerto;
    int destinatario = 0;
    JList chats; //contiene los puertos en Jlist
    ArrayList contactos = new ArrayList(); //Arreglo que permite modificar la Jlist
    Servidor servidor;
    JFrame ventana;
    JTextField txt_mensaje;
    JTextArea vista_chat;
    Map<String, String> conversaciones = new HashMap<>();
    java.lang.String destinatario_comparador;

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

        configurarLogger();
        username = JOptionPane.showInputDialog("Digite su nombre de usuario: ");
        IniciarServidor();
        crearInterfazGrafica();
    }

    public void configurarLogger(){

        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("Logging_File.log", false);
            fileHandler.setFormatter(new SimpleFormatter()); // simplifica los logs
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        }
        catch (IOException e){

            logger.log(Level.SEVERE, "Error al generar fileHandler y Logging_File.log", e);
        }
    }

    /**
     * Crea el servidor y lo mantiene siempre a la espera de mensajes mediante un hilo.
     */
    public void IniciarServidor(){
        servidor = new Servidor();
        servidor.addObserver(this);
        Thread hilo_servidor = new Thread(servidor);
        hilo_servidor.start();
        mi_puerto = servidor.getPuerto();

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
        zona_superior.add(btn_destinatario, BorderLayout.NORTH);

        //BorderLayout Center
        JPanel zona_Central = new JPanel();
        zona_Central.setLayout(new BorderLayout());
        vista_chat = new JTextArea(10, 1);
        vista_chat.setEditable(false);
        JScrollPane scroll = new JScrollPane(vista_chat); //quitar de argumentos
        zona_Central.add(scroll);


        //BorderLayout West
        JPanel zona_izquierda = new JPanel();
        zona_izquierda.setLayout(new BorderLayout());
        chats = new JList();
        chats.setFont(new Font("Arial", Font.ITALIC,20));
        contactos.add(servidor.getPuerto());
        chats.setListData(contactos.toArray());
        JScrollPane scroll_chats = new JScrollPane(chats);
        zona_izquierda.add(scroll_chats);
        chats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chats.addListSelectionListener(e -> {
            destinatario = (int) chats.getSelectedValue();
            if (conversaciones.get(Integer.toString(destinatario)) == null){
                vista_chat.setText("Mi puerto: " + servidor.getPuerto() + "\n" + "\n");
            }
            else {
                vista_chat.setText(conversaciones.get(Integer.toString(destinatario)));
            }

        });


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
     * Agrega un nuevo puerto para enviar mensajes, se activa con el botón de agregar destinatario
     */
    public void agregarDestinatario(){
        try {
            int nuevo_destinatario = Integer.parseInt(JOptionPane.showInputDialog("Digite el puerto de su destinatario: "));
            if (contactos.contains(nuevo_destinatario) == false) {
                contactos.add(nuevo_destinatario);
                chats.setListData(contactos.toArray());
            }
        }
        catch (RuntimeException e){
            logger.log(Level.SEVERE, "Fallo al agregar destinatario. Puede que no haya un servidor en ese puerto ó que el el destino ingresado no sea un numero entero válido");
        }
    }

    /**
     * Crea ina instancia de la clase Cliente para enviar un mensaje al presionar el botón de enviar mensaje
     */
    private void enviar_msg(){

        if (destinatario != 0) {

            vista_chat.append("Tú: " + this.txt_mensaje.getText() + "\n");

            Cliente envia_mensaje = new Cliente(destinatario,  servidor.getPuerto() + ":" +"(" +username + ")" + ":  " + this.txt_mensaje.getText() + "\n ");
            Thread hilo_cliente = new Thread(envia_mensaje);
            hilo_cliente.start();
            if (conversaciones.get(Integer.toString(destinatario)) == null){
                conversaciones.put(Integer.toString(destinatario), "Tú: " + this.txt_mensaje.getText() + "\n");
            }
            else {
                conversaciones.put(Integer.toString(destinatario), conversaciones.get(Integer.toString(destinatario)) + "Tú: " + this.txt_mensaje.getText() + "\n");
            }
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
        try {
            String[] mensaje_dividido = ((String) arg).split(":");
            if (Integer.toString(destinatario).equals(mensaje_dividido[0])) {
                this.vista_chat.append((String) arg);
            }

            if (conversaciones.get(mensaje_dividido[0]) == null) {
                conversaciones.put(mensaje_dividido[0], (String) arg);
                if (contactos.contains(Integer.parseInt(mensaje_dividido[0])) == false) {
                    contactos.add(Integer.parseInt(mensaje_dividido[0]));
                    chats.setListData(contactos.toArray());
                }
            } else {
                conversaciones.put(mensaje_dividido[0], conversaciones.get(mensaje_dividido[0]) + (String) arg);
                System.out.println(conversaciones.get(mensaje_dividido[0]));
            }
        }
        catch (RuntimeException e){

            logger.log(Level.SEVERE, "Error al intentar agregar el mensaje entrante de un nuevo chat a la lista de conversaciones de chats");
        }
        }

}