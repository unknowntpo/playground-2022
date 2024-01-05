import mongoose from "mongoose";

const blogSchema = new mongoose.Schema({
    title: String, // String is shorthand for {type: String}
    author: String,
    body: String,
    comments: [{ body: String, date: Date }],
    date: { type: Date, default: Date.now },
    hidden: Boolean,
    meta: {
        votes: Number,
        favs: Number
    }
});

const main = async () => {
    // directConnection is necessary
    await mongoose
        .connect("mongodb://127.0.0.1:30001/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongoose", {
            dbName: "test",
            autoCreate: true,
        })
        .then(() => console.log("Database connected!"))
        .catch(err => console.log(err));

    const blog = mongoose.model('Blog', blogSchema);
    console.log("model blog created");

    const newBlog = new blog({
        title: "My First Blog",
        author: "John Doe",
        body: "This is the content of my first blog post.",
        comments: [{ body: "Great post!", date: new Date() }],
        hidden: false,
        meta: {
            votes: 10,
            favs: 5
        }
    });
    console.log("blog instance created");

    await newBlog.save().then(() => console.log('Blog saved successfully.', newBlog));
}

main()