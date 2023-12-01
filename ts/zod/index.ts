import { z } from "zod"

const mySchema = z.string();

const UserSchema = z.object({
    username: z.string(),
    age: z.number().positive("age must be greater than 0"),
})

type User = z.infer<typeof UserSchema>

export const createUser = (username: string, age: number): User | undefined => {
    try {
        return UserSchema.parse({ username, age })
    } catch (e) {
        console.error(e)
        return undefined
    }
}


let u1 = createUser("Betty", -1)
let u2 = createUser("Kelly", 3)

console.log(u1, u2)
