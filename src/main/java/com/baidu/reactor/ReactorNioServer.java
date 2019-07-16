package com.baidu.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

// FIXME
/*
Channels:连接到文件，Socket 等，支持非阻塞读取
Buffers:类似数组的对象，可由通道直接读取或写入
Selectors:通知哪组通道有 IO 事件
SelectionKeys:维护 IO 事件的状态和绑定信息
 */
public class ReactorNioServer implements Runnable {
    private final Selector selector;
    private final ServerSocketChannel serverSocket;

    ReactorNioServer(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        // 将通道注册到选择器上,并注册的操作为：“接收”操作
        SelectionKey selectionKey = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Acceptor());
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()) {
                    dispatch((SelectionKey) it.next());
                }
                selected.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatch(SelectionKey selectionKey) {
        //调用之前注册的callback对象
        Runnable r = (Runnable) selectionKey.attachment();
        if (r != null) {
            r.run();
        }
    }

    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocket.accept();
                new Handler(selector, socketChannel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Handler implements Runnable {
        private SocketChannel socketChannel;
        final SelectionKey sk;
        ByteBuffer input = ByteBuffer.allocate(1024 * 10);
        ByteBuffer output = ByteBuffer.allocate(1024 * 10);
        static final int READING = 0, SENDING = 1;
        int state = READING;

        public Handler(Selector selector, SocketChannel socketChannel) throws IOException {
            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false);
            // Optionally try first read now
            sk = serverSocket.register(selector, 0);
            // 将Handler作为callback对象
            sk.attach(this);
            // 注册Read就绪事件
            sk.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        }

        boolean inputIsComplete() {
            return true;
        }

        boolean outputIsComplete() {
            return true;
        }

        void process() { /* ... */ }

        @Override
        public void run() {
            try {
                if (state == READING) {
                    read();
                } else if (state == SENDING) {
                    send();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void read() throws IOException {
            socketChannel.read(input);
            if (inputIsComplete()) {
                process();
                state = SENDING;
                sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        void send() throws IOException {
            socketChannel.write(output);
            if (outputIsComplete()) {
                sk.cancel();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new ReactorNioServer(80)).start();
    }
}


