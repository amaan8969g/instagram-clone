import React, { useState } from "react";
import axios from "axios";

export default function Login({ onLogin, onSwitch }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("Attempting login with:", { username, password });
    try {
      const res = await axios.post(
        "http://localhost:8080/api/users/login",
        { username, password }
      );
      console.log("Login response:", res.data);
      onLogin(res.data);
    } catch (err) {
      console.error("Login error:", err.response || err);
      setError("Invalid credentials");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1 className="insta-logo">Instagram</h1>
        <form onSubmit={handleSubmit}>
          <input
            className="auth-input"
            placeholder="Phone number, username or email address"
            value={username}
            onChange={e => setUsername(e.target.value)}
          />
          <input
            className="auth-input"
            placeholder="Password"
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
          />
          <button className="auth-btn" type="submit">Log in</button>
        </form>
        {error && <div className="auth-error">{error}</div>}
        <div className="auth-or"><span></span>OR<span></span></div>
        <button className="fb-btn" type="button">Log in with Facebook</button>
        <div className="forgot">Forgotten your password?</div>
      </div>
      <div className="auth-box auth-signup">
        Don't have an account? <span className="signup-link" onClick={onSwitch}>Sign up</span>
      </div>
    </div>
  );
} 