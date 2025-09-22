import {Url_downloader} from "../../src/downloader/url_downloader";

describe('url_downloader', () => {
    it('can download file', async () => {
        const downloader = new Url_downloader();
        const task = {
            id: 0,
            name: 'cat.image',
            url: 'https://www.pexels.com/search/cat/',
            do: () => {
                console.log("task id[0] are resolving...");
                console.log("done");
                return;
            }
        };
        downloader.addTask(task);
        await downloader.doTasks();
        expect(downloader.getDoneTask()).toStrictEqual([task]);
    })
});