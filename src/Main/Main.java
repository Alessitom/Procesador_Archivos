package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static final DateTimeFormatter Formato_fecha_hora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    static final Pattern MATRICULA_ESPANOLA_ACTUAL = Pattern.compile("\\b\\d{4}\\s*[BCDFGHJKLMNPRSTVWXYZ]{3}\\b");
    static final Pattern MATRICULA_ESPANOLA_ANTIGUA = Pattern.compile("\\b[A-Z]{1,3}[-\\s]?\\d{4}[-\\s]?[A-Z]{1,2}\\b");
    static final String PATHNAME = "./archivos/";

    static class Infraccion {
        LocalDateTime fechayhora;
        double velocidadpermitida;
        double velocidadregistada;
        double importe;

        public Infraccion(LocalDateTime fechayhora, double velocidadregistada, double velocidadpermitida, double importe) {
            this.fechayhora = fechayhora;
            this.velocidadpermitida = velocidadpermitida;
            this.velocidadregistada = velocidadregistada;
            this.importe = importe;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, LinkedList<Infraccion>> MapInfracciones = new LinkedHashMap<>();
        System.out.println("¿Qué archivo quieres leer?");
        String archivo = sc.nextLine();
        try (BufferedReader lector = new BufferedReader(new FileReader(PATHNAME + archivo + ".txt"))) {
           String Linea;
            while ((Linea=lector.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(Linea, "#$&");
                if (tokenizer.countTokens() == 5) {
                    String fechaStr = tokenizer.nextToken().trim();
                    String horaStr = tokenizer.nextToken().trim();
                    double velocidadregistada = Double.parseDouble(tokenizer.nextToken().trim());
                    double velocidadpermitida = Double.parseDouble(tokenizer.nextToken().trim());
                    String matricula = tokenizer.nextToken().trim().replaceAll("[\\s-]", "");

                    if (esMatriculaEspaniola(matricula)) {
                        LocalDateTime fechayhora = LocalDateTime.parse(fechaStr + " " + horaStr, Formato_fecha_hora);
                        double importeMulta = calcularImporteMultas(velocidadregistada, velocidadpermitida);
                        Infraccion infraccion = new Infraccion(fechayhora, velocidadregistada, velocidadpermitida, importeMulta);

                        if (!MapInfracciones.containsKey(matricula)) {
                            MapInfracciones.put(matricula, new LinkedList<>());
                        }
                        MapInfracciones.get(matricula).add(infraccion);
                    }
                }
                 // Actualizar la variable para leer la siguiente línea
            }
        } catch (Exception e) {
            e.printStackTrace(); // Manejar la excepción y mostrar el error
        }

        generarInforme(MapInfracciones);
    }

    private static boolean esMatriculaEspaniola(String Matricula) {
        Matcher matcherActual = MATRICULA_ESPANOLA_ACTUAL.matcher(Matricula);
        Matcher matcherAntiguo = MATRICULA_ESPANOLA_ANTIGUA.matcher(Matricula);
        return matcherActual.matches() || matcherAntiguo.matches();
    }

    private static double calcularImporteMultas(double velocidadregistada, double velocidadpermitida) {
        double exceso = velocidadregistada - velocidadpermitida;

        if (exceso > 40) {
            return 500;
        } else if (exceso > 20) {
            return 200;
        } else if (exceso > 5) {
            return 100;
        }
        return 0;
    }

    private static void generarInforme(Map<String, LinkedList<Infraccion>> MapInfracciones) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(PATHNAME + "informe.txt"))) {
            for (String matricula : MapInfracciones.keySet()) {
                escritor.write("Matrícula: " + matricula);
                escritor.newLine();
                escritor.write("Fecha de infracción | Hora de infracción | Velocidad Registrada | Velocidad Permitida | Importe de Multa");
                escritor.newLine();

                LinkedList<Infraccion> infracciones = MapInfracciones.get(matricula);

                for (Infraccion infraccion : infracciones) {
                    String fechayhora = infraccion.fechayhora.format(Formato_fecha_hora);
                    String[] fechaHoraParts = fechayhora.split(" ");
                    escritor.write(fechaHoraParts[0] + " \t\t|\t\t " + fechaHoraParts[1] + " \t\t|\t\t " +
                            infraccion.velocidadregistada + " \t\t|\t\t " +
                            infraccion.velocidadpermitida + " \t\t|\t\t " +
                            infraccion.importe);
                    escritor.newLine();
                }
                escritor.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Manejar la excepción y mostrar el error
        }
    }
}
