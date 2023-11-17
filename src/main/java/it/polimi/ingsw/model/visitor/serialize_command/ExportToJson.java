package it.polimi.ingsw.model.visitor.serialize_command;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.model.state.State;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportToJson {
    public void visit(State state) {
        Gson gson = CustomJsonBuilder.createDeserializer();
        String storageFolder = System.getProperty("user.dir");
        File file = new File(storageFolder, "test.json");
        try {
            FileWriter writer = new FileWriter(file.getAbsolutePath());
            writer.write(gson.toJson(state));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
