
interface Task {
    id: number
    name: string,
    url: string,

    // TODO: exception handling
    do: () => void;
}

/**
 * init all tasks, including subtasks, and after resolving them, done is set to true
 */
class Url_downloader {
    private tasks: Task[] = [];
    private done: boolean = false;
    private doneTasks: Task[] = [];

    constructor() {
    }

    public addTask(task: Task) {
        this.tasks.push(task);
    }

    getDoneTask() {
        return this.doneTasks;
    }

    private allocateWorkers() {
        return;
    }

    async doTasks() {
        this.allocateWorkers();
        for (const task of this.tasks) {
            task.do();
            this.doneTasks.push(task);
        }
    }
}

export {Url_downloader};