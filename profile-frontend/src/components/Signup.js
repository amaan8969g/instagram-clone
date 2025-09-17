import React, { useState } from "react";
import axios from "axios";

export default function Signup({ onSwitch, onSignup }) {
  const [form, setForm] = useState({ name: "", username: "", email: "", password: "" });
  const [error, setError] = useState("");

  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("Attempting signup with:", form);
    try {
      const response = await axios.post("http://localhost:8080/api/users/signup", form);
      console.log("Signup response:", response.data);
      onSignup(response.data);
    } catch (err) {
      console.error("Signup error:", err.response || err);
      setError("Signup failed");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1 className="insta-logo">Instagram</h1>
        <form onSubmit={handleSubmit}>
          <input name="name" className="auth-input" placeholder="Full Name" onChange={handleChange} />
          <input name="username" className="auth-input" placeholder="Username" onChange={handleChange} />
          <input name="email" className="auth-input" placeholder="Email" onChange={handleChange} />
          <input name="password" className="auth-input" type="password" placeholder="Password" onChange={handleChange} />
          <button className="auth-btn" type="submit">Sign Up</button>
        </form>
        {error && <div className="auth-error">{error}</div>}
      </div>
      <div className="auth-box auth-signup">
        Already have an account? <span className="signup-link" onClick={onSwitch}>Login</span>
      </div>
    </div>
  );
} 