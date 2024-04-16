import mongoose from "mongoose";

const blogSchema = new mongoose.Schema({
	title: String, // String is shorthand for {type: String}
	author: String,
	body: String,
	comments: [mongoose.Schema.Types.ObjectId],
	date: { type: Date, default: Date.now },
	hidden: Boolean,
	meta: mongoose.Schema.Types.ObjectId
});

interface blogDoc {
	title: String, // String is shorthand for {type: String}
	author: String,
	body: String,
	comments: [mongoose.Schema.Types.ObjectId],
	date: Date,
	hidden: Boolean,
	meta: mongoose.Schema.Types.ObjectId
}

interface commentDoc {
	blog_id: mongoose.Types.ObjectId,
	body: String,
	date: Date
}

const commentSchema = new mongoose.Schema<commentDoc>({
	blog_id: mongoose.Types.ObjectId,
	body: String,
	date: Date
})

const metaSchema = new mongoose.Schema<metaDoc>({
	blog_id: mongoose.Types.ObjectId,
	votes: Number,
	favs: Number
});

interface meta {
	blog_id: mongoose.Types.ObjectId,
	votes: Number,
	favs: Number
}

type metaDoc = meta & BaseDocument;


interface BaseDocument {
	/** [mongodb native] Document 的 ID */
	_id: mongoose.Types.ObjectId;
	/** [mongoose native] Document 的建立時間 */
	createdAt: Date;
	/** [mongoose native] Document 的最後更新時間 */
	updatedAt: Date;
}

const comment = mongoose.model('Comment', commentSchema);
const meta = mongoose.model('Meta', metaSchema);
const blog = mongoose.model('Blog', blogSchema);


const main = async () => {
	// directConnection is necessary
	await mongoose
		.connect("mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongoose", {
			dbName: "test",
			autoCreate: true,
		})
		.then(() => console.log("Database connected!"))
		.catch((err: Error) => console.log(err));


	const newMeta = new meta({
		votes: 10,
		favs: 5
	});
	await newMeta.save().then(() => console.log('Meta saved successfully.', newMeta));

	const newBlog = new blog({
		title: "My First Blog",
		author: "John Doe",
		body: "This is the content of my first blog post.",
		comments: [],
		hidden: false,
		meta: newMeta._id
	});
	console.log("blog instance created");

	const savedBlog = await newBlog.save().then(() => console.log('Blog saved successfully.', newBlog));

	await createNewCommentsForBlog(newBlog._id)

	const updatedBlog = await blog.findOne({ _id: newBlog._id }).lean();

	peekStructure(blogSchema);

	await mongoose.connection.close();
}

async function peekStructure(schema: mongoose.Schema) {
	// const blogDoc = await blog.findOne({ _id: blog_id });
	// console.log(`inspect on blogDoc: ${blog.(blogDoc!)}`);
	// blog.insp
	for (const [k, v] of Object.entries(schema.paths)) {
		if (k !== '_id' && v.instance === 'ObjectId') {
			console.log(`${k.toString()} has type ObjectId`);
		}
	}
}

async function createNewCommentsForBlog(blog_id: mongoose.Types.ObjectId) {
	let commentIDs: Array<mongoose.Types.ObjectId> = [];
	for (let i = 0; i < 3; i++) {
		const newComment = new comment({
			blog_id,
			body: 'hello',
			date: new Date()
		});
		const savedComment = await newComment.save();
		console.log(`comment: ${savedComment} is saved`);
		commentIDs.push(savedComment._id);
	}
	let blogDoc = await blog.findOne({ _id: blog_id });
	await blog.updateOne(
		{ _id: blog_id },
		{ $set: { comments: commentIDs } }
	);
	// Fetch the updated blogDoc after the update operation
	let updatedBlogDoc = await blog.findOne({ _id: blog_id });
	// console.log(`updated blogDoc: ${updatedBlogDoc}`);
}

main()


