import mongoose from "mongoose";

const blogSchema = new mongoose.Schema({
	title: String, // String is shorthand for {type: String}
	author: String,
	body: String,
	comments: [{ body: String, date: Date }],
	date: { type: Date, default: Date.now },
	hidden: Boolean,
	meta: mongoose.Schema.Types.ObjectId
});

const metaSchema = new mongoose.Schema({
	blog_id: mongoose.Types.ObjectId,
	votes: Number,
	favs: Number
})

const main = async () => {
	// directConnection is necessary
	await mongoose
		.connect("mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongoose", {
			dbName: "test",
			autoCreate: true,
		})
		.then(() => console.log("Database connected!"))
		.catch((err: Error) => console.log(err));

	const meta = mongoose.model('Schema', metaSchema);
	console.log("model meta created");

	const blog = mongoose.model('Blog', blogSchema);
	console.log("model blog created");

	const newMeta = new meta({
		votes: 10,
		favs: 5
	});
	await newMeta.save().then(() => console.log('Meta saved successfully.', newMeta));

	const newBlog = new blog({
		title: "My First Blog",
		author: "John Doe",
		body: "This is the content of my first blog post.",
		comments: [{ body: "Great post!", date: new Date() }],
		hidden: false,
		meta: newMeta._id
	});
	console.log("blog instance created");

	await newBlog.save().then(() => console.log('Blog saved successfully.', newBlog));

	await mongoose.connection.close();
}

main()


