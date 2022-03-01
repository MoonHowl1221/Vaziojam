package com.company;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class ItemDAO {

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection <Document> mongoCollection;

    public ItemDAO() {
        // Prisijungimas prie MongoDB serverio.
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        // Prisijungimas prie duomenu bazes.
        mongoDatabase = mongoClient.getDatabase("db");
        // Priejimas prie DB esancios kolekcijos (lentele).
        mongoCollection = mongoDatabase.getCollection("items");
    }

    public void insert(Item item) {
        // Susikurimas naujo dokumento (iraso)
        Document document = new Document();
        // Uzpildysime naujai sukurta dokumenta informacija is item objekto.
        // itemId = raktas, item.getItemId = reiksme (JSON kodas).
        // Norint uzpildyti irasa, turime iterpti rakta ir reiksme.
        document.append("itemId", item.getItemId())
                .append("description", item.getDescription())
                .append("manufacturer", item.getManufacturer())
                .append("price", item.getPrice())
                .append("quantity", item.getQuantity());

        try {
            // Idejimas iraso (item) i kolekcija
            mongoCollection.insertOne(document);
            System.out.println("Pavyko iterpti nauja irasa i duomenu baze.");
        } catch (MongoWriteException exception) {
            exception.printStackTrace();
            System.out.println("Nepavyko sukurti naujo iraso.");
        }
    }

    public void update(Item item) {
        try {
            // Kad atnaujinti dokumenta duomenu bazeje, pirmiausia reikia ji surasti, kad galetume modifikuoti.
            // first() - grazina pirmaji elementa.
            Document document = mongoCollection.find(Filters.eq("itemId", item.getItemId())).first();
            // Atnaujinimas dokumento duomenu bazeje pagal item objekto savybes, kuri perduodame per parametrus.
            mongoCollection.updateOne(new Document("_id", document.get("_id")),             // _id - tai yra vidinis indetifikatorius dokumento esancio MongoDB.
                    new Document("$set", new Document("itemId", item.getItemId())
                            .append("description", item.getDescription())
                            .append("manufacturer", item.getManufacturer())
                            .append("price", item.getPrice())
                            .append("quantity", item.getQuantity())
                    )
            );
            System.out.println("Irasa pavyko sekmingai atnaujinti, kurio id yra " + item.getItemId());
        } catch (MongoWriteException exception) {
            exception.printStackTrace();
            System.out.println("Nepavyko atnaujinti iraso, kurio id yra " + item.getItemId());
        }
    }

    public void searchByDescription(String description) {
        try {
            // Paieskosime dokumenta pagal description
            Document document = mongoCollection.find(Filters.eq("description", description)).first();
            System.out.println("Irasa pavyko sekmingai atrasti pagal descriptiona, kuris yra " + description);
            System.out.println(document.toString());
        } catch (MongoWriteException exception) {
            exception.printStackTrace();
            System.out.println("Nepavyko atrasti iraso pagal descriptiona, kuris yra " + description);
        }
    }

    public void deleteById(int id) {
        try {
            // Suradimas ir istrynimas dokumento pagal id.
            Document document = mongoCollection.findOneAndDelete(Filters.eq("itemId", id));
            System.out.println("Pavyko sekmingai pasalinti irasa pagal id, kuris yra " + id);
        } catch (MongoWriteException exception) {
            exception.printStackTrace();
            System.out.println("Nepavyko pasalinti iraso pagal id, kuris yra " + id);
        }
    }

    public void searchByQuantityGreaterThan(int quantity) {
        try {
            Document document = mongoCollection.find(Filters.gt("quantity", quantity)).first();
            System.out.println("Pavyko sekmingai atspausdinti irasus, kuriu kiekis yra didesnis, nei " + quantity);
            System.out.println(document.toString());
        } catch (MongoWriteException exception) {
            exception.printStackTrace();
            System.out.println("Nepavyko atspausdinti irasus, kuriu kiekis yra didesnis, nei " + quantity);
        }
    }
}
