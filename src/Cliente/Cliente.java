package Cliente;

import Hilos.HiloCliente;
import Servidor.Servidor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Cliente extends JFrame {
    private String nombreUsuario;
    private JPanel panel1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextField textField1;
    private JButton button1;
    private PrintWriter pw;
    private BufferedReader br;

    public static void main(String[] args) throws IOException {
        Cliente cliente = new Cliente();
        cliente.crearInterfazInicio();
        cliente.start("localhost", 6001, cliente);
    }

    private void crearInterfazInicio() {
        boolean entradaValida = false;
        while (!entradaValida) {
            nombreUsuario = JOptionPane.showInputDialog("Introduce tu nombre de usuario:");

            if (nombreUsuario == null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Quieres salir del programa?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    nombreUsuario = JOptionPane.showInputDialog("Introduce tu nombre de usuario:");
                }
            } else {
                if(nombreUsuario.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debes ingresar un nombre de usuario. Inténtalo de nuevo.");
                    nombreUsuario = JOptionPane.showInputDialog("Introduce tu nombre de usuario:");
                } else {
                    entradaValida = true;
                }
            }
        }
    }

    private void start(String host, int port, Cliente cliente) throws IOException {
        System.out.println("Conectado con " + host + ": " + port);

        Socket socket = new Socket(host, port);
        pw = new PrintWriter(socket.getOutputStream(), true);
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        pw.println("/nick" + nombreUsuario);

        crearInterfazChat(cliente);
        HiloCliente h = new HiloCliente(br, textArea1, textArea2, Servidor.usuarios);
        Thread t = new Thread(h);
        t.start();
    }

    private void crearInterfazChat(Cliente cliente) {
        cliente.setContentPane(cliente.panel1);
        cliente.setTitle("Chat TCP - (" + nombreUsuario + ")");
        cliente.setSize(875, 675);
        cliente.setVisible(true);
        cliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button1 = new JButton("Enviar");

        textArea1.setEditable(false);
        textArea2.setEditable(false);
    }

    public Cliente() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.println(nombreUsuario + ": " + textField1.getText());
                textField1.setText("");
            }
        });
    }
}
