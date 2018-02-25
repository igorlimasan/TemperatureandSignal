package com.proj.temperatureandsignal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.HashMap;
import java.util.Map;


public class ConnectionMongo {

    private Context context;


    public ConnectionMongo(Context context) {
        this.context = context;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void saveData(float battery, int signal, double latittude, double longitude, String date) {

        if (isConnected(context)) {
            MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://user:user@temp-signal-shard-00-00-jykq1.mongodb.net:27017,temp-signal-shard-00-01-jykq1.mongodb.net:27017,temp-signal-shard-00-02-jykq1.mongodb.net:27017/test?ssl=true&replicaSet=temp-signal-shard-0&authSource=admin"));
            MongoDatabase tempSignal = mongoClient.getDatabase("temp-signal");
            final MongoCollection leituras = tempSignal.getCollection("leituras");

            final Document dados = new Document();
            dados.put("battery", battery);
            dados.put("signal", signal);
            dados.put("latitude", latittude);
            dados.put("longitude", longitude);
            dados.put("datetime", date);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    leituras.insertOne(dados);
                    return;
                }
            });
            thread.start();


            Toast.makeText(context, "Dados Salvos!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }


    }
}


