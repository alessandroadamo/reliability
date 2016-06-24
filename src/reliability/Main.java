package reliability;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {

        ArrayList<Double> time = new ArrayList();
        ArrayList<Integer> type = new ArrayList();
        StringTokenizer st;

        BufferedReader br = null;

        
        try {
            // Load the example file
            System.out.println(br = new BufferedReader(new FileReader(Paths
                    .get(new java.io.File(".").getCanonicalPath()
                            + "\\\\reliab3.csv").toString())));

            br.readLine(); // read header
            String line = br.readLine();

            while (line != null) {

                st = new StringTokenizer(line, ";");

                time.add(new Double(st.nextToken()));
                type.add(new Integer(st.nextToken()));

                line = br.readLine();

            }

        } finally {
            br.close();
        }

        double[] data = new double[time.size()];
        boolean[] censored = new boolean[time.size()];

        for (int i = 0; i < time.size(); i++) {
            data[i] = time.get(i).doubleValue();
            if (type.get(i).intValue() == 0) {
                censored[i] = false;
            } else {
                censored[i] = true;
            }
        }

        WeibullDistribution dist = (WeibullDistribution) WeibullDistribution.estimate(data, censored, 100, 1e-6);

        // System.out.println(dist.estimate(data, censored, 100, 1e-6));
        for (int i = 0; i < 100; i++) {
            System.out.println(dist.random());
        }

    }

}
