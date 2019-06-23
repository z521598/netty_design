package com.baidu.reactor;

import com.baidu.reactor.core.HtmlWrapperUtils;
import com.baidu.reactor.core.Processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClassicalServer implements Runnable {
    private ServerSocket serverSocket;

    public ClassicalServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket.getInputStream(), socket.getOutputStream());
                handler.process();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Handler implements Processor {
        private InputStream inputStream;
        private OutputStream outputStream;

        public Handler(InputStream inputStream, OutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public void process() throws IOException {
            byte[] req = new byte[1024 * 10];
            inputStream.read(req);
            byte[] res = HtmlWrapperUtils.wrapperHttpHtmlRepoonse(req);
            outputStream.write(res);
            outputStream.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new ClassicalServer(80)).start();
    }
}
