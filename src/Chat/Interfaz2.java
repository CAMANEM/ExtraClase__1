package Chat;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Interfaz2 implements Observer {

    String username;
    String destinatario; //cambiar a int
    JFrame ventana;
    JButton btn_enviar;
    JTextField txt_mensaje;
    JTextArea vista_chat;
    JPanel contenedor_vista_chat;
    JPanel contenedor_btntxt;
    JScrollPane scroll;

    public static void main(String[] args) {
        new Interfaz2();
    }

    public Interfaz2(){
        username = JOptionPane.showInputDialog("Digite su nombre de usuario: ");
        destinatario = JOptionPane.showInputDialog("Digite el puerto de su destinatario: ");
        crearInterfaz();
        IniciarServidor();

    }

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

        btn_enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviar_msg();
            }
        });
    }

    private void enviar_msg(){

        vista_chat.append("Tú: " + this.txt_mensaje.getText() + "\n");

        Cliente envia_mensaje = new Cliente(5000, username + "  " + this.txt_mensaje.getText() + "\n");
        Thread hilo_cliente = new Thread(envia_mensaje);
        hilo_cliente.start();
    }

    public void IniciarServidor(){
        Servidor servidor = new Servidor(6000);
        servidor.addObserver(this);
        Thread hilo_servidor = new Thread(servidor);
        hilo_servidor.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        this.vista_chat.append((String) arg);
    }
}