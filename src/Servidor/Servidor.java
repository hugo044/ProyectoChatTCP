package Servidor;

import Hilos.HiloServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Servidor {
    private List<PrintWriter> mensajes = new ArrayList<>();
    public static List<String> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        new Servidor().start(6001);
    }

    public void start(int puerto) {
        System.out.println("Iniciando servidor en el puerto: " + puerto);

        try {
            ServerSocket servidor = new ServerSocket(puerto);
            while(true) {
                Socket socket = servidor.accept();
                System.out.println("Conexión aceptada de " + socket.getInetAddress());

                PrintWriter mensaje = new PrintWriter(socket.getOutputStream(), true);
                mensajes.add(mensaje);

                Thread t = new Thread(new HiloServidor(socket, mensaje, mensajes, usuarios));
                t.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
