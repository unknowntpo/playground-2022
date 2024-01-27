# Use the official Node.js 18 image as a parent image
FROM node:18

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the package.json and yarn.lock files into the container at /usr/src/app
COPY package*.json yarn.lock ./

# Install dependencies in the container
RUN yarn install

# Copy the rest of your app's source code from your host to your image filesystem
COPY . .

# Your app binds to port 4444, so use the EXPOSE instruction to have it mapped by the docker daemon
EXPOSE 4444

# Define the command to run your app using CMD which defines your runtime
CMD ["yarn", "start"]