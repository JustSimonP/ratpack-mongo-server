
import java.io.IOException;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {

//        var date =  getCurrentDateTime();
//        var plantInfo = new PlantEnv("123.44",
//                "34.12",
//                "34.55",
//                "500",
//                date.toString());
//        new MongoOperator().insertObject(plantInfo);

        new  RequestHandler();
        //new MongoOperator().getListOfMongoObjects(30);

        //System.out.println(new MongoOperator().generateCSV(700));
    }

    public static LocalDateTime getCurrentDateTime(){
        return LocalDateTime.now().withNano(0);
    }
}
