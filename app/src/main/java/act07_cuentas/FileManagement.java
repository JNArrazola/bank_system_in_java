package act07_cuentas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

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

    /**
     * Function to serialize actual Date for credit cards
     * @param date
     * @author Joshua
      */
    public static void serializeDate(Date date) {
        Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
        
        try (FileWriter writer = new FileWriter(appPath + "date.json")) {
            gson.toJson(date, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Date deserializeDate() {
        if(!(new File(appPath + "date.json").exists())){
            return null;
        }
        
        Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
        
        try (BufferedReader br = new BufferedReader(new FileReader(appPath + "date.json"))) {
            String json = br.readLine(); 
            if (json == null || json.isEmpty()) {
                return null; 
            }
            return gson.fromJson(json, Date.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null; 
        }
    }
}
