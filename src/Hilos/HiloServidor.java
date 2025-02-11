package Hilos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class HiloServidor implements Runnable {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private List<PrintWriter> mensajes;
    private List<String> usuarios;
    private String nombre;

    public HiloServidor(Socket socket, PrintWriter pw, List<PrintWriter> mensajes, List<String> usuarios) {
        this.socket = socket;
        this.pw = pw;
        this.mensajes = mensajes;
        this.usuarios = usuarios;

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error recibiendo InputStream del cliente");
            throw new RuntimeException(e);
        }
    }

    private void muestraMensajes(List<PrintWriter> mensajes, String mensaje) {
        for(PrintWriter mj: mensajes) {
            mj.println(mensaje);
        }
    }

    private void notificarConexionUsuarios() {
        StringBuilder usuariosConectados = new StringBuilder();
        usuariosConectados.append("/usuarios");
        for(int i=0; i<usuarios.size(); i++) {
            usuariosConectados.append(" ").append(usuarios.get(i));
        }
        for(PrintWriter mj: mensajes) {
            mj.println(usuariosConectados.toString());
        }
    }

    @Override
    public void run() {
        String mensaje;
        try {
            while((mensaje = br.readLine())!=null) {
                if(mensaje.startsWith("/nick") && usuarios.contains(mensaje.substring(5))) {
                    nombre = mensaje.substring(5);
                    System.out.println("El usuario " + nombre + " ya existe");
                    pw.println("/fail");
                    socket.close();
                } else {
                    System.out.println("Mensaje recibido -> " + mensaje);
                    if(mensaje.startsWith("/nick")) {
                        nombre = mensaje.substring(5);
                        usuarios.add(nombre);
                        notificarConexionUsuarios();
                    }
                    muestraMensajes(mensajes, mensaje);
                }
            }
            System.err.println("El cliente " + nombre + " se ha desconectado");
            usuarios.remove(nombre);
            notificarConexionUsuarios();
            socket.close();
        } catch(IOException e) {
            System.err.println("El cliente " + nombre + " se ha desconectado");
        }
    }
}
