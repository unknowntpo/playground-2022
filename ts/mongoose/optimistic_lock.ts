import mongoose, { ValidatorProps } from "mongoose";
import { networkInterfaces } from "os";

const blogSchema = new mongoose.Schema<blog>({
	title: String, // String is shorthand for {type: String}
	author: String,
	body: String,
	// }, { optimisticConcurrency: true, versionKey: true });
}, { optimisticConcurrency: true, versionKey: '__v' });


interface blog {
	title: String, // String is shorthand for {type: String}
	author: String,
	body: String,
}

const blogModel = mongoose.model<blog>('Blog', blogSchema);

const models = [
	blogModel,
]


const main = async () => {
	// directConnection is necessary
	await mongoose
		.connect("mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongoose", {
			dbName: "test",
			autoCreate: true,
		})
		.then(() => console.log("Database connected!"))
		.catch((err: Error) => console.log(err));

	// await blogModel.deleteMany({});

	// const newBlog = new blogModel({
	// 	title: "My First Blog",
	// 	author: "John Doe",
	// 	body: "This is the content of my first blog post.",
	// });
	// console.log("blog instance created");

	// await newBlog.save().then(() => console.log('Blog saved successfully.', newBlog));

	const promises = Array.from({ length: 10, }, async () => {
		return new Promise<void>(async (resolve) => {
			const oldBlog = await blogModel.findOne({ title: "My First Blog" })
			if (!oldBlog) {
				throw new Error(`no old blog`)
			}

			oldBlog['body'] = Math.random().toString()
			await oldBlog.save();
			// await oldBlog.updateOne({
			// 	body: Math.random().toString()
			// });
			console.log('updated');
			resolve();
		})
	})

	console.log(promises.length)
	console.log(promises[0] === promises[3])
	await Promise.all(promises)
	await mongoose.connection.close()
}

main()

