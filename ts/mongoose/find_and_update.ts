import mongoose, { ValidatorProps } from "mongoose";

const blogSchema = new mongoose.Schema<blog>({
	title: String, // String is shorthand for {type: String}
	author: String,
	body: String,
}, { timestamps: true });

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
	mongoose.set('debug', true)
	await mongoose
		.connect("mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongoose", {
			dbName: "test",
			autoCreate: true,
		})
		.then(() => console.log("Database connected!"))
		.catch((err: Error) => console.log(err))

	await blogModel.deleteMany({})

	const blog = new blogModel({ "title": "first blog", "author": "John Doe", "body": "0" })

	await blog.save()

	// update partial doc, not including createdAt, updatedAt
	const updatedBlog: blog | null = await blogModel.findOneAndUpdate({ _id: blog._id }, { "title": "second blog", createdAt: new Date("1900-01-06T01:01:01.000Z"), }).lean();
	assertNotNull(updatedBlog)
	console.log(`updatedBlog: ${JSON.stringify(updatedBlog, null, "\t")}`)



	// replace ->replace ful doc, including createdAt, updatedAt
	const replacedBlog: blog | null = await blogModel.findOneAndReplace({ _id: blog._id }, { "title": "second blog", createdAt: new Date("1900-01-06T01:01:01.000Z"), }).lean();
	assertNotNull(replacedBlog)
	console.log(`replacedBlog: ${JSON.stringify(replacedBlog, null, "\t")}`)

	await mongoose.connection.close()
}

main()

function assertNotNull(blog: blog | null) {
	if (!blog) { throw new Error("blog should not be null") };
}

