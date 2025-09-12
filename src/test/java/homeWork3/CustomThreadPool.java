package homeWork3;

import java.util.LinkedList;

public class CustomThreadPool {
    private final LinkedList<Worker> workers = new LinkedList<>();
    private final LinkedList<Runnable> taskQueue = new LinkedList<>();

    private boolean isShutdown = false;

    public CustomThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker("Worker " + i);
            workers.add(worker);
            worker.start();
        }
    }

    public void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("Задачи больше не принимаются, ранее был вызван метод shutdown()");
        }
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }
    }

    public void shutdown() {
        synchronized (taskQueue) {
            taskQueue.notifyAll();
            isShutdown = true;
        }
    }

    public void awaitTermination() {
        for (Worker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Пришлось прервать выполнение задания потоком " + worker.getName());
            }
        }
    }

    private class Worker extends Thread {

        public String name;

        @Override
        public void run() {
            while (true) {
                Runnable task;
                synchronized (taskQueue) {
                    if (isShutdown && taskQueue.isEmpty()) {
                        return;
                    }
                    while (taskQueue.isEmpty() && !isShutdown) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    task = taskQueue.removeFirst();
                }
                try {
                    task.run();
                } catch (Exception e) {
                    System.out.println("Ошибка выполнения задания в потоке " + getName() + ". Error: " + e.getMessage());
                }
            }
        }

        public Worker(String name) {
            super(name);
        }
    }
}
