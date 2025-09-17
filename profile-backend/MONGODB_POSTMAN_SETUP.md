# Instagram Clone API - MongoDB & Postman Setup Guide

## 🗄️ MongoDB Setup

### 1. Install MongoDB
Download and install MongoDB Community Edition from: https://www.mongodb.com/try/download/community

### 2. Start MongoDB Service
```bash
# Windows (Command Prompt as Administrator)
net start MongoDB

# Or start manually
mongod --dbpath C:\data\db
```

### 3. Verify MongoDB is Running
```bash
# Connect to MongoDB shell
mongo
# or for newer versions
mongosh
```

### 4. Create Database and Collections
```javascript
// In MongoDB shell
use instagram_clone
db.users.insertOne({name: "Test User", username: "testuser", email: "test@example.com"})
```

## 📮 Postman Setup

### 1. Import Collection
1. Open Postman
2. Click "Import" button
3. Select `Instagram_Clone_API.postman_collection.json`
4. The collection will be imported with all endpoints

### 2. Set Environment Variables
The collection includes these variables:
- `base_url`: http://localhost:8080
- `user_id`: Will be auto-set after login
- `username`: Will be auto-set after login

### 3. Test Sequence
Follow this order for testing:

#### Step 1: Authentication
1. **User Signup** - Create a new user
2. **User Login** - Login (automatically sets user_id variable)
3. **User Logout** - Test logout functionality

#### Step 2: Profile Management
1. **Get Profile by ID** - Retrieve user profile
2. **Get Profile by Username** - Retrieve profile by username
3. **Update Profile** - Modify profile information

#### Step 3: Follow System
1. **Follow User** - Follow another user (create second user first)
2. **Get Followers** - View followers list
3. **Get Following** - View following list
4. **Unfollow User** - Unfollow a user

#### Step 4: Search & Discovery
1. **Search Users** - Search for users by query
2. **Get All Users** - Retrieve all users

#### Step 5: File Upload
1. **Upload Profile Image** - Upload profile picture
2. **Upload Avatar** - Upload avatar image

## 🔧 API Endpoints Reference

### Authentication
- `POST /api/users/signup` - Register new user
- `POST /api/users/login` - User login
- `POST /api/users/logout` - User logout

### Profile Management
- `GET /api/users/profile/{userId}` - Get profile by ID
- `GET /api/users/profile/username/{username}` - Get profile by username
- `PUT /api/users/profile/{userId}` - Update profile

### Follow System
- `POST /api/users/follow/{followerId}/{followingId}` - Follow user
- `POST /api/users/unfollow/{followerId}/{followingId}` - Unfollow user
- `GET /api/users/followers/{userId}` - Get followers
- `GET /api/users/following/{userId}` - Get following

### Search & Discovery
- `GET /api/users/search?query={query}` - Search users
- `GET /api/users/all` - Get all users

### File Upload
- `POST /api/users/upload-profile-image/{userId}` - Upload profile image
- `POST /api/users/upload-avatar/{userId}` - Upload avatar

## 🧪 Testing Examples

### 1. Create Multiple Users
```json
// User 1
{
  "name": "John Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123"
}

// User 2
{
  "name": "Jane Smith",
  "username": "janesmith",
  "email": "jane@example.com",
  "password": "password456"
}
```

### 2. Update Profile
```json
{
  "name": "John Updated Doe",
  "bio": "Photographer & Travel Enthusiast 📸✈️",
  "isPrivate": false
}
```

### 3. Search Query Examples
- `john` - Find users with "john" in name/username/bio
- `photo` - Find users with "photo" in bio
- `smith` - Find users with "smith" in name/username

## 🐛 Troubleshooting

### MongoDB Issues
1. **Connection Refused**: Ensure MongoDB service is running
2. **Database Not Found**: MongoDB will create database automatically on first insert
3. **Port Issues**: Default MongoDB port is 27017

### API Issues
1. **404 Errors**: Ensure backend is running on port 8080
2. **CORS Errors**: Frontend should connect to http://localhost:8080
3. **File Upload Errors**: Check file size and format (images only)

### Common Error Responses
```json
// User not found
{
  "timestamp": "2024-01-01T00:00:00.000+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "User not found"
}

// Username already exists
{
  "timestamp": "2024-01-01T00:00:00.000+00:00",
  "status": 500,
  "error": "Internal Server Error", 
  "message": "Username already exists"
}
```

## 📊 MongoDB Data Structure

### Users Collection
```json
{
  "_id": "ObjectId",
  "name": "John Doe",
  "username": "johndoe",
  "password": "password123",
  "email": "john@example.com",
  "bio": "Photographer & Travel Enthusiast 📸✈️",
  "avatarUrl": "/uploads/profile-images/...",
  "profileImageUrl": "/uploads/profile-images/...",
  "isPrivate": false,
  "createdAt": "2024-01-01T00:00:00.000Z",
  "updatedAt": "2024-01-01T00:00:00.000Z",
  "followers": ["userId1", "userId2"],
  "following": ["userId3", "userId4"],
  "followersCount": 2,
  "followingCount": 2,
  "postsCount": 0
}
```

## 🚀 Quick Start Commands

```bash
# Start MongoDB
mongod

# Start Backend (in profile-backend directory)
./mvnw spring-boot:run

# Start Frontend (in profile-frontend directory)
npm start
```

## 📝 Notes
- All uploaded images are stored in `uploads/profile-images/` directory
- Images are served at `http://localhost:8080/uploads/...`
- User passwords are stored in plain text (for demo purposes)
- CORS is enabled for `http://localhost:3000` (React frontend)
