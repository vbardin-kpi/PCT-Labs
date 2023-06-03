import mpi.MPI;
import mpi.MPIException;

public class ManyToOne extends ProcessorBase {
    public ManyToOne(String[] args, TaskConfig config) {
        super(args, config);
    }

    @Override
    void run() throws MPIException {
        var startTime = System.currentTimeMillis();

        var a = new double[config.matrixWidth()][config.matrixWidth()];
        var b = new double[config.matrixWidth()][config.matrixWidth()];
        var c = new double[config.matrixWidth()][config.matrixWidth()];

        // Generate partial matrices A and B
        var rowsPerProcess = config.matrixWidth() / mpjState.nodesNumber();
        UtilsExt.fillMatrix(a, mpjState.rank() * rowsPerProcess, (mpjState.rank() + 1) * rowsPerProcess);
        UtilsExt.fillMatrix(b, mpjState.rank() * rowsPerProcess, (mpjState.rank() + 1) * rowsPerProcess);

        // Broadcast partial matrices B to all processes
        for (var i = 0; i < mpjState.nodesNumber(); i++) {
            var b1DToSend = UtilsExt.flatten(b, i * rowsPerProcess, (i + 1) * rowsPerProcess);
            MPI.COMM_WORLD.Bcast(b1DToSend, 0, config.matrixWidth() * rowsPerProcess, MPI.DOUBLE, i);
            if (mpjState.rank() != i) {
                for (var j = 0; j < rowsPerProcess; j++) {
                    System.arraycopy(b1DToSend, j * config.matrixWidth(), b[i * rowsPerProcess + j], 0, config.matrixWidth());
                }
            }
        }

        // Perform matrix multiplication
        for (var i = mpjState.rank() * rowsPerProcess; i < (mpjState.rank() + 1) * rowsPerProcess; i++) {
            for (var j = 0; j < config.matrixWidth(); j++) {
                for (var k = 0; k < config.matrixWidth(); k++) {
                    c[i - mpjState.rank() * rowsPerProcess][j] += a[i][k] * b[k][j];
                }
            }
        }

        // Gather results on master process
        if (mpjState.rank() == 0) {
            for (var i = 1; i < mpjState.nodesNumber(); i++) {
                var temp = new double[rowsPerProcess][config.matrixWidth()];
                for (var j = 0; j < rowsPerProcess; j++) {
                    MPI.COMM_WORLD.Recv(temp[j], 0, config.matrixWidth(), MPI.DOUBLE, i, 0);
                }
                for (var j = 0; j < rowsPerProcess; j++) {
                    System.arraycopy(temp[j], 0, c[i * rowsPerProcess + j], 0, config.matrixWidth());
                }
            }
        } else {
            for (var i = 0; i < rowsPerProcess; i++) {
                MPI.COMM_WORLD.Send(c[i], 0, config.matrixWidth(), MPI.DOUBLE, 0, 0);
            }
        }

        System.out.println("Process " + mpjState.rank() + " took " + (System.currentTimeMillis() - startTime) + " ms to multiply matrices");
        System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + "ms");

        MPI.Finalize();
    }
}
