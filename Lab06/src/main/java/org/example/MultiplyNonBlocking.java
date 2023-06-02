package org.example;

import mpi.MPI;
import mpi.Request;

public class MultiplyNonBlocking extends MultiplierBase {
    private static final int m1Tag = 20;
    private static final int m2Tag = 22;
    private static final int responseTag = 24;
    private static final MatrixGenerator generator = new MatrixGenerator();

    private int nodeId = 0;

    private final int[][] matrix1 = generator.generate(getTaskConfig().matrixWidth(), getTaskConfig().matrixWidth(), 5);
    private final int[][] matrix2 = generator.generate(getTaskConfig().matrixWidth(), getTaskConfig().matrixWidth(), 5);

    public MultiplyNonBlocking(String[] args, TaskConfig taskConfig) {
        super(args, taskConfig);
    }

    @Override
    public void multiply() {
        nodeId = MPI.COMM_WORLD.Rank();
        var isMaster = nodeId == state.nodesNumber() - 1;

        if (isMaster) {
            masterThreadLogic();
        } else {
            slaveThreadLogic();
        }
        MPI.Finalize();
    }

    protected void masterThreadLogic() {
        var startTime = System.currentTimeMillis();
        Request[] results = new Request[state.nodesNumber() - 1];
        var flatResult = new int[matrix1.length * matrix2[0].length];

        for (var i = 0; i < (state.nodesNumber() - 1); i++) {
            var startIndex = i * state.chunkSize();
            var endIndex = (i == state.nodesNumber() - 2)
                    ? (i + 1) * state.chunkSize() + state.reminder()
                    : (i + 1) * state.chunkSize();

            var flatM1 = MatrixHelpers.flattenMatrix(MatrixHelpers.getSubMatrix(matrix1, startIndex, endIndex));
            MPI.COMM_WORLD.Isend(flatM1, 0, flatM1.length, MPI.INT, i, m1Tag);

            var flatM2 = MatrixHelpers.flattenMatrix(matrix2);
            MPI.COMM_WORLD.Isend(flatM2, 0, flatM2.length, MPI.INT, i, m2Tag);

            results[i] = MPI.COMM_WORLD.Irecv(flatResult,
                    startIndex * getTaskConfig().matrixWidth(),
                    getTaskConfig().matrixWidth() * (endIndex - startIndex),
                    MPI.INT, i, responseTag);
        }
        Request.Waitall(results);
        var time = System.currentTimeMillis() - startTime;
        System.out.println(this.getClass().getSimpleName() + " // Duration - " + time);
    }

    protected void slaveThreadLogic() {
        var rowNum = (nodeId == state.nodesNumber() - 2) ? state.chunkSize() + state.reminder() : state.chunkSize();
        var responseChunk = new int[rowNum][matrix1.length];

        var flatChunk1 = new int[rowNum * matrix1.length];
        MPI.COMM_WORLD.Recv(flatChunk1, 0, flatChunk1.length, MPI.INT, state.nodesNumber() - 1, m1Tag);
        var chunk1 = MatrixHelpers.reshapeMatrix(flatChunk1, matrix1.length);

        var flatChunk2 = new int[matrix2.length * matrix2[0].length];
        MPI.COMM_WORLD.Recv(flatChunk2, 0, flatChunk2.length, MPI.INT, state.nodesNumber() - 1, m2Tag);
        var matrix2 = MatrixHelpers.reshapeMatrix(flatChunk2, matrix1.length);

        fillResultMatrix(responseChunk, chunk1, matrix2);

        int[] flatResp = MatrixHelpers.flattenMatrix(responseChunk);
        MPI.COMM_WORLD.Send(flatResp, 0, flatResp.length, MPI.INT, state.nodesNumber() - 1, responseTag);
    }
}
