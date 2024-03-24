package act07_cuentas;

import java.util.*;
import java.text.*;

public class ManejadorClientes {
    private static ArrayList<Cliente> clientes =  FileManagement.deserializarClientes();
    private static Scanner in = new Scanner(System.in);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    /*******************************************************************/
    /*CREAR CLIENTE*/
    /*******************************************************************/
    public static void crearCliente() throws ParseException{
        boolean esPersonaFisica;
        do {
            System.out.println("¿Es una persona fisica?\n1) Sí\n2) No");
            int dec = Integer.parseInt(in.nextLine());
            if(dec == 1){
                esPersonaFisica = true;
                break;
            } else if (dec == 2){
                esPersonaFisica = false;
                break;
            }
        } while (true);

        System.out.println("Ingresa el nombre "+ ((esPersonaFisica) ? "de la persona" : "de la empresa") + ": ");
        String nombre = in.nextLine().toUpperCase();
        
        String apellido;
        do {
            if(esPersonaFisica){
                System.out.println("Ingresa el apellido: ");
                apellido = in.nextLine().toUpperCase();
                
                String[] words = apellido.trim().split(" ");
                if(words.length<2){
                    System.out.println("Ingresa ambos apellidos, no uno.");
                } else {
                    break;
                }
            } else {
                apellido = "";
                break;
            }
    
        } while (esPersonaFisica);
        System.out.println("Ingresa el email: ");
        String email = in.nextLine();

        String fechaStr;
        Date fechaNacimiento;
        do {
            System.out.println("Ingresa la fecha de " + ((esPersonaFisica) ? "nacimiento" : "fundación") +"(mm/dd/yyyy): ");
            fechaStr = in.nextLine();
            if(isValidDate(fechaStr)){
                fechaNacimiento = sdf.parse(fechaStr);
                break;
            } else {
                System.out.println("Fecha inválida");
            }
        } while (true);
        
        String rfc;
        if(esPersonaFisica){
            rfc = generateRfcPersonaFisica(nombre, apellido, fechaNacimiento);
        } else {
            rfc = generateRfcPersonaMoral(nombre, fechaNacimiento);
        }
        
        do {
            if(!validarRfc(rfc)){
                if(esPersonaFisica){
                    rfc = generateRfcPersonaFisica(nombre, apellido, fechaNacimiento);
                }
            } else {
                break;
            }
        } while (true);

        System.out.println("Persona agregada con éxito");
    
        clientes.add(new Cliente(rfc, nombre, apellido, esPersonaFisica, email, fechaNacimiento));
    }
    
    public static boolean isValidDate(String dateStr) {
        sdf.setLenient(false);
        try {
            @SuppressWarnings("unused")
            Date date = sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean esVocal(char c) {
        return "AEIOUaeiou".indexOf(c) != -1;
    }
    
    public static String generateRfcPersonaFisica(String nombre, String apellidos, Date birthDate) {
        Random rnd = new Random();
        StringBuilder rfc = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);

        String[] apellidoWords = apellidos.trim().split(" ");
        String primerApellido = apellidoWords[0];
    
        rfc.append(primerApellido.charAt(0));
    
        boolean huboVocal = false;
        for (int i = 1; i < primerApellido.length(); i++) {
            char c = primerApellido.charAt(i);
            if (esVocal(c)) {
                rfc.append(c);
                huboVocal = true;
                break;
            }
        }
    
        if(!huboVocal) rfc.append(primerApellido.charAt(1));
        if (apellidoWords.length > 1) {
            rfc.append(apellidoWords[1].charAt(0));
        } else {
            rfc.append("X");
        }
        
        int yearLastTwoDigits = calendar.get(Calendar.YEAR) % 100;
        rfc.append(String.format("%02d", yearLastTwoDigits)); 
        rfc.append(String.format("%02d", calendar.get(Calendar.MONTH) + 1)); 
        rfc.append(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))); 

        for (int i = 0; i < 3; i++) {
            int numero = rnd.nextInt(10) + 1;
            rfc.append(numero);
        }

