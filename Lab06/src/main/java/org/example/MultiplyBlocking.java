package org.example;

import mpi.MPI;

public class MultiplyBlocking extends MultiplierBase {
    static int m1Tag = 20;
    static int m2Tag = 22;
    static int responseTag = 24;
    static MatrixGenerator generator = new MatrixGenerator();
    static int size = 4;

    int nodeId;

    public MultiplyBlocking(String[] args, TaskConfig taskConfig) {
        super(args, taskConfig);
    }

    @Override
    protected void masterThreadLogic() {
        long startTime = System.currentTimeMillis();

        var matrix1 = generator.generate(size, size, 5);
        var matrix2 = generator.generate(size, size, 5);
        var result = new int[matrix1.length][matrix2[0].length];

        for (var i = 1; i < state.nodesNumber(); i++) {
            var endIndex = i == state.nodesNumber() - 1
                    ? ((i + 1) * state.chunkSize()) + state.reminder()
                    : (i + 1) * state.chunkSize();

            var flatM1 = MatrixHelpers.flattenMatrix(
                    MatrixHelpers.getSubMatrix(matrix1, i * state.chunkSize(), endIndex));
            MPI.COMM_WORLD.Send(flatM1, 0, flatM1.length, MPI.INT, i, m1Tag);

            var flatM2 = MatrixHelpers.flattenMatrix(matrix2);
            MPI.COMM_WORLD.Send(flatM2, 0, flatM2.length, MPI.INT, i, m2Tag);
        }

        for (var i = 1; i < state.nodesNumber(); i++) {
            var rowCount = i == state.nodesNumber() - 1
                    ? state.chunkSize() + state.reminder()
                    : state.chunkSize();
            var flatChunk = new int[rowCount * matrix1.length];

            MPI.COMM_WORLD.Recv(flatChunk, 0, flatChunk.length, MPI.INT, i, responseTag);

            var matrixChunk = MatrixHelpers.reshapeMatrix(flatChunk, matrix1.length);
            for (var b = 0; b < matrixChunk.length; b++) {
                System.arraycopy(matrixChunk[b], 0, result[(i - 1) * state.chunkSize() + b], 0, result[b].length);
            }
        }
        var time = System.currentTimeMillis() - startTime;
        MatrixHelpers.print(result);
        System.out.println("Duration - " + time);
    }

    @Override
    protected void slaveThreadLogic() {
        var rowNum = (nodeId == state.nodesNumber() - 1) ? state.chunkSize() + state.reminder() : state.chunkSize();
        var responseChunk = new int[rowNum][size];

        var flatChunk1 = new int[rowNum * size];
        MPI.COMM_WORLD.Recv(flatChunk1, 0, flatChunk1.length, MPI.INT, 0, m1Tag);
        var chunk1 = MatrixHelpers.reshapeMatrix(flatChunk1, size);

        var flatChunk2 = new int[size * size];
        MPI.COMM_WORLD.Recv(flatChunk2, 0, flatChunk2.length, MPI.INT, 0, m2Tag);
        var matrix2 = MatrixHelpers.reshapeMatrix(flatChunk2, size);

        MultiplyNonBlocking.fillResultMatrix(responseChunk, chunk1, matrix2);

        var flatResp = MatrixHelpers.flattenMatrix(responseChunk);
        MPI.COMM_WORLD.Send(flatResp, 0, flatResp.length, MPI.INT, 0, responseTag);
    }

    public void multiplyBlocking() {
        nodeId = MPI.COMM_WORLD.Rank();

        var isMaster = nodeId == 0;
        if (isMaster) {
            masterThreadLogic();
        } else {
            slaveThreadLogic();
        }

        MPI.Finalize();
    }
}