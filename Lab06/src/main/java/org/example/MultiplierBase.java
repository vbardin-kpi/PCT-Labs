package org.example;

import mpi.MPI;

public abstract class MultiplierBase {
    protected final MpjState state;
    private final TaskConfig taskConfig;

    public MultiplierBase(String[] args, TaskConfig taskConfig) {
        this.taskConfig = taskConfig;

        state = InitMpj(args, taskConfig);
    }

    protected MpjState InitMpj(String[] args, TaskConfig taskConfig) {
        MPI.Init(args);
        var mpi_size = MPI.COMM_WORLD.Size();
        var remainder = taskConfig.matrixWidth() % (mpi_size - 1);
        var chunkSize = Math.floorDiv(taskConfig.matrixWidth(), mpi_size - 1);

        return new MpjState(mpi_size, remainder, chunkSize);
    }

    protected abstract void masterThreadLogic();
    protected abstract void slaveThreadLogic();

    protected void fillResultMatrix(int[][] responseChunk, int[][] chunk1, int[][] matrix2) {
        for (var i = 0; i < chunk1.length; i++) {
            for (var j = 0; j < matrix2[0].length; j++) {
                for (var n = 0; n < matrix2.length; n++) {
                    responseChunk[i][j] += chunk1[i][n] * matrix2[n][j];
                }
            }
        }
    }
}