        return rfc.toString().toUpperCase(); 
    }

    public static String generateRfcPersonaMoral(String nombreEmpresa, Date birthDate) {
        Random rnd = new Random();
        StringBuilder rfc = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);
        
        rfc.append(nombreEmpresa.substring(0, Math.min(nombreEmpresa.length(), 3)).toUpperCase());
        
        int yearLastTwoDigits = calendar.get(Calendar.YEAR) % 100;
        rfc.append(String.format("%02d", yearLastTwoDigits)); 
        rfc.append(String.format("%02d", calendar.get(Calendar.MONTH) + 1)); 
        rfc.append(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));  

        for (int i = 0; i < 3; i++) {
            int numero = rnd.nextInt(10) + 1;
            rfc.append(numero);
        }

        return rfc.toString().toUpperCase(); 

    }
    
    /**
     * Función que válida que no se repita RFC
     * @param rfc
     * @return
      */
    private static boolean validarRfc(String rfc){
        // validar que el rfc no se repita
        for(Cliente c : clientes)
            if(c.getRfc().equals(rfc))
                return false;
        
        return true;
    }

    /*******************************************************************/
    /*****************************BORRAR********************************/
    /*******************************************************************/
    public static void borrarCliente(){
        System.out.println("Ingresa el RFC del cliente a borrar: ");
        String rfc = in.nextLine().toUpperCase();
        
        Cliente c = buscarCliente(rfc);

        if(c == null){
            System.out.println("No se encontró el cliente");
            return;
        }
        
        ArrayList<Debito> cuentasDebito = ManejadorDebito.obtenerListaCuentas(rfc); 

        if(cuentasDebito.isEmpty()){
            ManejadorDebito.eliminarRegistro(rfc);
            clientes.remove(c);
            System.out.println("Cliente borrado éxitosamente\n\n");
            return;
        } 
        System.out.println("El cliente aún tiene cuentas activas\n\n");
    }

    public static Cliente buscarCliente(String rfc){
        for(Cliente c : clientes){
            if(c.getRfc().equals(rfc)){
                return c;
            }
        }
        return null;
    }   

    /*******************************************************************/
    /*LISTAR*/
    /*******************************************************************/
    public static void listarNombre(String subcadena){
        ArrayList<Cliente> tipo = new ArrayList<>();
        
        for(Cliente c : clientes)
            if(c.getNombre().toUpperCase().startsWith(subcadena.toUpperCase()))
                tipo.add(c);
        
        if(tipo.isEmpty()){
            System.out.println("No se encontraron coincidencias en la búsqueda");
            return;
        }
        
        for(Cliente c : tipo)
            System.out.println(c.toString());
    }

    public static void listarApellido(String subcadena){
        ArrayList<Cliente> tipo = new ArrayList<>();
        
        for(Cliente c : clientes)
            if(c.getApellidos().toUpperCase().startsWith(subcadena.toUpperCase()))
                tipo.add(c);
        
        if(tipo.isEmpty()){
            System.out.println("No se encontraron coincidencias en la búsqueda");
            return;
        }
        
        for(Cliente c : tipo)
            System.out.println(c.toString());
    }

    public static void listarNombreCompleto(String subcadena){
        ArrayList<Cliente> tipo = new ArrayList<>();
        
        for(Cliente c : clientes)
            if((c.getNombre() + c.getApellidos()).startsWith(subcadena.toUpperCase()))
                tipo.add(c);
        
        if(tipo.isEmpty()){
            System.out.println("No se encontraron coincidencias en la búsqueda");
            return;
        }
        
        for(Cliente c : tipo)
            System.out.println(c.toString());
    }
    
    public static void listarPorRfc(String rfc){
        Cliente c = buscarCliente(rfc);
        if(c==null){
            System.out.println("Cliente no encontrado");
            return;
        }

        System.out.println("Cliente encontrado con éxito");
        System.out.println(c.toString() + "\n");
    }   


    public static void listarClientes(){
        if(clientes.isEmpty()){
            System.out.println("No hay clientes registrados");
            return;
        }
        
        System.out.println("===Lista de todos los clientes ===");
        for(Cliente c : clientes)
            System.out.println(c.toString());    
    }


    /*******************************************************************/
    /*****************************MODIFICAR*****************************/
    /*******************************************************************/
    public static void modificarCliente() throws ParseException{
        int opc = 0;
        System.out.println("Ingresa el RFC del cliente a modificar: ");
        String rfc = in.nextLine().toUpperCase();
        Cliente c = buscarCliente(rfc);
        if(c==null){
            System.out.println("Cliente no encontrado");
            return;
        }

        do {
            Menus.menuModificaciones();
            opc = Integer.parseInt(in.nextLine());
            switch (opc) {
                case 1:
                    System.out.println("Ingresa el nuevo nombre: ");
                    c.setNombre(in.nextLine().toUpperCase());
                    break;
                case 2:
                    if (c.getEsPersonaFisica()) {
                        System.out.println("Ingresa el nuevo apellido: ");
                        c.setApellidos(in.nextLine().toUpperCase());
                    } else {
                        System.out.println("No puedes modificar el apellido de una persona moral");
                    }
                    break;
                case 3: // fecha de nacimiento
                    System.out.println("Ingresa la nueva fecha de nacimiento: ");
                    String fechaStr = in.nextLine();
                    if (isValidDate(fechaStr)) {
                        c.setFechaNacimiento(sdf.parse(fechaStr));
                    } else {
                        System.out.println("Fecha inválida");
                    }
                    break;
                case 4: // email
                    System.out.println("Ingresa el nuevo email: ");
                    c.setEmail(in.nextLine());
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        } while (opc != 0);
    }

    /* 
     * ****************************************************************************
     * Funciones de guardado
     * ****************************************************************************
     */ 

    public static void save(){
        FileManagement.serializarClientes(clientes);
    }
    
}
