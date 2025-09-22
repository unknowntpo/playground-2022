import {UrlDownloader} from "../../src/downloader/url_downloader";
import * as fs from "node:fs";
import * as path from "node:path";
import {expect} from "vitest";

describe('url_downloader', () => {
    it('can download file', async () => {
        const downloader = new UrlDownloader();
        const task = {
            id: 0,
            name: 'cat.image',
            url: 'https://images.pexels.com/photos/45201/kitty-cat-kitten-pet-45201.jpeg',
        };
        downloader.addTask(task);
        await downloader.doTasks();

        const doneTask = downloader.getDoneTask()[0];
        const {filePath, ...taskWithoutFilePath} = doneTask;
        expect(taskWithoutFilePath).toStrictEqual(task);
        expect(doneTask.filePath).toBeDefined();

        const stat = await fs.promises.stat(path.resolve(doneTask.filePath!));
        expect(stat.isFile()).toBe(true);
    })
});