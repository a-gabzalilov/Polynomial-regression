import java.util.Random;

public class Sample {
    private Random rand = new Random();
    private double std;
    private double[] coefficient;


    public Sample(double std1, double[] coef)  {
        this.std = std1;
        this.coefficient = coef.clone();
    }

    public double get(double t) {
        double res = 0.;
        for (int i = 0; i < this.coefficient.length; i++) {
            res += this.coefficient[i] * Math.pow(t, i);
        }
        res += this.rand.nextGaussian() * this.std;
        return res;
    }
}
