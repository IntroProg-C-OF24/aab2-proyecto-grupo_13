package feria;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
public class Feria3 {
    public static void main(String[] args) {
        Scanner ap = new Scanner(System.in);
        double entradaNor = 1.5, entradaConc = 7.00, boletoEscogido = 0, Total, TotalIva;
        int cantidad , cantidadAdultos , cantidadPreferencial, hora , dia, clienteActual = 0, maxClientes = 10, fecha = 0;
        String nombCli;
        String[][] clientes = new String[maxClientes][8];
        double[][] datos = new double[10][5]; //Las filas representan los días y las columnas diferentes datos 
        String opcion = null;

        String[][] conciertos = new String[10][5];
        try {
            String csvFile = "conciertos.csv";
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line = "";

            int contador = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");

                conciertos[contador][0] = data[0]; //Artista
                conciertos[contador][1] = data[1]; //Dia
                conciertos[contador][2] = data[2]; //Fecha
                conciertos[contador][3] = data[3]; //Entrada
                conciertos[contador][4] = data[4]; //Valor

                contador++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            String csvFile = "baseDatos.csv";
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line = "";

            int contador = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");

                datos[contador][0] = Double.valueOf(data[0]); //Dia
                datos[contador][1] = Double.valueOf(data[1]); //Cantidad Entradas
                datos[contador][2] = Double.valueOf(data[2]); //Entradas Normales
                datos[contador][3] = Double.valueOf(data[3]); //Entradas Especiales
                datos[contador][4] = Double.valueOf(data[4]); //Total Ingresos 

                contador++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }   

        System.out.println("============================== BIENVENIDOS A FERIA DE LOJA ====================================");
        System.out.println("CRONOGRAMA DE FUNCIONES ");
        System.out.println("+--------------------+---------------+---------------+--------------------+----------+");
        System.out.printf("|%20s|%15s|%15s|%20s|%10s|\n", "Artista", "Dia", "Fecha", "Entrada", "Precio");
        System.out.println("+--------------------+---------------+---------------+--------------------+----------+");
        for (int j = 0; j < 10; j++) {
            System.out.printf("|%20s|%15s|%15s|%20s|%10s|\n", conciertos[j][0], conciertos[j][1], conciertos[j][2], conciertos[j][3], conciertos[j][4]);
        }
        System.out.println("+--------------------+---------------+---------------+--------------------+----------+");

        int aux=0;
        do {
            System.out.println("Nombre y apellido de la persona que desea hacer la factura: ");
            if(aux > 0){
                ap.nextLine();             
            }
            aux++;
            nombCli = ap.nextLine();
            System.out.println("Estimad@ " + nombCli);
            System.out.println(" --------<  LAS FECHAS DISPONIBLES SON >--------");
            System.out.println("[1] Viernes -> 30/08");
            System.out.println("[2] Sabado -> 31/08");
            System.out.println("[3] Domingo -> 1/09");
            System.out.println("[4] Lunes -> 2/09");
            System.out.println("[5] Martes -> 3/09");
            System.out.println("[6] Miercoles -> 4/09");
            System.out.println("[7] Jueves -> 5/09");
            System.out.println("[8] Viernes -> 6/09");
            System.out.println("[9] Sabado -> 7/09");
            System.out.println("[10] Domingo -> 8/09");
            boolean fechabool = false;
            while (fechabool == false) {
                try {
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Ingrese el numero correspondiente a la fecha deseada:");
                    fecha = ap.nextInt();
                    if (fecha>0 && fecha < 11){ //Controlamos que el día ingresado exista feria
                        fechabool = true;
                    }else{
                        System.out.println("Lo sentimos este día no hay feria :(");
                    }
                    
                } catch (Exception e) {
                    ap.nextLine();
                }
            }
            System.out.println("Ingrese la hora que desea asistir: (Ejemplo: 12)");
            hora = ap.nextInt();
            
            boletoEscogido = boletosEscogidos(hora, fecha, boletoEscogido, entradaConc, entradaNor );
            System.out.println("Cuantos boletos de adultos desea comprar: ");
            cantidadAdultos = ap.nextInt();
            System.out.println("Cuantos boletos preferenciales desea comprar: ");
            cantidadPreferencial = ap.nextInt();
            
            cantidad = cantidadAdultos + cantidadPreferencial;

            Total = totalPagar(cantidadAdultos, boletoEscogido ,1 );
            Total += totalPagar(cantidadPreferencial, boletoEscogido ,2 );

            clientes[clienteActual][0] = nombCli;
            clientes[clienteActual][1] = obtenerFecha((int)(fecha));
            clientes[clienteActual][2] = Integer.toString(hora);
            clientes[clienteActual][3] = Double.toString(boletoEscogido);
            clientes[clienteActual][4] = Integer.toString(cantidad);
            clientes[clienteActual][5] = Integer.toString(cantidadAdultos);
            clientes[clienteActual][6] = Integer.toString(cantidadPreferencial);
            clientes[clienteActual][7] = Double.toString(Total);
            mostrarFactura(clientes[clienteActual]);

            clienteActual++;

            //Almacenamos la información en la matriz de datos 
            datos[fecha-1][1] += cantidad; // Añadimos la cantidad de nuevas entradas
            if(boletoEscogido == 1.5){
                datos[fecha-1][2] += cantidad; // Añadimos la cantidad de nuevas entradas Normales
            }else if (boletoEscogido == 7.0){
                datos[fecha-1][3] += cantidad; // Añadimos la cantidad de nuevas entradas Especiales
            }                     
            datos[fecha-1][4] += Total; // Añadimos la cantidad de ingresos generados 

            ap.nextLine();
            System.out.println("Desea ingresar informacion de un cliente nuevo? (Si/No): ");
            opcion = ap.next();
        } while (opcion.equals("si"));

        actualizarArchivoDatos(datos);
    }
    
