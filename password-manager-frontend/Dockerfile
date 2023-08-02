FROM node:14-alpine
EXPOSE 3000
WORKDIR /app
# Copy package.json and package-lock.json to the working directory
COPY package*.json ./
RUN npm install
# Copy the entire project to the working directory
COPY . .
RUN npm run build
ENTRYPOINT ["npm", "start"]
