package Hilos;

import Cliente.Cliente;
import Servidor.Servidor;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HiloCliente implements Runnable {
    private BufferedReader br;
    private JTextArea ta;
    JTextArea ta2;
    List<String> nombreUsuarios;

    public HiloCliente(BufferedReader br, JTextArea ta, JTextArea ta2, List<String> nombreUsuarios) {
        this.br = br;
        this.ta = ta;
        this.ta2 = ta2;
        this.nombreUsuarios = nombreUsuarios;
    }

    private void actualizarNombresUsuarios() {
        ta2.setText("");
        for(int i=0; i<nombreUsuarios.size(); i++) {
            ta2.append(nombreUsuarios.get(i) + "\n");
        }
    }

    @Override
    public void run() {
        String mensaje;
        boolean fallo = false;
        try {
            while((mensaje = br.readLine())!=null && !fallo) {
                if(mensaje.startsWith("/fail")) {
                    JOptionPane.showMessageDialog(null, "El nombre de usuario ya está en uso. Elija otro");
                    fallo = true;
                } else {
                    if(mensaje.startsWith("/nick")) {
                        String nombre = mensaje.substring(5);
                        nombreUsuarios.add(nombre);
                    } else if(mensaje.startsWith("/usuarios")) {
                        String[] datos = mensaje.substring(9).split(" ");
                        nombreUsuarios.clear();
                        for(int i=0; i<datos.length; i++) {
                            if(!datos[i].isEmpty()) {
                                nombreUsuarios.add(datos[i]);
                            }
                        }
                        actualizarNombresUsuarios();
                    } else {
                        ta.append(mensaje + "\n");
                    }
                }
            }
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Error al leer mensajes del servidor");
        }
    }
}
