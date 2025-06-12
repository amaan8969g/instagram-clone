import React from "react";
import "./Auth.css";

export default function Profile({ user }) {
  return (
    <div className="profile-container">
      <h2>Login Successful!</h2>
      <h2>Welcome, {user.name}!</h2>
      <img src="/amaan.jpg" alt="Profile" className="profile-img" />
      <div className="profile-info">Username: {user.username}</div>
      <div className="profile-info">Email: {user.email}</div>
    </div>
  );
} 