import mongoose, { ValidatorProps } from "mongoose";
import { networkInterfaces } from "os";

const blogSchema = new mongoose.Schema<blogDoc>({
  title: String, // String is shorthand for {type: String}
  author: String,
  body: String,
  comments: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: "Comment",
  }],
  date: { type: Date, default: Date.now },
  hidden: Boolean,
  meta: { type: mongoose.Schema.Types.ObjectId, ref: "Meta" },
});

//   interface ValidateFn<T> {
//     (value: T, props?: ValidatorProps & Record<string, any>): boolean;
//   }

interface blogDoc {
  title: String; // String is shorthand for {type: String}
  author: String;
  body: String;
  comments: [mongoose.Types.ObjectId];
  date: Date;
  hidden: Boolean;
  meta: mongoose.Schema.Types.ObjectId;
}

interface commentDoc {
  blog_id: mongoose.Types.ObjectId;
  body: String;
  date: Date;
  nested_comment: mongoose.Types.ObjectId;
  nested_comment_body: String;
  nested_comment_date: Date;
}

const commentSchema = new mongoose.Schema<commentDoc>({
  blog_id: { type: mongoose.Schema.Types.ObjectId, ref: "Blog" },
  body: String,
  date: Date,
  nested_comment: { type: mongoose.Schema.Types.ObjectId, ref: "Comment" },
  nested_comment_body: {
    type: mongoose.Schema.Types.String,
    validate: {
      // (value: T, props?: ValidatorProps & Record<string, any>): Promise<boolean>;
      // validator: async<T>(value: T, props: ValidatorProps & Record<string, any>): Promise<boolean> => {
      // 	const nested_comment_id = this.get("nested_comment");
      // 	const nested_comment = commentModel.find({ _id: nested_comment_id });
      // 	const { body, date } = nested_comment;
      // 	console.log(`KKK here`);
      // 	return body == this.get('body') && date == this.get('date');
      // }
      validator: async function(
        this: commentDoc,
        value: String,
      ): Promise<boolean> {
        const nested_comment_id = this.nested_comment;
        const nested_comment = await commentModel.findOne({
          _id: nested_comment_id,
        }).lean();
        if (!nested_comment) {
          throw new Error(`should exist`);
        }
        const { body, date } = nested_comment!;
        console.log(`KKK here`);

        return this.nested_comment_body == nested_comment.body &&
          this.nested_comment_date == nested_comment.date;
      },
    },
  },
  nested_comment_date: Date,
});

const metaSchema = new mongoose.Schema<metaDoc>({
  blog_id: mongoose.Types.ObjectId,
  votes: Number,
  favs: Number,
});

interface meta {
  blog_id: mongoose.Types.ObjectId;
  votes: Number;
  favs: Number;
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

const commentModel = mongoose.model("Comment", commentSchema);
const metaModel = mongoose.model("Meta", metaSchema);
const blogModel = mongoose.model("Blog", blogSchema);

const models = [
  commentModel,
  metaModel,
  blogModel,
];

const main = async () => {
  // directConnection is necessary
  await mongoose
    .connect(
      "mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongoose",
      {
        dbName: "test",
        autoCreate: true,
      },
    )
    .then(() => console.log("Database connected!"))
    .catch((err: Error) => console.log(err));

  const newMeta = new metaModel({
    votes: 10,
    favs: 5,
  });
  await newMeta.save().then(() =>
    console.log("Meta saved successfully.", newMeta)
  );

  const newBlog = new blogModel({
    title: "My First Blog",
    author: "John Doe",
    body: "This is the content of my first blog post.",
    comments: [],
    hidden: false,
    meta: newMeta._id,
  });
  console.log("blog instance created");

  const savedBlog = await newBlog.save().then(() =>
    console.log("Blog saved successfully.", newBlog)
  );

  await createNewCommentsForBlog(newBlog._id);

  const updatedBlog = await blogModel.findOne({ _id: newBlog._id }).lean();

  await validate();

  // const populatedBlog = await blogModel.
  // 	find({}).
  // 	populate({
  // 		path: 'comments', populate: {
  // 			path: 'nested_comment',
  // 			model: 'Comment'
  // 		}
  // 	}).
  // 	exec();
  // console.log(`XXXX Populated blog: ${populatedBlog}`);
  await mongoose.connection.close();
};

async function validate() {
  console.log("YYY start validate");
  const comments = await commentModel.find({});
  for (const comment of comments) {
    await comment.validate();
  }
}

async function createNewCommentsForBlog(blog_id: mongoose.Types.ObjectId) {
  let commentIDs: Array<mongoose.Types.ObjectId> = [];
  const rootComment = new commentModel({
    blog_id,
    body: "hello",
    date: new Date(),
  });
  const savedRootComment = await rootComment.save();

  console.log(`saved root comment: ${rootComment}`);

  for (let i = 0; i < 3; i++) {
    const newComment = new commentModel({
      blog_id,
      body: "hello",
      date: new Date(),
      nested_comment: savedRootComment._id,
      // nested_comment: new mongoose.Types.ObjectId()
      // nested_comment_body: savedRootComment.body + "testModify",
      nested_comment_date: savedRootComment.date,
    });
    const savedComment = await newComment.save();
    console.log(`comment: ${savedComment} is saved`);
    commentIDs.push(savedComment._id);
  }
  let blogDoc = await blogModel.findOne({ _id: blog_id });
  await blogModel.updateOne(
    { _id: blog_id },
    { $set: { comments: commentIDs } },
  );
  // Fetch the updated blogDoc after the update operation
  //let updatedBlogDoc = await blogModel.findOne({ _id: blog_id });

  console.log(`test not found`);
  let updatedBlogDoc = await blogModel.findOne({ title: "fdsfsdfasdf" });

  console.log(`not found updated result: ${updatedBlogDoc}`);
}

main();
