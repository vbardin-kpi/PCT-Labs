import mpi.MPI;
import mpi.MPIException;

public class OneToOne extends ProcessorBase {
    public OneToOne(String[] args, TaskConfig config) {
        super(args, config);
    }

    @Override
    public void run() throws MPIException {
        var startTime = System.currentTimeMillis();

        var a = new double[config.matrixWidth()][config.matrixWidth()];
        var b = new double[config.matrixWidth()][config.matrixWidth()];
        var c = new double[config.matrixWidth()][config.matrixWidth()];
        var b1DReceived = new double[config.matrixWidth() * config.matrixWidth()];

        if (mpjState.rank() == 0) {
            // fill the matrices with random numbers
            Utils.fillMatrix(a);
            Utils.fillMatrix(b);

            // send matrix B to all slave processes
            var b1D = Utils.flatten(b);
            for (var i = 1; i < mpjState.nodesNumber(); i++) {
                MPI.COMM_WORLD.Send(b1D, 0, config.matrixWidth() * config.matrixWidth(), MPI.DOUBLE, i, 0);
            }

            // send parts of matrix A to all slave processes
            var rowsPerProcess = config.matrixWidth() / mpjState.nodesNumber();
            for (var i = 1; i < mpjState.nodesNumber(); i++) {
                for (var j = i * rowsPerProcess; j < (i + 1) * rowsPerProcess; j++) {
                    MPI.COMM_WORLD.Send(a[j], 0, config.matrixWidth(), MPI.DOUBLE, i, 0);
                }
            }
        } else {
            // receive matrix B on slave processes
            MPI.COMM_WORLD.Recv(b1DReceived, 0, config.matrixWidth() * config.matrixWidth(), MPI.DOUBLE, 0, 0);
            b = Utils.toMatrix(b1DReceived, config.matrixWidth(), config.matrixWidth());
        }

        // receive parts of matrix A on slave processes
        var rowsPerProcess = config.matrixWidth() / mpjState.nodesNumber();
        for (var i = mpjState.rank() * rowsPerProcess; i < (mpjState.rank() + 1) * rowsPerProcess; i++) {
            if (mpjState.rank() != 0) {
                MPI.COMM_WORLD.Recv(a[i], 0, config.matrixWidth(), MPI.DOUBLE, 0, 0);
            }

            // perform matrix multiplication on each process
            for (var j = 0; j < config.matrixWidth(); j++) {
                for (var k = 0; k < config.matrixWidth(); k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        // send the results back to the master process
        if (mpjState.rank() != 0) {
            for (var i = mpjState.rank() * rowsPerProcess; i < (mpjState.rank() + 1) * rowsPerProcess; i++) {
                MPI.COMM_WORLD.Send(c[i], 0, config.matrixWidth(), MPI.DOUBLE, 0, 0);
            }
        } else {
            for (var i = 1; i < mpjState.nodesNumber(); i++) {
                for (var j = i * rowsPerProcess; j < (i + 1) * rowsPerProcess; j++) {
                    MPI.COMM_WORLD.Recv(c[j], 0, config.matrixWidth(), MPI.DOUBLE, i, 0);
                }
            }
        }

        if (mpjState.rank() == 0) {
            System.out.println("The result of matrix multiplication:");
            //printMatrix(c);
        }
        System.out.println("Process " + mpjState.rank() + " took " + (System.currentTimeMillis() - startTime) + " ms to multiply matrices");
        System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + "ms");

        MPI.Finalize();
    }
}