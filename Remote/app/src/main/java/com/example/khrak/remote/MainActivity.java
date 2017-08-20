package com.example.khrak.remote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Socket socket = null;

    private InetAddress serverAddr = null;

    private PrintWriter out = null;

    private static final int SERVERPORT = 2221;
    private static final String SERVER_IP = "192.168.1.10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = (Button) findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.message_view);

                String message = editText.getText().toString();

                editText.setText("");

                sendMessage(message);
            }
        });
    }

    private void sendMessage(final String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Inside thread");

                try {
                    System.out.println("Creating address");

                    if (serverAddr == null) {
                        serverAddr = InetAddress.getByName(SERVER_IP);

                        System.out.println("Creating socket");
                        socket = new Socket(serverAddr, SERVERPORT);

                        System.out.println("Creating output stream");

                        OutputStream stream = socket.getOutputStream();

                        System.out.println("Creating printwriter");

                        out = new PrintWriter(stream);
                    }

                    System.out.println("Sending");

                    out.println(message);

                    out.flush();

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String reply = in.readLine();

                    System.out.println(reply);

                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }
}
