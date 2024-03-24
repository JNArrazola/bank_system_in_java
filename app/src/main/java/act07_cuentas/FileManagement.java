package act07_cuentas;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FileManagement {
    private final static String appPath = new File("").getAbsolutePath() + "/appData/";
    private final static String personasFilePath = appPath + "personas";
    private final static String cuentasDebitoPath = appPath + "debito";
    private final static String cuentasCreditoPath = appPath + "credito";

    public static void verificacionInicial(){
        if(!((new File(appPath)).exists())){
            new File(appPath).mkdir();
            new File(personasFilePath).mkdir();
            new File(cuentasDebitoPath).mkdir();
            new File(cuentasCreditoPath).mkdir();
        }
    }

    public static void serializarCuentasDebito(HashMap<String, ArrayList<Debito>> hashMap) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fw = new FileWriter(cuentasDebitoPath + "/debito_hashmap.json")) {
            gson.toJson(hashMap, fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, ArrayList<Debito>> deserializarCuentasDebito() {
        File file = new File(cuentasDebitoPath + "/debito_hashmap.json");
        if (!file.exists()) {
            return new HashMap<>();
        }

        HashMap<String, ArrayList<Debito>> hashMap;
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(file)) {
            Type tipoHashMap = new TypeToken<HashMap<String, ArrayList<Debito>>>(){}.getType();
            hashMap = gson.fromJson(reader, tipoHashMap);
            if (hashMap == null) {
                return new HashMap<>();
            } else {
                return hashMap;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static void serializarClientes(ArrayList<Cliente> c){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try(FileWriter fw = new FileWriter(personasFilePath + "/personas.json")){
            gson.toJson(c, fw);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Cliente> deserializarClientes() {
        if(!(new File(personasFilePath + "/personas.json").exists())){
            return new ArrayList<Cliente>();
        }

        ArrayList<Cliente> arr;
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(personasFilePath + "/personas.json")) {
            Type tipoListaClientes = new TypeToken<ArrayList<Cliente>>(){}.getType();
            arr = gson.fromJson(reader, tipoListaClientes);
            if(arr==null)
                return new ArrayList<Cliente>();
            else
                return arr;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void serializarCuentasCredito(HashMap<String, ArrayList<Credito>> hashMap) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fw = new FileWriter(cuentasDebitoPath + "/credito_hashmap.json")) {
            gson.toJson(hashMap, fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, ArrayList<Credito>> deserializarCuentasCredito() {
        File file = new File(cuentasDebitoPath + "/credito_hashmap.json");
        if (!file.exists()) {
            return new HashMap<>();
        }

        HashMap<String, ArrayList<Credito>> hashMap;
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(file)) {
            Type tipoHashMap = new TypeToken<HashMap<String, ArrayList<Credito>>>(){}.getType();
            hashMap = gson.fromJson(reader, tipoHashMap);
            if (hashMap == null) {
                return new HashMap<>();
            } else {
                return hashMap;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
