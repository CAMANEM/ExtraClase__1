package Chat;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class Interfaz {

    JFrame ventana_chat;
    JButton btn_enviar;
    JTextField txt_mensaje;
    JTextArea area_chat;
    JPanel contenedor_areachat;
    JPanel contenedor_btntxt;
    JScrollPane scroll;
    Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {
        new Interfaz();
    }

    public Interfaz(){
        hacerInterfaz();
    }

    public void hacerInterfaz(){
        ventana_chat = new JFrame("Chat");
        btn_enviar = new JButton("Enviar");
        txt_mensaje = new JTextField(4);
        area_chat = new JTextArea(27, 3);
        //area_chat.setEditable(false);
        scroll = new JScrollPane(area_chat);
        contenedor_areachat = new JPanel();
        contenedor_areachat.setLayout(new GridLayout(1,1));
        contenedor_areachat.add(scroll);
        contenedor_btntxt = new JPanel();
        contenedor_btntxt.setLayout(new GridLayout(1,2));
        contenedor_btntxt.add(txt_mensaje);
        contenedor_btntxt.add(btn_enviar);
        ventana_chat.setLayout(new BorderLayout());
        ventana_chat.add(contenedor_areachat, BorderLayout.NORTH);
        ventana_chat.add(contenedor_btntxt, BorderLayout.SOUTH);
        ventana_chat.setSize(500, 500);
        ventana_chat.setLocationRelativeTo(null);
        ventana_chat.setVisible(true);
        ventana_chat.setResizable(false);
        ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
