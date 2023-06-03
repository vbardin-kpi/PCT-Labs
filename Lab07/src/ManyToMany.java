import mpi.MPI;
import mpi.MPIException;

public class ManyToMany extends ProcessorBase {
    public ManyToMany(String[] args, TaskConfig config) {
        super(args, config);
    }

    @Override
    public void run() throws MPIException {
        long startTime = System.currentTimeMillis();

        double[][] a = new double[config.matrixWidth()][config.matrixWidth()];
        double[][] b = new double[config.matrixWidth()][config.matrixWidth()];
        double[][] c = new double[config.matrixWidth()][config.matrixWidth()];

        // Generate partial matrices A and B
        int rowsPerProcess = config.matrixWidth() / mpjState.nodesNumber();
        UtilsExt.fillMatrix(a, mpjState.rank() * rowsPerProcess, (mpjState.rank() + 1) * rowsPerProcess);
        UtilsExt.fillMatrix(b, mpjState.rank() * rowsPerProcess, (mpjState.rank() + 1) * rowsPerProcess);

        // Broadcast partial matrices B to all processes
        for (int i = 0; i < mpjState.nodesNumber(); i++) {
            double[] b1DToSend = UtilsExt.flatten(b, i * rowsPerProcess, (i + 1) * rowsPerProcess);
            MPI.COMM_WORLD.Bcast(b1DToSend, 0, config.matrixWidth() * rowsPerProcess, MPI.DOUBLE, i);
            if (mpjState.rank() != i) {
                for (int j = 0; j < rowsPerProcess; j++) {
                    System.arraycopy(b1DToSend, j * config.matrixWidth(), b[i * rowsPerProcess + j], 0, config.matrixWidth());
                }
            }
        }

        // Perform matrix multiplication
        for (int i = mpjState.rank() * rowsPerProcess; i < (mpjState.rank() + 1) * rowsPerProcess; i++) {
            for (int j = 0; j < config.matrixWidth(); j++) {
                for (int k = 0; k < config.matrixWidth(); k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        // All processes use Allgather to collect and exchange results
        double[] c1DToSend = UtilsExt.flatten(c, mpjState.rank() * rowsPerProcess, (mpjState.rank() + 1) * rowsPerProcess);
        double[] c1DToRecv = new double[config.matrixWidth() * config.matrixWidth()];
        int[] sendCounts = new int[mpjState.nodesNumber()];
        int[] displacements = new int[mpjState.nodesNumber()];

        for (int i = 0; i < mpjState.nodesNumber(); i++) {
            sendCounts[i] = config.matrixWidth() * rowsPerProcess;
            displacements[i] = i * config.matrixWidth() * rowsPerProcess;
        }

        MPI.COMM_WORLD.Allgatherv(c1DToSend, 0, config.matrixWidth() * rowsPerProcess, MPI.DOUBLE, c1DToRecv, 0, sendCounts, displacements, MPI.DOUBLE);
        for (int i = 0; i < mpjState.nodesNumber(); i++) {
            for (int j = 0; j < rowsPerProcess; j++) {
                System.arraycopy(c1DToRecv, (i * rowsPerProcess + j) * config.matrixWidth(), c[i * rowsPerProcess + j], 0, config.matrixWidth());
            }
        }

        System.out.println("Process " + mpjState.rank() + " took " + (System.currentTimeMillis() - startTime) + " ms to multiply matrices");
        System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + "ms");

        MPI.Finalize();
    }
}