package com.gary.tcp_test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class MainActivity extends Activity {
    EditText input;
    TextView show;
    Button send;
    Handler handler;
    ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.edit_box);
        show = (TextView) findViewById(R.id.info_show);
        send = (Button) findViewById(R.id.btn_send);
        handler = new Handler(){
            public  void handleMessage(Message msg){
                if(msg.what == 0x123){
                    Log.d("tcp test","show info");
                    show.append("\n"+msg.obj.toString());
                }
            }
        };
        clientThread = new ClientThread(handler);
        new Thread(clientThread).start();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Log.d("tcp test","btn handle");
                    Message msg = new Message();
                    msg.what = 0x345;
                    msg.obj = input.getText().toString();
                    clientThread.revHandler.sendMessage(msg);
                    input.setText("");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public class ClientThread implements Runnable{
        private Socket s;
        private Handler handler;
        private Handler revHandler;
        BufferedReader br = null;
        OutputStream os = null;
        public ClientThread(Handler handler){
            this.handler = handler;
        }
        public void run(){
            try{
                Log.d("tcptest","111111111111");
                s = new Socket();
                s.setSoTimeout(100);
                s.connect(new InetSocketAddress("www.baidu.com",8080));
                Log.d("tcptest","222222222222");
                br = new BufferedReader(new InputStreamReader((s.getInputStream())));
                os = s.getOutputStream();
                Log.d("tcp test","start tcp client");
                new Thread(){
                    public void run(){
                        String content = null;
                        char[] temp = new char[100];
                        Log.d("tcp test","get server info");
                        try{
                            while(true){
                                br.read(temp);
                                content = String.valueOf(temp);
                                if(content != null) {
                                    Log.d("tcp test","get server info");
                                    Message msg = new Message();
                                    msg.what = 0x123;
                                    msg.obj = content;
                                    handler.sendMessage(msg);
                                    content = null;
                                }
                            }
                        }
                        catch (Exception e){
                            Log.d("tcp test","get server err..........");
                            e.printStackTrace();
                        }
                    }
                }.start();
                Looper.prepare();
                revHandler = new Handler(){
                    public void handleMessage(Message msg){
                        if(msg.what == 0x345){
                            try{
                                Log.d("tcp test","send server info");
                                os.write((msg.obj.toString() + "\r\n").getBytes("utf-8"));
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                };
                Looper.loop();
            } catch (Exception e) {
                Log.d("tcptest","tcp connect failToast................");
                Toast.makeText(getApplicationContext(),"tcp connect fail",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }
}
