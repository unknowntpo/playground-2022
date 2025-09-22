import {URL} from "url";
import * as fs from "node:fs";
import {Readable} from 'stream';
import {pipeline} from 'stream/promises';

interface Task {
    id: number
    name: string,
    url: string,
    filePath?: string;
}

/**
 * init all tasks, including subtasks, and after resolving them, done is set to true
 */
class UrlDownloader {
    private tasks: Task[] = [];
    private doneTasks: Task[] = [];

    constructor() {
    }

    public addTask(task: Task) {
        // defensive copying
        this.tasks.push({...task});
    }

    getDoneTask() {
        return this.doneTasks;
    }

    async download(task: Task): Promise<Task> {
        const url = new URL(task.url);
        const resp = await fetch(url);
        if (!resp.body) {
            throw new Error(`taskName: ${task.name} response body cannot be null, url: ${task.url}`);
        }

        // Get file extension from Content-Type
        const extension = this.getExtensionFromHeaders(resp.headers);

        const fileName = extension ? `${task.name}.${extension}` : task.name;
        const filePath = `./downloads/${fileName}`;

        const dataStream = Readable.fromWeb(resp.body);
        const outputStream = fs.createWriteStream(filePath);

        await pipeline(dataStream, outputStream);

        return {...task, filePath};
    }

    private async createOutputDir() {
        await fs.promises.mkdir('./downloads', {recursive: true});
    }

    private getExtensionFromHeaders(headers: Headers): string | null {
        const contentType = headers.get('content-type');

        if (!contentType) return null;

        const mimeToExt: Record<string, string> = {
            'image/jpeg': 'jpeg',
            'image/jpg': 'jpg',
            'image/png': 'png',
            'image/gif': 'gif',
            'image/webp': 'webp',
            'text/html': 'html',
            'application/pdf': 'pdf',
            'text/plain': 'txt'
        };

        return mimeToExt[contentType.split(';')[0]] || null;
    }

    async doTasks() {
        await this.createOutputDir();
        for (const task of this.tasks) {
            console.log(`taskID: [${task.id}] downloading ${task.name}, url ${task.url}`);
            const doneTask = await this.download(task);
            console.log(`${doneTask.name} is downloaded.`);
            this.doneTasks.push(doneTask);
        }
    }
}

export {UrlDownloader};