    public static String obtenerFecha(int f){
        switch(f){
            case 1: return "Viernes -> 30/08";
            case 2: return "Sabado -> 31/08";
            case 3: return "Domingo -> 1/09";
            case 4: return "Lunes -> 2/09";
            case 5: return "Martes -> 3/09";
            case 6: return "Miercoles -> 4/09";
            case 7: return "Jueves -> 5/09";
            case 8: return "Viernes -> 6/09";
            case 9: return "Sabado -> 7/09";
            case 10: return "Domingo -> 8/09";
            default: return "No especificada";
        }
    }

    public static void actualizarArchivoDatos(double[][] datos){ //Se guardan los datos registrados de la compra
        try {
            Formatter escritura = new Formatter("baseDatos.csv", "UTF-8", Locale.US);

            for(int i=0 ; i<10 ; i++){
                escritura.format("%s;%.0f;%.0f;%.0f;%.2f\n", datos[i][0], datos[i][1], datos[i][2], datos[i][3], datos[i][4]);
            }
            escritura.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        double diaMayorIngresos = diaMayorIngresos(datos);
        double diaMasEntradasVendidas = diaMasEntradasVendidas(datos);
        double totalPersonasAsistidas = totalPersonasAsistidas(datos);

        System.out.println("");
        System.out.println("RESULTADOS ESTADISTICOS");
        System.out.println("-------------------------------------------------------");
        System.out.println("El dia con mayor ingresos obtenidos es el : "+ obtenerFecha((int)(diaMayorIngresos)));
        System.out.println("El dia con mas entradas vendidas es el : "+ obtenerFecha((int)(diaMasEntradasVendidas)));
        System.out.println("El numero total de asistidos es : "+ totalPersonasAsistidas+ " personas");
    }

    public static double boletosEscogidos(int hora, int fecha, double boletoEscogido, double entradaConc, double entradaNor ) {
        switch (fecha) {
            case 1:
                boletoEscogido = entradaConc;
                if (hora > 16) {
                    System.out.println("Su boleto cuesta $ 7,00");
                } else {
                    System.out.println("Su boleto cuesta $ 1,50");
                    boletoEscogido -= 5.50;
                }
                break;
            case 2:
                boletoEscogido = entradaConc;
                if (hora > 16) {
                    System.out.println("Su boleto cuesta $ 7,00");
                } else {
                    System.out.println("Su boleto cuesta $ 1,50");
                    boletoEscogido -= 5.50;
                }
                break;

            case 3:
                boletoEscogido = entradaNor;
                break;
            case 4:
                boletoEscogido = entradaNor;
                break;
            case 5:
                boletoEscogido = entradaNor;
                break;
            case 6:
                boletoEscogido = entradaNor;
                break;
                
            case 7:
                boletoEscogido = entradaConc;
                if (hora > 16) {
                    System.out.println("Su boleto cuesta $ 7,00");
                } else {
                    System.out.println("Su boleto cuesta $ 1,50");
                    boletoEscogido -= 5.50;
                }
                break;
                
            case 8:
                if (hora > 16) {
                    System.out.println("Su boleto cuesta $ 7,00");
                } else {
                    System.out.println("Su boleto cuesta $ 1,50");
                    boletoEscogido -= 5.50;
                }
                break;
                
            case 9:
                boletoEscogido = entradaConc;
                if (hora > 16) {
                    System.out.println("Su boleto cuesta $ 7,00");
                } else {
                    System.out.println("Su boleto cuesta $ 1,50");
                    boletoEscogido -= 5.50;
                }
                break;
            case 10:
                boletoEscogido = entradaNor;
                break;

            default:
                System.out.println("Esos dias no hay feria");
        }

        return boletoEscogido;
    }

    public static double totalPagar(int cantidad, double boletoEscogido , int tipo) {
        double total = 0;
        total = cantidad * boletoEscogido;

        if (tipo == 1){
            total = cantidad * boletoEscogido;
        }else if(tipo == 2){
            total = cantidad * boletoEscogido/2; //Boletos preferenciales valen la mitad
        }

        //Generamos una promocion por cantidad de entradas compradas (Promocion solo para adultos)
        if (boletoEscogido == 7 && cantidad > 3){ // Si es día especial y compra mas de 3 entradas las entradas valen 5
            System.out.println("Por la compra de mas de entradas sus voletos cuestan 5 dolares ");
            total = cantidad * 5;
        }

        return total;
    }
    
    public static void mostrarFactura(String[] cliente) {
        double totalSinIva = Double.parseDouble(cliente[7]);
        double iva = totalSinIva * 0.12;
        double totalConIva = totalSinIva + iva;
        
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~ FACTURA ~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Nombre: " + cliente[0]);
        System.out.println("Fecha: " + cliente[1]);
        System.out.println("Hora: " + (cliente[2]+":00"));
        System.out.println("Precio Boleto: $" + cliente[3]);
        System.out.println("Cantidad Boletos: " + cliente[4]);
        System.out.println("Cantidad Boletos Normales: " + cliente[5]);
        System.out.println("Cantidad Boletos Preferenciales: " + cliente[6]);
        System.out.println("Total sin IVA: $" + totalSinIva);
        System.out.println("Total con IVA: $" + totalConIva);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        
        generarArchivoFactura(cliente , totalSinIva , totalConIva , cliente[0] );
    }
    
    public static void generarArchivoFactura(String[] cliente , double totalSinIva , double totalConIva , String nombre ){
        try {
            String nombreArchivo = "Cliente_"+nombre+".csv";
            Formatter escritura = new Formatter(nombreArchivo, "UTF-8", Locale.US);
            
            escritura.format("%s\n","~~~~~~~~~~~~~~~~~~~~~~~~~~ FACTURA ~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            escritura.format("%s%s\n","Nombre: ", cliente[0]);
            escritura.format("%s%s\n","Fecha: ", cliente[1]);
            escritura.format("%s%s\n","Hora: ", (cliente[2]+":00"));
            escritura.format("%s%s\n","Precio Boleto: $",cliente[3]);
            escritura.format("%s%s\n","Cantidad: ",cliente[4] );
            escritura.format("%s%s\n","Cantidad Boletos Normales: ",cliente[5] );
            escritura.format("%s%s\n","Cantidad Boletos Preferenciales: ",cliente[6] );
            escritura.format("%s%.2f\n","Total sin IVA: $",totalSinIva);
            escritura.format("%s%.2f\n","Total con IVA: $",totalConIva);
            escritura.format("%s\n","~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            escritura.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static double diaMayorIngresos(double[][] datos){
        double maxValue=0;
        double dia = 0;
        for(int i=0 ; i<10 ; i++){
            if(datos[i][4] > maxValue){
                maxValue = datos[i][4];
                dia = datos[i][0];
            }
        }
        return dia;
    }

    public static double diaMasEntradasVendidas(double[][] datos){
        double maxValor=0;
        double dia = 0;
        for(int i=0 ; i<10 ; i++){
            if(datos[i][1] > maxValor){
                maxValor = datos[i][1];
                dia = datos[i][0];
            }
        }
        return dia;
    }

    public static double totalPersonasAsistidas(double[][] datos){
        double suma=0;
        for(int i=0 ; i<10 ; i++){
            suma += datos[i][1];
        }
        return suma;
    }

}
/*
============================== BIENVENIDOS A FERIA DE LOJA ====================================
CRONOGRAMA DE FUNCIONES 
+--------------------+---------------+---------------+--------------------+----------+
|             Artista|            Dia|          Fecha|             Entrada|    Precio|
+--------------------+---------------+---------------+--------------------+----------+
|               Morat|        Viernes|          30/08|   Entrada Concierto|      7.00|
|     Sonora dinamita|         Sabado|          31/08|   Entrada Concierto|      7.00|
|                    |        Domingo|           1/09|      Entrada Normal|      1.50|
|                    |          Lunes|           2/09|      Entrada Normal|      1.50|
|                    |         Martes|           3/09|      Entrada Normal|      1.50|
|                    |      Miercoles|           4/09|      Entrada Normal|      1.50|
|       Tierra Canela|         Jueves|           5/09|   Entrada Concierto|      7.00|
|             Americo|        Viernes|           6/09|   Entrada Concierto|      7.00|
|         Mon laferte|         Sabado|           7/09|   Entrada Concierto|      7.00|
|                    |        Domingo|           8/09|      Entrada Normal|      1.50|
+--------------------+---------------+---------------+--------------------+----------+
Nombre y apellido de la persona que desea hacer la factura: 
Paula Rivas
Estimad@ Paula Rivas
 --------<  LAS FECHAS DISPONIBLES SON >--------
[1] Viernes -> 30/08
[2] Sabado -> 31/08
[3] Domingo -> 1/09
[4] Lunes -> 2/09
[5] Martes -> 3/09
[6] Miercoles -> 4/09
[7] Jueves -> 5/09
[8] Viernes -> 6/09
[9] Sabado -> 7/09
[10] Domingo -> 8/09
-----------------------------------------------------
Ingrese el numero correspondiente a la fecha deseada:
3
Ingrese la hora que desea asistir: (Ejemplo: 12)
14
Cuantos boletos de adultos desea comprar: 
4
Cuantos boletos preferenciales desea comprar: 
3
~~~~~~~~~~~~~~~~~~~~~~~~~~ FACTURA ~~~~~~~~~~~~~~~~~~~~~~~~~~~
Nombre: Paula Rivas
Fecha: Domingo -> 1/09
Hora: 14:00
Precio Boleto: $1.5
Cantidad Boletos: 7
Cantidad Boletos Normales: 4
Cantidad Boletos Preferenciales: 3
Total sin IVA: $8.25
Total con IVA: $9.24
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Desea ingresar informacion de un cliente nuevo? (Si/No): 
si
Nombre y apellido de la persona que desea hacer la factura: 
Alisson Condoy
Estimad@ Alisson Condoy
 --------<  LAS FECHAS DISPONIBLES SON >--------
[1] Viernes -> 30/08
[2] Sabado -> 31/08
[3] Domingo -> 1/09
[4] Lunes -> 2/09
[5] Martes -> 3/09
[6] Miercoles -> 4/09
[7] Jueves -> 5/09
[8] Viernes -> 6/09
[9] Sabado -> 7/09
[10] Domingo -> 8/09
-----------------------------------------------------
Ingrese el numero correspondiente a la fecha deseada:
4
Ingrese la hora que desea asistir: (Ejemplo: 12)
16
Cuantos boletos de adultos desea comprar: 
9
Cuantos boletos preferenciales desea comprar: 
5
~~~~~~~~~~~~~~~~~~~~~~~~~~ FACTURA ~~~~~~~~~~~~~~~~~~~~~~~~~~~
Nombre: Alisson Condoy
Fecha: Lunes -> 2/09
Hora: 16:00
Precio Boleto: $1.5
Cantidad Boletos: 14
Cantidad Boletos Normales: 9
Cantidad Boletos Preferenciales: 5
Total sin IVA: $17.25
Total con IVA: $19.32
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Desea ingresar informacion de un cliente nuevo? (Si/No): 
si
Nombre y apellido de la persona que desea hacer la factura: 
Diego Jimenez
Estimad@ Diego Jimenez
 --------<  LAS FECHAS DISPONIBLES SON >--------
[1] Viernes -> 30/08
[2] Sabado -> 31/08
[3] Domingo -> 1/09
[4] Lunes -> 2/09
[5] Martes -> 3/09
[6] Miercoles -> 4/09
[7] Jueves -> 5/09
[8] Viernes -> 6/09
[9] Sabado -> 7/09
[10] Domingo -> 8/09
-----------------------------------------------------
Ingrese el numero correspondiente a la fecha deseada:
1
Ingrese la hora que desea asistir: (Ejemplo: 12)
18
Su boleto cuesta $ 7,00
Cuantos boletos de adultos desea comprar: 
6
Cuantos boletos preferenciales desea comprar: 
1
Por la compra de mas de entradas sus voletos cuestan 5 dolares 
~~~~~~~~~~~~~~~~~~~~~~~~~~ FACTURA ~~~~~~~~~~~~~~~~~~~~~~~~~~~
Nombre: Diego Jimenez
Fecha: Viernes -> 30/08
Hora: 18:00
Precio Boleto: $7.0
Cantidad Boletos: 7
Cantidad Boletos Normales: 6
Cantidad Boletos Preferenciales: 1
Total sin IVA: $33.5
Total con IVA: $37.519999999999996
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Desea ingresar informacion de un cliente nuevo? (Si/No): 
si
Nombre y apellido de la persona que desea hacer la factura: 
Leydi
Estimad@ Leydi
 --------<  LAS FECHAS DISPONIBLES SON >--------
[1] Viernes -> 30/08
[2] Sabado -> 31/08
[3] Domingo -> 1/09
[4] Lunes -> 2/09
[5] Martes -> 3/09
[6] Miercoles -> 4/09
[7] Jueves -> 5/09
[8] Viernes -> 6/09
[9] Sabado -> 7/09
[10] Domingo -> 8/09
-----------------------------------------------------
Ingrese el numero correspondiente a la fecha deseada:
1
Ingrese la hora que desea asistir: (Ejemplo: 12)
16
Su boleto cuesta $ 1,50
Cuantos boletos de adultos desea comprar: 
6
Cuantos boletos preferenciales desea comprar: 
4
~~~~~~~~~~~~~~~~~~~~~~~~~~ FACTURA ~~~~~~~~~~~~~~~~~~~~~~~~~~~
Nombre: Leydi
Fecha: Viernes -> 30/08
Hora: 16:00
Precio Boleto: $1.5
Cantidad Boletos: 10
Cantidad Boletos Normales: 6
Cantidad Boletos Preferenciales: 4
Total sin IVA: $12.0
Total con IVA: $13.44
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Desea ingresar informacion de un cliente nuevo? (Si/No): 
no

RESULTADOS ESTADISTICOS
-------------------------------------------------------
El dia con mayor ingresos obtenidos es el : Viernes -> 30/08
El dia con mas entradas vendidas es el : Viernes -> 30/08
El numero total de asistidos es : 106.0 personas
*/

