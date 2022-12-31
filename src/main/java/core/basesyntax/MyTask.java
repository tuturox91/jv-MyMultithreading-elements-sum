package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class MyTask extends RecursiveTask<Long> {
    private int startPoint;
    private int finishPoint;

    public MyTask(int startPoint, int finishPoint) {
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
    }

    @Override
    protected Long compute() {
        if (Math.abs(finishPoint) - Math.abs(startPoint) > 10) {
            System.out.println("RecursiveAction: Splitting the task"
                    + " with start point " + startPoint + " end point: "
                    + finishPoint + ". " + Thread.currentThread().getName());
            List<RecursiveTask<Long>> subTasks = new ArrayList<>(createSubTasks());
            long result = 0;
            for (RecursiveTask<Long> subTask : subTasks) {
                subTask.fork();
                result += subTask.join();
            }
            return result;
        } else {
            return LongStream.range(startPoint, finishPoint).sum();
        }
    }

    private List<RecursiveTask<Long>> createSubTasks() {
        int newPoint = startPoint + (finishPoint - startPoint) / 2;
        return List.of(
                new MyTask(startPoint, newPoint),
                new MyTask(newPoint, finishPoint));
    }
}
