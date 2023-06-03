import mpi.MPI;
import mpi.MPIException;

public class OneToMany extends ProcessorBase {
    public OneToMany(String[] args, TaskConfig config) {
        super(args, config);
    }

    public void run() throws MPIException {
        var startTime = System.currentTimeMillis();

        var a = new double[config.matrixWidth()][config.matrixWidth()];
        var b = new double[config.matrixWidth()][config.matrixWidth()];

        if (mpjState.rank() == 0) {
            // fill the matrices with random numbers
            Utils.fillMatrix(a);
            Utils.fillMatrix(b);
        }

        // broadcast matrix B to all processes
        var b1D = Utils.flatten(b);
        MPI.COMM_WORLD.Bcast(b1D, 0, config.matrixWidth() * config.matrixWidth(), MPI.DOUBLE, 0);
        b = Utils.toMatrix(b1D, config.matrixWidth(), config.matrixWidth());

        // scatter parts of matrix A to all processes
        var rowsPerProcess = config.matrixWidth() / mpjState.nodesNumber();
        var a1D = Utils.flatten(a);
        var aPart1D = new double[rowsPerProcess * config.matrixWidth()];
        MPI.COMM_WORLD.Scatter(a1D, 0, rowsPerProcess * config.matrixWidth(), MPI.DOUBLE, aPart1D, 0, rowsPerProcess * config.matrixWidth(), MPI.DOUBLE, 0);
        var aPart = Utils.toMatrix(aPart1D, rowsPerProcess, config.matrixWidth());

        // perform matrix multiplication on each process
        var cPart = new double[rowsPerProcess][config.matrixWidth()];
        for (var i = 0; i < rowsPerProcess; i++) {
            for (var j = 0; j < config.matrixWidth(); j++) {
                for (var k = 0; k < config.matrixWidth(); k++) {
                    cPart[i][j] += aPart[i][k] * b[k][j];
                }
            }
        }

        // gather the results on the master process
        var cPart1D = Utils.flatten(cPart);
        var c1D = new double[config.matrixWidth() * config.matrixWidth()];
        MPI.COMM_WORLD.Gather(cPart1D, 0, rowsPerProcess * config.matrixWidth(), MPI.DOUBLE, c1D, 0, rowsPerProcess * config.matrixWidth(), MPI.DOUBLE, 0);

        if (mpjState.rank() == 0) {
            System.out.println("The result of matrix multiplication:");
             //printMatrix(c);
        }
        System.out.println("Process " + mpjState.rank() + " took " + (System.currentTimeMillis() - startTime)  + " ms to multiply matrices");

        System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + "ms");

        MPI.Finalize();
    }
}