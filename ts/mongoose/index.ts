import mongoose from "mongoose";
import { BuilderProgram } from "typescript";

const blogSchema = new mongoose.Schema({
	title: String, // String is shorthand for {type: String}
	author: String,
	body: String,
	comments: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Comment' }],
	date: { type: Date, default: Date.now },
	hidden: Boolean,
	meta: { type: mongoose.Schema.Types.ObjectId, ref: 'Meta' }
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

const commentModel = mongoose.model('Comment', commentSchema);
const metaModel = mongoose.model('Meta', metaSchema);
const blogModel = mongoose.model('Blog', blogSchema);


const main = async () => {
	// directConnection is necessary
	await mongoose
		.connect("mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongoose", {
			dbName: "test",
			autoCreate: true,
		})
		.then(() => console.log("Database connected!"))
		.catch((err: Error) => console.log(err));


	const newMeta = new metaModel({
		votes: 10,
		favs: 5
	});
	await newMeta.save().then(() => console.log('Meta saved successfully.', newMeta));

	const newBlog = new blogModel({
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

	const updatedBlog = await blogModel.findOne({ _id: newBlog._id }).lean();

	await validate(blogSchema);

	await mongoose.connection.close();
}

async function validate(schema: mongoose.Schema) {
	console.log("YYY start validate");
	const model = mongoose.model('Blog', schema);
	const docs = await model.find();
	docs.forEach(async (doc) => {
		await doc.validate();
	})
	console.log("validate OK");
}


async function createNewCommentsForBlog(blog_id: mongoose.Types.ObjectId) {
	let commentIDs: Array<mongoose.Types.ObjectId> = [];
	for (let i = 0; i < 3; i++) {
		const newComment = new commentModel({
			blog_id,
			body: 'hello',
			date: new Date()
		});
		const savedComment = await newComment.save();
		console.log(`comment: ${savedComment} is saved`);
		commentIDs.push(savedComment._id);
	}
	let blogDoc = await blogModel.findOne({ _id: blog_id });
	await blogModel.updateOne(
		{ _id: blog_id },
		{ $set: { comments: commentIDs } }
	);
	// Fetch the updated blogDoc after the update operation
	let updatedBlogDoc = await blogModel.findOne({ _id: blog_id });
	// console.log(`updated blogDoc: ${updatedBlogDoc}`);
}

main()


