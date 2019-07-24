package ru.kuptservol;

import java.util.Random;

import Jama.Matrix;
import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;
import org.jblas.DoubleMatrix;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;

/**
 * @author Sergey Kuptsov
 * Benchmark                                            (shape)  Mode  Cnt        Score         Error  Units
 * MatrixMultiplicationBench.ejmlRank2                       10  avgt    5        1,935 ±       1,890  us/op
 * MatrixMultiplicationBench.ejmlRank2                      100  avgt    5     1479,927 ±    1830,287  us/op
 * MatrixMultiplicationBench.ejmlRank2                     1000  avgt    5  1973162,428 ± 1584677,156  us/op
 * MatrixMultiplicationBench.jBlasRank2                      10  avgt    5        2,747 ±       1,311  us/op
 * MatrixMultiplicationBench.jBlasRank2                     100  avgt    5      557,052 ±     299,997  us/op
 * MatrixMultiplicationBench.jBlasRank2                    1000  avgt    5   321523,133 ±  228659,835  us/op
 * MatrixMultiplicationBench.jamaRank2                       10  avgt    5        2,599 ±       2,438  us/op
 * MatrixMultiplicationBench.jamaRank2                      100  avgt    5     1119,908 ±     270,989  us/op
 * MatrixMultiplicationBench.jamaRank2                     1000  avgt    5  2520840,558 ±  801118,670  us/op
 * MatrixMultiplicationBench.javaArrayElementWiseRank1       10  avgt    5        1,261 ±       2,211  us/op
 * MatrixMultiplicationBench.javaArrayElementWiseRank1      100  avgt    5       38,497 ±      25,064  us/op
 * MatrixMultiplicationBench.javaArrayElementWiseRank1     1000  avgt    5     5935,165 ±    4746,994  us/op
 * MatrixMultiplicationBench.javaArrayElementWiseRank2       10  avgt    5        3,620 ±       4,265  us/op
 * MatrixMultiplicationBench.javaArrayElementWiseRank2      100  avgt    5     1662,731 ±    2363,664  us/op
 * MatrixMultiplicationBench.javaArrayElementWiseRank2     1000  avgt    5  7458950,798 ± 5143724,480  us/op
 */
@State(value = Scope.Benchmark)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class MatrixMultiplicationBench {

    Random r = new Random();

    double[] rank1A;

    double[][] rank2A;
    Matrix rank2JamaM;
    DoubleMatrix rank2JBlasM;
    SimpleMatrix rank2ejmlM;


    //@Param({"10", "100", "1000", "10000"})
    @Param({"10", "100", "1000"})
    public int shape;

    @Setup
    public void prepare() {
        rank1A = new double[shape];

        for (int i = 0; i < shape; i++) {
            rank1A[i] = r.nextDouble();
        }

        rank2A = new double[shape][shape];
        for (int i = 0; i < shape; i++) {
            for (int j = 0; j < shape; j++) {
                rank2A[i][j] = r.nextDouble();
            }
        }

        rank2JamaM = new Matrix(rank2A);
        rank2JBlasM = new DoubleMatrix(rank2A);
        rank2ejmlM = SimpleMatrix.wrap(new DMatrixRMaj(rank2A));
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public double[][] javaArrayElementWiseRank1() {
        int aRows = rank1A.length;
        int bCols = rank1A.length;

        double[][] result = new double[aRows][bCols];

        for (int lrow = 0; lrow < aRows; lrow++) {
            for (int rcol = 0; rcol < bCols; rcol++) {

                result[lrow][rcol] = rank1A[lrow] * rank1A[rcol];
            }
        }

        return result;
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public double[][] javaArrayElementWiseRank2() {
        int aRows = rank2A.length;
        int n = rank2A.length;

        int bColumns = rank2A[0].length;
        double[][] result = new double[aRows][bColumns];

        for (int lrow = 0; lrow < aRows; lrow++) {
            for (int rcol = 0; rcol < bColumns; rcol++) {

                double resVal = 0;
                for (int i = 0; i < n; i++) {
                    resVal += rank2A[lrow][i] * rank2A[i][rcol];
                }

                result[lrow][rcol] = resVal;
            }
        }

        return result;
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public Matrix jamaRank2() {
        return rank2JamaM.times(rank2JamaM);
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public DoubleMatrix jBlasRank2() {
        return rank2JBlasM.mmul(rank2JBlasM);
    }

    @Benchmark
    @BenchmarkMode(value = AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    public SimpleMatrix ejmlRank2() {
        return rank2ejmlM.mult(rank2ejmlM);
    }
}
