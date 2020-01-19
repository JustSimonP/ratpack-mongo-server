import ratpack.exec.Blocking;
import ratpack.handling.Context;
import ratpack.jackson.Jackson;
import ratpack.server.PublicAddress;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

import java.net.URI;
import java.util.List;

public class RequestHandler {

    //PATH: http://localhost:5050/choosenEndpoint
    public RequestHandler(){
        try {
            RatpackServer.start(ratpackServerSpec ->
                    ratpackServerSpec

                            //.serverConfig(ServerConfig.embedded().publicAddress(new URI("http://192.168.1.13:5050")))

                            .handlers(chain ->chain
                                    .get("getAll",
                                            context -> context.render(Jackson.json(new MongoOperator().getAll())))


                                    .get("test",
                                            context -> context.render(Jackson.json("Connection to server functions properly!")))
                                    .get("getAllSync", context ->
                                            context.byMethod(byMethodSpec ->
                                                    byMethodSpec.get(this::getListOfEnvInfo)))

                                    .post("insertObject",context ->
                                            context.byMethod(byMethodSpec ->
                                                    byMethodSpec.post(this::createEnvInfo)))

                                    .get(":number",context ->
                                            context.byMethod(byMethodSpec ->
                                                    byMethodSpec.get(this::getSpecificAmountEnvInfo)))
                                    .get("csv/:number",context ->
                                           context.byMethod(byMethodSpec ->
                                                   byMethodSpec.get(this::getCsvString)) )
                            ));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createEnvInfo(Context context){
       // PublicAddress address = context.get(PublicAddress.class);
        context.getRequest().getBody()

                .onError(error -> context.getResponse().status(500).send(error.getMessage()))
                .then(body ->
                        new MongoOperator().parseStringToMongo(body.getText()));


    }
    public void getListOfEnvInfo(Context context){
        Blocking
                .get(()->new MongoOperator().getAll())
                .then(value->context.render(Jackson.json(value)));
    }

    public void getSpecificAmountEnvInfo(Context context){
        Blocking
                .get(()->new MongoOperator()
                        .getListOfMongoObjects(Integer.parseInt(context.getPathTokens().get("number"))))
                .then(value->context.render(value));
    }
    public void getCsvString(Context context){
        Blocking
                .get(()->new MongoOperator()
                .generateCSV(Integer.parseInt(context.getPathTokens().get("number"))))
                .then(context::render);
    }

}
