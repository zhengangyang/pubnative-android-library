package net.pubnative.library.managers.task;

public abstract class TaskItem {
    public interface TaskItemListener {
        /**
         * Invoked when the task is completely executed
         *
         * @param item Task Item object used to run the task
         */
        void onTaskItemListenerFinished(TaskItem item);

        /**
         * Invoked when the task execution fails
         *
         * @param item Task Item object used to run the task
         * @param e    Exception that caused the failure
         */
        void onTaskItemListenerFailed(TaskItem item, Exception e);
    }

    private TaskItemListener managerListener;
    private TaskItemListener listener;

    public TaskItem(TaskItemListener listener) {
        this.listener = listener;
    }

    /**
     * Executes the Task
     *
     * @param listener Listener to track the task execution behavior
     */
    public void execute(TaskItemListener listener) {
        this.managerListener = listener;
        this.onExecute();
    }

    public abstract void onExecute();

    protected void invokeOnTaskItemListenerFinished() {
        if (this.managerListener != null) {
            this.managerListener.onTaskItemListenerFinished(this);
        }
        if (this.listener != null) {
            this.listener.onTaskItemListenerFinished(this);
        }
    }

    protected void invokeOnTaskItemListenerFailed(Exception error) {
        if (this.managerListener != null) {
            this.managerListener.onTaskItemListenerFailed(this, error);
        }
        if (this.listener != null) {
            this.listener.onTaskItemListenerFailed(this, error);
        }
    }
}
