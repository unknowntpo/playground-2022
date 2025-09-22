import {UrlDownloader} from "../../src/downloader/url_downloader";
import * as fs from "node:fs";
import * as path from "node:path";

describe('url_downloader', () => {
    it('can download file', async () => {
        const downloader = new UrlDownloader();
        const task = {
            id: 0,
            name: 'cat.image',
            url: 'https://images.pexels.com/photos/45201/kitty-cat-kitten-pet-45201.jpeg',
            do: () => {
                console.log("task id[0] are resolving...");
                console.log("done");
                return;
            }
        };
        downloader.addTask(task);
        await downloader.doTasks();

        expect(downloader.getDoneTask()).toStrictEqual([task]);
        const stat = await fs.promises.stat(path.join("./downloads", task.name));
        expect(stat.isFile()).toBe(true);
    })
});