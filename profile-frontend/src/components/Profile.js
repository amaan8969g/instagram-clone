import React, { useState, useEffect } from 'react';
import './Profile.css';

const Profile = ({ currentUser, onLogout }) => {
  const [profile, setProfile] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editedProfile, setEditedProfile] = useState({});
  const [followers, setFollowers] = useState([]);
  const [following, setFollowing] = useState([]);
  const [showFollowers, setShowFollowers] = useState(false);
  const [showFollowing, setShowFollowing] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [showSearch, setShowSearch] = useState(false);
  const [uploading, setUploading] = useState(false);

  useEffect(() => {
    if (currentUser) {
      fetchProfile();
    }
  }, [currentUser]);

  const fetchProfile = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/users/profile/username/${currentUser.username}`);
      if (response.ok) {
        const profileData = await response.json();
        setProfile(profileData);
        setEditedProfile(profileData);
      }
    } catch (error) {
      console.error('Error fetching profile:', error);
    }
  };

  const handleEdit = () => {
    setIsEditing(true);
    setEditedProfile({ ...profile });
  };

  const handleSave = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/users/profile/${profile.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editedProfile),
      });

      if (response.ok) {
        const updatedProfile = await response.json();
        setProfile(updatedProfile);
        setIsEditing(false);
      }
    } catch (error) {
      console.error('Error updating profile:', error);
    }
  };

  const handleCancel = () => {
    setIsEditing(false);
    setEditedProfile({ ...profile });
  };

  const handleImageUpload = async (file, type) => {
    setUploading(true);
    const formData = new FormData();
    formData.append('file', file);

    try {
      const endpoint = type === 'avatar' 
        ? `http://localhost:8080/api/users/upload-avatar/${profile.id}`
        : `http://localhost:8080/api/users/upload-profile-image/${profile.id}`;
      
      const response = await fetch(endpoint, {
        method: 'POST',
        body: formData,
      });

      if (response.ok) {
        const result = await response.json();
        const updatedProfile = { ...profile };
        if (type === 'avatar') {
          updatedProfile.avatarUrl = result.avatarUrl;
        } else {
          updatedProfile.profileImageUrl = result.imageUrl;
        }
        setProfile(updatedProfile);
      }
    } catch (error) {
      console.error('Error uploading image:', error);
    } finally {
      setUploading(false);
    }
  };

  const handleFollow = async (userId) => {
    try {
      const response = await fetch(`http://localhost:8080/api/users/follow/${currentUser.id}/${userId}`, {
        method: 'POST',
      });

      if (response.ok) {
        fetchProfile(); // Refresh profile to update counts
      }
    } catch (error) {
      console.error('Error following user:', error);
    }
  };

  const handleUnfollow = async (userId) => {
    try {
      const response = await fetch(`http://localhost:8080/api/users/unfollow/${currentUser.id}/${userId}`, {
        method: 'POST',
      });

      if (response.ok) {
        fetchProfile(); // Refresh profile to update counts
      }
    } catch (error) {
      console.error('Error unfollowing user:', error);
    }
  };

  const fetchFollowers = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/users/followers/${profile.id}`);
      if (response.ok) {
        const followersData = await response.json();
        setFollowers(followersData);
        setShowFollowers(true);
      }
    } catch (error) {
      console.error('Error fetching followers:', error);
    }
  };

  const fetchFollowing = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/users/following/${profile.id}`);
      if (response.ok) {
        const followingData = await response.json();
        setFollowing(followingData);
        setShowFollowing(true);
      }
    } catch (error) {
      console.error('Error fetching following:', error);
    }
  };

  const searchUsers = async (query) => {
    if (query.trim() === '') {
      setSearchResults([]);
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/users/search?query=${encodeURIComponent(query)}`);
      if (response.ok) {
        const results = await response.json();
        setSearchResults(results);
      }
    } catch (error) {
      console.error('Error searching users:', error);
    }
  };

  if (!profile) {
    return <div className="profile-loading">Loading profile...</div>;
  }

  return (
    <div className="profile-container">
      <div className="profile-header">
        <button className="logout-btn" onClick={onLogout}>
          Logout
        </button>
        <h1>Profile</h1>
        <button 
          className="search-btn" 
          onClick={() => setShowSearch(!showSearch)}
        >
          Search Users
        </button>
      </div>

      {showSearch && (
        <div className="search-container">
          <input
            type="text"
            placeholder="Search users..."
            value={searchQuery}
            onChange={(e) => {
              setSearchQuery(e.target.value);
              searchUsers(e.target.value);
            }}
            className="search-input"
          />
          <div className="search-results">
            {searchResults.map((user) => (
              <div key={user.id} className="search-result-item">
                <div className="user-info">
                  <img 
                    src={user.avatarUrl || '/default-avatar.png'} 
                    alt={user.username}
                    className="search-avatar"
                  />
                  <div>
                    <h4>{user.username}</h4>
                    <p>{user.name}</p>
                    {user.bio && <p className="bio">{user.bio}</p>}
                  </div>
                </div>
                {user.id !== currentUser.id && (
                  <button 
                    className="follow-btn"
                    onClick={() => handleFollow(user.id)}
                  >
                    Follow
                  </button>
                )}
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="profile-content">
        <div className="profile-info">
          <div className="profile-image-section">
            <div className="image-upload-container">
              <img 
                src={profile.profileImageUrl || '/default-profile.png'} 
                alt="Profile"
                className="profile-image"
              />
              <input
                type="file"
                accept="image/*"
                onChange={(e) => handleImageUpload(e.target.files[0], 'profile')}
                className="image-upload-input"
                id="profile-image-upload"
              />
              <label htmlFor="profile-image-upload" className="upload-label">
                {uploading ? 'Uploading...' : 'Change Profile Image'}
              </label>
            </div>
            
            <div className="avatar-upload-container">
              <img 
                src={profile.avatarUrl || '/default-avatar.png'} 
                alt="Avatar"
                className="avatar-image"
              />
              <input
                type="file"
                accept="image/*"
                onChange={(e) => handleImageUpload(e.target.files[0], 'avatar')}
                className="image-upload-input"
                id="avatar-upload"
              />
              <label htmlFor="avatar-upload" className="upload-label">
                {uploading ? 'Uploading...' : 'Change Avatar'}
              </label>
            </div>
          </div>

          <div className="profile-details">
            {isEditing ? (
              <div className="edit-form">
                <input
                  type="text"
                  value={editedProfile.name || ''}
                  onChange={(e) => setEditedProfile({...editedProfile, name: e.target.value})}
                  placeholder="Full Name"
                  className="edit-input"
                />
                <input
                  type="text"
                  value={editedProfile.username || ''}
                  onChange={(e) => setEditedProfile({...editedProfile, username: e.target.value})}
                  placeholder="Username"
                  className="edit-input"
                />
                <textarea
                  value={editedProfile.bio || ''}
                  onChange={(e) => setEditedProfile({...editedProfile, bio: e.target.value})}
                  placeholder="Bio"
                  className="edit-textarea"
                />
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={editedProfile.isPrivate || false}
                    onChange={(e) => setEditedProfile({...editedProfile, isPrivate: e.target.checked})}
                  />
                  Private Account
                </label>
                <div className="edit-buttons">
                  <button onClick={handleSave} className="save-btn">Save</button>
                  <button onClick={handleCancel} className="cancel-btn">Cancel</button>
                </div>
              </div>
            ) : (
              <div className="profile-text">
                <h2>{profile.name}</h2>
                <p className="username">@{profile.username}</p>
                {profile.bio && <p className="bio">{profile.bio}</p>}
                {profile.isPrivate && <p className="private-indicator">🔒 Private Account</p>}
                <button onClick={handleEdit} className="edit-btn">Edit Profile</button>
              </div>
            )}
          </div>
        </div>

        <div className="profile-stats">
          <div className="stat-item">
            <span className="stat-number">{profile.postsCount || 0}</span>
            <span className="stat-label">Posts</span>
          </div>
          <div className="stat-item" onClick={fetchFollowers}>
            <span className="stat-number">{profile.followersCount || 0}</span>
            <span className="stat-label">Followers</span>
          </div>
          <div className="stat-item" onClick={fetchFollowing}>
            <span className="stat-number">{profile.followingCount || 0}</span>
            <span className="stat-label">Following</span>
          </div>
        </div>

        {(showFollowers || showFollowing) && (
          <div className="follow-modal">
            <div className="follow-content">
              <div className="follow-header">
                <h3>{showFollowers ? 'Followers' : 'Following'}</h3>
                <button onClick={() => {
                  setShowFollowers(false);
                  setShowFollowing(false);
                }}>×</button>
              </div>
              <div className="follow-list">
                {(showFollowers ? followers : following).map((user) => (
                  <div key={user.id} className="follow-item">
                    <img 
                      src={user.avatarUrl || '/default-avatar.png'} 
                      alt={user.username}
                      className="follow-avatar"
                    />
                    <div className="follow-info">
                      <h4>{user.username}</h4>
                      <p>{user.name}</p>
                    </div>
                    {user.id !== currentUser.id && (
                      <button 
                        className="follow-btn"
                        onClick={() => handleFollow(user.id)}
                      >
                        Follow
                      </button>
                    )}
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Profile;
