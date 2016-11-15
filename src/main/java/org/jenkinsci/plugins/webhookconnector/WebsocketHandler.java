package org.jenkinsci.plugins.webhookconnector;

import hudson.Extension;
import hudson.lifecycle.Lifecycle;
import hudson.model.PeriodicWork;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;

/**
 * Created by michaelneale on 4/11/16.
 * https://www.eclipse.org/jetty/documentation/9.3.x/jetty-websocket-client-api.html
 */
@Extension
@SuppressWarnings(value = "unused")
public class WebsocketHandler extends PeriodicWork {


    public WebsocketHandler() {
        super();
        System.err.println("yay");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) { listen(); }
            }
        });
        t.start();

    }

    @Override
    protected void doRun() throws Exception {
        System.out.println("noop");
    }

    @Override
    public long getRecurrencePeriod() {
        return 20000000;
    }


    private static void listen() {
        WebSocketClient client = new WebSocketClient();
        //String destUri = "wss://cloudbees-hooksocket.beescloud.com/ws?tenant=java";
        String destUri = "ws://172.18.128.252:33048/ws?tenant=java";
        //String destUri = "ws://localhost:8888/ws?tenant=java";


        //String destUri = "ws://localhost:8888/ws?tenant=java";

        WebhookReceiver socket = new WebhookReceiver();
        try
        {
            client.start();

            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket,echoUri,request);
            System.out.printf("Connecting to : %s%n",echoUri);
            socket.await();
            System.out.println("Peace out...");

        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            try
            {
                client.stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
