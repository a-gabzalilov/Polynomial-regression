import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Solve {
    public static final String Delimeters;
    public static final int Max_degree;
    public static final double my_pValue;
    static
    {
        Delimeters = " ";
        Max_degree = 3;
        my_pValue = 0.05;
    }

    private static int my_N = 0;
    private static double[] T_data;
    private static double[] X_data;

    static void Solve (Config config, int chunks) throws IOException{
        FileWriter output_file = new FileWriter(config.get_file_name(Config.ConfigGrammar.OUTPUT_FILE.toString()));
        FileWriter leftovers_file = new FileWriter("leftovers.txt");
        File input_file = new File(config.get_file_name(Config.ConfigGrammar.INPUT_FILE.toString()));
        Scanner scanner = new Scanner(input_file);
        try  {
            //while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                my_N = Integer.parseInt(line);
                T_data = new double[my_N];
                X_data = new double[my_N];
                //double[] T_data = new double[my_N];
                //double[] X_data = new double[my_N];
                line = scanner.nextLine();
                String[] data = line.split(Delimeters);
                for (int i = 0; i < data.length; i++)
                {
                    T_data[i] = Double.parseDouble(data[i]);
                }
                line = scanner.nextLine();
                data = line.split(Delimeters);
                for (int i = 0; i < data.length; i++)
                {
                    X_data[i] = Double.parseDouble(data[i]);
                }
            //}
        }
        catch (Exception exception) {
            Log.Log("log.log", "Troubles with reading input file");
        }
        finally {
            scanner.close();
        }

        for (int l = 0; l < my_N; l+=chunks)
        {
            int right_bound = l + chunks;
            String str = "[" + l + ";"+ right_bound + "]"+ "\n";
            //byte[] byteArray = str.getBytes();
            output_file.write(str);
            for (int row = 0; row<=Max_degree; row++){
                double[][] z_matrix = new double[row + 1][chunks];
                double[] x_vector = new double[chunks];
                for (int j = 0; j < chunks; j++) {
                    for (int i = 0; i <= row; i++) {
                        z_matrix[i][j] = Math.pow(T_data[j+l], i);
                    }
                    x_vector[j] = X_data[j+l];
                }
                SVD solution = new SVD(z_matrix, x_vector);
                Result res = new Result(T_data, X_data, solution, l, chunks, row);
                str = "Degree: " + row + " pValue: " + res.get_pValue(my_pValue)  + " Sigma:" + res.getSigma() + " " +  solution.getBeta() + "\n";
                //byteArray = str.getBytes();
                output_file.write(str);
                for (int i = l; i<l+chunks; i++)
                {
                    str = X_data[i] - solution.get(T_data[i]) + " ";
                    //byteArray = str.getBytes();
                    leftovers_file.write(str);
                }
            }
        }
        output_file.close();
        leftovers_file.close();
    }
}