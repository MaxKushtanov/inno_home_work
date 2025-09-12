package homeWork3;

public class HomeWork3 {

    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool(3);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            pool.execute(() -> {
                System.out.printf("Task %d started by %s\n", finalI, Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //ignore
                }
                System.out.printf("Task %d finished\n", finalI);
            });
        }
        pool.shutdown();
        pool.awaitTermination();
        System.out.println("All tasks finished");
    }

}
