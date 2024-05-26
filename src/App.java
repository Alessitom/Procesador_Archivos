import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

    static final String PATHNAME = "./archivos/";
    static final DateTimeFormatter FormatoFechayhora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    static final Pattern MATRICULA_ESPANOLA_ACTUAL = Pattern.compile("\\b\\d{4}\\s*[BCDFGHJKLMNPRSTVWXYZ]{3}\\b");
    static final Pattern MATRICULA_ESPANOLA_ANTIGUA = Pattern.compile("\\b[A-Z]{1,3}[-\\s]?\\d{4}[-\\s]?[A-Z]{1,2}\\b");



    static class Infraccion{
        LocalDateTime Fechayhora;
        double velocidadpermitida;
        double velocidadRegistrada;
        double importeMulta;

        public Infraccion(LocalDateTime fechayhora, double velocidadpermitida, double velocidadRegistrada,
                double importeMulta) {
            Fechayhora = fechayhora;
            this.velocidadpermitida = velocidadpermitida;
            this.velocidadRegistrada = velocidadRegistrada;
            this.importeMulta = importeMulta;
        }

        
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner (System.in);
        System.out.println("Que archivo quieres leer?");
        String archivo = sc.nextLine();
        Map<String,List<Infraccion>>infracciones = new LinkedHashMap<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(PATHNAME+archivo))){
            String Linea = lector.readLine();
            while(Linea!=null){
                StringTokenizer tokenizer = new StringTokenizer(Linea, "#$&");

                if(tokenizer.countTokens() == 5){
                    String fechaString = tokenizer.nextToken().trim();
                    String horaString = tokenizer.nextToken().trim();
                    double velocidadRegistrada = Double.parseDouble(tokenizer.nextToken().trim());
                    double velocidadpermitida = Double.parseDouble(tokenizer.nextToken().trim());
                    String matricula = tokenizer.nextToken().trim().replaceAll("[\\s-]", "");
                    
                    if(esMstriculaEspanola(matricula)){
                        
                    }

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    private static boolean esMstriculaEspanola(String Matricula){

        Matcher matcherActual = MATRICULA_ESPANOLA_ACTUAL.matcher(Matricula);
        Matcher matcherAntiguo = MATRICULA_ESPANOLA_ANTIGUA.matcher(Matricula);

        return matcherActual.matches() || matcherAntiguo.matches();

    }

    private static void generarInforme(){

    }
}
