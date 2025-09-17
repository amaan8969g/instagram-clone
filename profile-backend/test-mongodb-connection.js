// Simple MongoDB connection test script
// Run this with: node test-mongodb-connection.js

const { MongoClient } = require('mongodb');

async function testConnection() {
    const uri = 'mongodb://localhost:27017';
    const client = new MongoClient(uri);
    
    try {
        console.log('🔌 Connecting to MongoDB...');
        await client.connect();
        
        console.log('✅ Connected to MongoDB successfully!');
        
        // Test database operations
        const db = client.db('instagram_clone');
        const collection = db.collection('users');
        
        // Insert a test document
        const testUser = {
            name: 'Test User',
            username: 'testuser',
            email: 'test@example.com',
            password: 'testpassword',
            createdAt: new Date(),
            followers: [],
            following: [],
            followersCount: 0,
            followingCount: 0,
            postsCount: 0
        };
        
        console.log('📝 Inserting test user...');
        const result = await collection.insertOne(testUser);
        console.log('✅ Test user inserted with ID:', result.insertedId);
        
        // Find the test user
        console.log('🔍 Finding test user...');
        const foundUser = await collection.findOne({ username: 'testuser' });
        console.log('✅ Found user:', foundUser);
        
        // Clean up - remove test user
        console.log('🧹 Cleaning up test data...');
        await collection.deleteOne({ _id: result.insertedId });
        console.log('✅ Test data cleaned up');
        
        console.log('🎉 MongoDB connection test completed successfully!');
        
    } catch (error) {
        console.error('❌ MongoDB connection failed:', error.message);
        console.log('\n📋 Troubleshooting tips:');
        console.log('1. Make sure MongoDB is running: mongod');
        console.log('2. Check if MongoDB service is started on Windows');
        console.log('3. Verify MongoDB is listening on port 27017');
    } finally {
        await client.close();
        console.log('🔌 Connection closed');
    }
}

testConnection();


