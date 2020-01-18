
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MongoOperator {
    MongoCollection collection;

    public MongoOperator() {


        var mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("PlantCare");
        collection = database.getCollection("EnvInfo");

    }

    public int insertObject(PlantEnv plantEnv) {

        Document document = new Document();
        document.put("airTemperature", plantEnv.airTemperature);
        document.put("airHumidity", plantEnv.airHumidity);
        document.put("groundHumidity", plantEnv.groundHumidity);
        document.put("light", plantEnv.light);
        document.put("date", Main.getCurrentDateTime());


        collection.insertOne(document);
        return 0;

    }

    public List getAllTemperature() {
        MongoCursor iterator = collection.find().iterator();
        return Collections.emptyList();
    }

    public void returnAllTempRecords() {

        FindIterable<Document> docs =
                collection.find();
        for (Document doc : docs) {
            doc.getString("airTemperature");
            doc.getString("date");
        }
        // .projection(Projections.fields(Projections.include("date")));

    }

    public void parseStringToMongo(String jsonString) {

        collection.insertOne(Document.parse(jsonString));
    }

    public List getAll() {
        var listOfObjects = new ArrayList<String>();


        MongoCursor iterator = collection.find().iterator();
        while (iterator.hasNext()) listOfObjects.add(iterator.next().toString());

        iterator.close();
        return listOfObjects;
    }

    public List getListOfMongoObjects(int objectsAmount) {
        var listOfMongoObjects = new ArrayList<String>();


        //Block<Document> block = Document::toJson;
        collection
                .find()
                .sort(new Document("_id", -1))
                .limit(objectsAmount)
                .forEach((Consumer<Document>) o -> {
                    o.remove("_id");
                    listOfMongoObjects.add(o.toJson());
                });


//        MongoCursor dupa = collection.find().sort(new Document("_id", -1)).limit(objectsAmount).iterator();
//        MongoCursor docs = collection.find().iterator();



        //collection.find().limit(objectsAmount);
//        for (Object o : collection.find().sort(new Document("_id", -1)).limit(objectsAmount)) {
//            listOfMongoObjects.add(o.toString());
//            return listOfMongoObjects;
//        }
        return listOfMongoObjects;
    }


    public String generateCSV(int objectsNum){
        String fullList = getListOfMongoObjects(objectsNum).toString();
        var listOfCsvLines = new ArrayList<String>();
        String csvLine = null;
        try {
            JsonNode mongoInserts = new ObjectMapper().readTree(fullList);
            CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
            JsonNode firstObject = mongoInserts.elements().next();
            firstObject.fieldNames().forEachRemaining(csvSchemaBuilder::addColumn);
            CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            csvMapper.writerFor(JsonNode.class)
                    .with(csvSchema)
                    .writeValue(new File("src/main/resources/envInfo.csv"), mongoInserts);


            BufferedReader csvReader = new BufferedReader(new FileReader("src/main/resources/envInfo.csv"));

            while (( csvLine =csvReader.readLine()) != null) {
                listOfCsvLines.add(csvLine);
            }
            csvReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return listOfCsvLines.toString();
    }


}

