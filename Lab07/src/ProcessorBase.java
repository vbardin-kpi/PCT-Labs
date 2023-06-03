import mpi.MPI;
import mpi.MPIException;

public abstract class ProcessorBase
{
    protected final TaskConfig config;
    protected MpjState mpjState;

    public ProcessorBase(String[] args, TaskConfig config) {
        this.config = config;
        InitMpj(args);
    }

    protected void InitMpj(String[] args) {
        MPI.Init(args);
        int computingNodesNumber = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();

        if (config.matrixWidth() % computingNodesNumber != 0) {
            if (rank == 0) {
                System.err.println("The dimensionality of the matrix must be a multiple of the number of processes.");
            }
            MPI.Finalize();
            return;
        }

        mpjState = new MpjState(computingNodesNumber, rank);
    }

    abstract void run() throws MPIException;
}
