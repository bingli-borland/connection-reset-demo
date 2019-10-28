package com.weiquding.connection.reset.demo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoServer.class);


    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8084)) {
            while(true){
                Socket accept = serverSocket.accept();
                new Thread(new ThreadHandler(accept)).start();
            }
        } catch (IOException e) {
            LOGGER.error("serverSocket异常", e);
        }
    }

    static class ThreadHandler implements Runnable{

        private Socket socket;

        public ThreadHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                // 设置SO_LINGER
                socket.setSoLinger(true, 0);
                inputStream = socket.getInputStream();
                byte[] rcvBytes = new byte[1024];
                inputStream.read(rcvBytes);
                LOGGER.info("receive data:[{}]", new String(rcvBytes, StandardCharsets.UTF_8));
                outputStream = socket.getOutputStream();
                outputStream.write("server socket".getBytes(StandardCharsets.UTF_8));
                socket.close();
            } catch (IOException e) {
               LOGGER.error("处理socket异常", e);
            }finally {
                if(inputStream != null){
                    try {
                        inputStream.close();
                    }catch (IOException e){
                        LOGGER.error("关闭inputStream异常", e);
                    }
                }
                if(outputStream != null){
                    try {
                        outputStream.close();
                    }catch (IOException e){
                        LOGGER.error("关闭outputStream异常", e);
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    LOGGER.error("关闭socket异常", e);
                }
            }

        }
    }
}
